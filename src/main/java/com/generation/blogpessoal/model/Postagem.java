package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity // Define que é um model
@Table(name = "tb_postagens") // Define o nome da model no banco de dados
public class Postagem {
	
	@Id // Define a chave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTOINCREMENT
	private Long id;
	
	@Column(length = 100)
	@NotBlank(message = "O atributo titulo é obrigatório!")
	@Size(min = 5, max = 100, message = "O atributo titulo deve conter no mínimo 5 e no máximo 100 caracteres!")
	private String titulo;
	
	@Column(length = 1000)
	@NotBlank(message = "O atributo texto é obrigatório!")
	@Size(min = 10, max = 1000, message = "O atributo texto deve conter no mínimo 10 e no máximo 1000 caracteres!")
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	
}
