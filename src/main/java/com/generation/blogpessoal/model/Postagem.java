package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;//Trablhamos com JPA.
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity//Create Table
@Table(name = "tb_postagens") //Informar o nome da tabela dentro do banco de dados ou seja postgem
public class Postagem {

	@Id // Primary Key (id)
	@GeneratedValue(strategy = GenerationType.IDENTITY)//MySql gera a chave primaria = auto_increment.
	private Long id ;
	
	@NotBlank (message = "O atributo titulo é obrigatorio") //Não permitir que tenha espaco em branco e se é nulo
	@Size(min = 5, max = 100, message = "O atributo titulo deve conter no minimo 5 e maximo 100 caracteres!") //definir o tamanho da string
	private String titulo;
	
	@NotNull (message = "O atributo texto é obrigatorio") //Não permitir que tenha espaco em branco e se é nulo
	@Size(min = 10, max = 1000, message = "O atributo texto deve conter no minimo 5 e maximo 100 caracteres!")
	private String texto;
	
	@UpdateTimestamp //Pega o relgoio do windows a hora do momento e grava no banco.
	private LocalDateTime data;
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Tema tema;
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Usuario usuario;


	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

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
