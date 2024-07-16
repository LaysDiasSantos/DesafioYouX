package com.example.demo.testepratico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.testepratico.model.Cliente;
import com.example.demo.testepratico.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente save(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	// Método para listar todos os clientes
	public List<Cliente> listarClientes() {
		return clienteRepository.findAll();
	}
}
