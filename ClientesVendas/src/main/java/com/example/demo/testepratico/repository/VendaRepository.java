package com.example.demo.testepratico.repository;

import com.example.demo.testepratico.model.Venda;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {
	List<Venda> findByCliente_Nome(String nome);

	boolean existsByClienteCodigo(Integer codigo);

	Optional<Venda> findByCodigo(Integer codigo);

}