package com.desafioJava.desafioJava.repository;


import com.desafioJava.desafioJava.model.Portifolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortifolioRepository extends JpaRepository<Portifolio, Long> {
    Optional<Portifolio> findById(Long id);

}
