package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.JwtHelper;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private static final String BASE_URL = "/usuarios";
	private static final String USUARIO = "root@root.com";
	private static final String SENHA = "rootroot";

	@BeforeAll
	void inicio() {
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Root", USUARIO, SENHA));
	}

	@Test
	@DisplayName("01 - Deve cadastrar um novo usuário com sucesso")
	void deveCadastrarUsuario() {
		// Given - Cenário
		Usuario usuario = TestBuilder.criarUsuario(null, "Sofia", "sofiiagomes34@gmail.com", "Sofia123");

		// When - Ação
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				requisicao, Usuario.class);

		// Then - É exatamente o que esperamos
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}

	@Test
	@DisplayName("02 - Não deve cadastrar usuário duplicado")
	void naoDeveCadastrarUsuarioDuplicado() {
		// Given - Cenário
		Usuario usuario = TestBuilder.criarUsuario(null, "Igor Reis", "igor_reis@gmail.com", "Igor123");
		usuarioService.cadastrarUsuario(usuario);

		// When - Ação
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				requisicao, Usuario.class);

		// Then - É exatamente o que esperamos
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertNull(resposta.getBody());
	}

	@Test
	@DisplayName("03 - Deve atualizar os dados do usuário com sucesso")
	void deveAtualizarUmUsuario() {
		// Given - Cenário
		Usuario usuario = TestBuilder.criarUsuario(null, "Mariana", "mari223@gmail.com", "Mari123");
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);

		Usuario usuarioUpdate = TestBuilder.criarUsuario(usuarioCadastrado.get().getId(), "Mariana Luz",
				"mari223@gmail.com", "Mari1234");

		// When - Ação

		// Gerar o Token
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);

		// Criar a requisição com o token
		HttpEntity<Usuario> requisicao = JwtHelper.criarRequisicaoComToken(usuarioUpdate, token);

		// Enviar a Requisição PUT
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/atualizar", HttpMethod.PUT,
				requisicao, Usuario.class);

		// Then - É exatamente o que esperamos
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}

	@Test
	@DisplayName("04 - Deve Listar os usuários com sucesso")
	void deveListarTodosUsuarios() {

		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Luana Maria", "luma@gmail.com", "Luma123"));
		usuarioService
				.cadastrarUsuario(TestBuilder.criarUsuario(null, "Anitta Luisa", "AnittaLu@gmail.com", "AnaLu123"));

		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);

		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);

		ResponseEntity<List<Usuario>> resposta = testRestTemplate.exchange(BASE_URL + "/all", HttpMethod.GET,
				requisicao, new ParameterizedTypeReference<List<Usuario>>() {
				});

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertFalse(resposta.getBody().isEmpty());
		assertTrue(resposta.getBody().size() > 0);
	}

	@Test
	@DisplayName("05 - Deve buscar um usuário por ID com sucesso")
	void deveBuscarUsuarioPorId() {
		Long id = usuarioService
				.cadastrarUsuario(TestBuilder.criarUsuario(null, "Camila Fernandes", "cami.fer@gmail.com", "C@mil4F!"))
				.get().getId();

		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);

		HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);

		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET, requisicao,
				Usuario.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
		assertEquals(id, resposta.getBody().getId());
	}

}
