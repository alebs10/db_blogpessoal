package com.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

//Pra receber requisições.
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
	
	public ResponseEntity< List<Postagem>> getAll(){ 
		return ResponseEntity.ok(postagemRepository.findAll()); //Ok --> 200, metodo findAll equivae ao select*from do MySql
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){ //@PathVariable variável do caminho para minha uri
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
		
		//select * from tb_postagens where id = 1;
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){ 
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo)); 		//Ok --> 200, metodo findAll equivae ao select*from do MySql
		
		//select * from tb_postagens where titulo like "%titulo%"
	}
	
	@PostMapping //Criação de Dados
	public ResponseEntity <Postagem> postPostagem(@Valid @RequestBody Postagem postagem){ //Corpo da REQUISIÇÃO
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
	}
	
	@PutMapping //Criação de Dados
	public ResponseEntity <Postagem> putPostagem(@Valid @RequestBody Postagem postagem){
		
		return postagemRepository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.ok().body(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.notFound().build());
		}
		
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePostagem(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta -> {
					postagemRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
}
