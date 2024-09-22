package com.desafioJava.desafioJava.repository;


import com.desafioJava.desafioJava.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
