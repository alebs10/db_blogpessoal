package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate; //Criar uma requisição, substitui o Insomnia.

	@Autowired
	private UsuarioService usuarioService;

    @Autowired
	private UsuarioRepository usuarioRepository;
    
    @BeforeAll
	void start(){

		usuarioRepository.deleteAll();
	}
    
    @Test
	@Order(1) //Começa o primeiro teste para ser executado
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() {

    	//Criar requisição com um objeto da classe usuario chamado requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));

		//. exchange envia a os dados, corpo da requisição é o usuario "Paulo"
		ResponseEntity<Usuario> resposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
	}

    @Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário") //tentativa de duplicar o usuário na base de dados
	public void naoDeveDuplicarUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

		ResponseEntity<Usuario> resposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
    
    @Test
	@Order(3)
	@DisplayName("Alterar um Usuário")
	public void deveAtualizarUmUsuario() {

		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Juliana Andrews", "juliana_andrews@email.com.br", 
			"juliana123", "https://i.imgur.com/yDRVeK7.jpg"));

		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), 
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", 
			"juliana123", "https://i.imgur.com/yDRVeK7.jpg");
		
		//enviar para minha requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
	}
    
    @Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {

		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Sabrina Sanches", "sabrina_sanches@email.com.br", 
			"sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
			"Ricardo Marques", "ricardo_marques@email.com.br", 
			"ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));

		//null porque somente mostraremos os dados, sem retornar uma requisição e o String.class vai mostrar a lista e dados
		ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root", "root")//meu token
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
    
    @Test
    @Order(5)
    @DisplayName("Buscar usuário pelo ID")
    public void mostrarUsuarioPeloId() {
    	
    	Optional<Usuario> usuarioGet = usuarioService.cadastrarUsuario(new Usuario(0L, 
    			"Alexandre", "alexandre@email.com.br", 
    			"ale123", "https://i.imgur.com/yDRVeK7.jpg"));
    	
    	ResponseEntity<String> resposta = testRestTemplate
    			.withBasicAuth("root", "root")//meu token
    			.exchange("/usuarios/"+ usuarioGet.get().getId(), HttpMethod.GET, null, String.class);

    		assertEquals(HttpStatus.OK, resposta.getStatusCode());
    	
    }
    
    @Test
    @Order(6)
    @DisplayName("Login")
    public void usuarioLogin() {
    	
    	usuarioService.cadastrarUsuario(new Usuario(0L, 
    			"Alexandre", "ale@email.com.br", "churrasco", "https://i.imgur.com/T12NIp9.jpg"));
    	
    	HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<UsuarioLogin>(new UsuarioLogin(0L, 
    			"", "ale@email.com.br", "churrasco", "", ""));
    	
    	ResponseEntity<UsuarioLogin> corpoResposta = testRestTemplate
    			.exchange("/usuarios/logar", HttpMethod.POST, corpoRequisicao, UsuarioLogin.class);
    	
    	assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
    	
    }
}
