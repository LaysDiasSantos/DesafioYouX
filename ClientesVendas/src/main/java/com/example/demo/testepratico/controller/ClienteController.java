package com.example.demo.testepratico.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.example.demo.testepratico.repository.ClienteRepository;
import com.example.demo.testepratico.repository.VendaRepository;
import jakarta.validation.Valid;

import com.example.demo.testepratico.model.Cliente;

@Controller
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private VendaRepository vendaRepository;

	private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

	@GetMapping("/criarCliente")
	public String exibirCriarCliente(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return "criarCliente.html";
	}

	@PostMapping("/criarCliente")
	public String criarCliente(@Valid @ModelAttribute Cliente cliente, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("cliente", cliente);
			return "criarCliente";
		}

		clienteRepository.save(cliente);
		return "redirect:/listarClientes";
	}

	@GetMapping("/removerCliente")
	public String removerCliente(@RequestParam("codigo") Integer codigo, RedirectAttributes redirectAttributes) {
		try {
			if (vendaRepository.existsByClienteCodigo(codigo)) {
				redirectAttributes.addFlashAttribute("error",
						"Não é possível excluir o cliente porque ele está vinculado a uma venda.");
				return "redirect:/listarClientes";
			}
			clienteRepository.deleteById(codigo);
		} catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("error",
					"Erro ao excluir o cliente. Ele pode estar vinculado a uma venda.");
			return "redirect:/listarClientes";
		}
		return "redirect:/listarClientes";
	}

	@GetMapping("/listarClientes")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("listarClientes");
		mv.addObject("listarClientes", clienteRepository.findAll());
		return mv;
	}

	@GetMapping("/exibirEditarCliente")
	public ModelAndView exibirCliente(@RequestParam Integer codigo) {
		ModelAndView mv = new ModelAndView("editarCliente");
		mv.addObject("cliente", clienteRepository.findById(codigo).orElse(new Cliente()));
		return mv;
	}

	@PostMapping("/editarCliente/{codigo}")
	public String editarCliente(@PathVariable("codigo") Integer codigo,
			@Valid @ModelAttribute Cliente clienteAtualizado, BindingResult result,
			@RequestParam("action") String action, RedirectAttributes redirectAttributes) {

		if ("cancel".equals(action)) {
			return "redirect:/listarClientes";
		}

		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("cliente", clienteAtualizado);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cliente", result);
			return "redirect:/exibirEditarCliente?codigo=" + codigo;
		}

		Optional<Cliente> clienteOpt = clienteRepository.findById(codigo);

		if (clienteOpt.isPresent()) {
			Cliente cliente = clienteOpt.get();
			cliente.setNome(clienteAtualizado.getNome());
			cliente.setCnpj(clienteAtualizado.getCnpj());
			cliente.setEmail(clienteAtualizado.getEmail());
			cliente.setTelefone(clienteAtualizado.getTelefone());
			cliente.setUf(clienteAtualizado.getUf());
			cliente.setLocalizacao(clienteAtualizado.getLocalizacao());

			clienteRepository.save(cliente);
		}
		return "redirect:/listarClientes";
	}

	@GetMapping("/pesquisar")
	public ModelAndView pesquisarCliente(@RequestParam(required = false) String cnpj) {
		ModelAndView mv = new ModelAndView("listarClientes");
		List<Cliente> listarClientes;

		try {
			if (!StringUtils.hasText(cnpj)) {
				listarClientes = this.clienteRepository.findAll(Sort.by("cnpj"));
				logger.info("Pesquisando todos os clientes, total encontrado: {}", listarClientes.size());
			} else if (!isValidCNPJ(cnpj)) {
				listarClientes = Collections.emptyList();
				mv.addObject("error", "CNPJ inválido.");
				logger.warn("Tentativa de pesquisa com CNPJ inválido: {}", cnpj);
			} else {
				listarClientes = this.clienteRepository.findByCnpj(cnpj);
				if (listarClientes.isEmpty()) {
					mv.addObject("message", "Nenhum cliente encontrado com o CNPJ fornecido.");
					logger.info("Nenhum cliente encontrado com o CNPJ: {}", cnpj);
				} else {
					logger.info("Clientes encontrados com o CNPJ {}: {}", cnpj, listarClientes.size());
				}
			}
		} catch (DataAccessException e) {
			listarClientes = Collections.emptyList();
			mv.addObject("error", "Erro ao acessar o banco de dados.");
			logger.error("Erro ao acessar o banco de dados ao pesquisar por CNPJ: {}", cnpj, e);
		}

		mv.addObject("listarClientes", listarClientes);
		return mv;
	}

	private boolean isValidCNPJ(String cnpj) {

		return cnpj != null && cnpj.matches("\\d{14}");
	}

}
