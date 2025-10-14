package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.blogpessoal.model.Postagem;


public interface PostagemRepository extends JpaRepository<Postagem, Long>{
	// Herdando o JpaRepository alguns metodos prontos ficam implicito
	public List <Postagem> findAllByTituloContainingIgnoreCase(String titulo);
	
	// SELECT * FROM tb_postagens WHERE titulo LIKE = "%?%";
}
