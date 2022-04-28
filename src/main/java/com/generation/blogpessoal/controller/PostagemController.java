package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*") //Permite requisições de qulquer servidor dentro da minha aplicação. e AllowedHeades pra acessar o token para liberar a requisição
public class PostagemController {

	//Injeção de dependencia - transefere a responsabilidade de criar e istanciar pro Spring e o JPA
	@Autowired
	private PostagemRepository postagemRepository; //
	
	@GetMapping //Trazer resposta para minha requisição
	//devolver uma entidade de resposta http
	
	/*getAll erá do tipo ResponseEntity porque 
	 * ele responderá a requisição (Request),
	 * com uma HTTP Response (Resposta http)*/
	
	public ResponseEntity<List<Postagem>> getAll(){ 
		return ResponseEntity.ok(postagemRepository.findAll()); //Ok --> 200, metodo findAll equivae ao select*from do MySql
	}
}
