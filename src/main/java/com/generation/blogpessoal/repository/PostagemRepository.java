package com.generation.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.Postagem;

@Repository //Tipo repository Jpa repository possui vários métodos,
public interface PostagemRepository extends JpaRepository <Postagem, Long> {

}
