package com.example.demo.testepratico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.testepratico.model.Venda;
import com.example.demo.testepratico.repository.VendaRepository;

@Service
public class VendaService {
	@Autowired
	private VendaRepository vendaRepository;

	public Venda save(Venda venda) {
		return vendaRepository.save(venda);
	}

	public List<Venda> buscarVendasPorNomeDoCliente(String nome) {
		return vendaRepository.findByCliente_Nome(nome);
	}

}
