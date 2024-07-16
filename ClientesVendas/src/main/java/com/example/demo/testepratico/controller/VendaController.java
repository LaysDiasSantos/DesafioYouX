package com.example.demo.testepratico.controller;

import com.example.demo.testepratico.model.Cliente;
import com.example.demo.testepratico.model.StatusVenda;
import com.example.demo.testepratico.model.Venda;
import com.example.demo.testepratico.repository.ClienteRepository;
import com.example.demo.testepratico.service.ClienteService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.example.demo.testepratico.repository.VendaRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VendaController {

	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteService clienteService;

	private static final Logger logger = LoggerFactory.getLogger(VendaController.class);

	@GetMapping("/criarVenda")
	public String exibirFormCriarVenda(Model model) {
		model.addAttribute("venda", new Venda());
		model.addAttribute("cliente", clienteService.listarClientes());
		model.addAttribute("status", StatusVenda.values());

		return "criarVenda";
	}

	@PostMapping("/criarVenda")
	public String criarVenda(@Valid @ModelAttribute Venda venda, BindingResult result, Model model) {

		if (venda.getCliente() == null || venda.getCliente().getCodigo() == null) {
			result.rejectValue("cliente", "error.venda", "Cliente não pode ser nulo");
		} else {

			Optional<Cliente> clienteOpt = clienteRepository.findById(venda.getCliente().getCodigo());

			if (clienteOpt.isEmpty()) {
				result.rejectValue("cliente", "error.venda", "Cliente não encontrado");
			} else {

				venda.setCliente(clienteOpt.get());
			}
		}

		if (result.hasErrors()) {
			model.addAttribute("venda", venda);
			model.addAttribute("cliente", clienteRepository.findAll(Sort.by("nome")));
			model.addAttribute("status", StatusVenda.values());
			return "criarVenda";
		}

		vendaRepository.save(venda);

		return "redirect:/listarVendas";
	}

	@GetMapping("/listarVendas")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("listarVendas");
		mv.addObject("listarVendas", vendaRepository.findAll());
		return mv;
	}

	@GetMapping("/editarVenda/{codigo}")
	public String mostrarFormEdicao(@PathVariable("codigo") Integer codigo, Model model) {
		Optional<Venda> vendaOpt = vendaRepository.findById(codigo);

		if (vendaOpt.isPresent()) {
			Venda venda = vendaOpt.get();
			model.addAttribute("venda", venda);
			model.addAttribute("cliente", clienteRepository.findAll(Sort.by("nome")));
			model.addAttribute("status", StatusVenda.values());
			return "editarVenda";
		} else {
			return "redirect:/listarVendas";
		}
	}

	@GetMapping("/exibirVendas")
	public ModelAndView exibirVendas(@RequestParam Integer codigo) {
		ModelAndView mv = new ModelAndView("editarVenda");// mudei isso aqui
		mv.addObject("venda", vendaRepository.findById(codigo).orElse(null));
		mv.addObject("cliente", clienteRepository.findAll(Sort.by("nome")));
		mv.addObject("status", StatusVenda.values());
		return mv;
	}

	@PostMapping("/editarVenda/{codigo}")
	public String editarVenda(@PathVariable("codigo") Integer codigo, @ModelAttribute Venda vendaAtualizada,
			@RequestParam("status") String status, @RequestParam("action") String action) {

		if ("cancel".equals(action)) {
			return "redirect:/listarVendas";
		}

		Optional<Venda> vendaOpt = vendaRepository.findById(codigo);

		if (vendaOpt.isPresent()) {
			Venda venda = vendaOpt.get();
			// venda.setCliente(vendaAtualizada.getCliente());
			venda.setData(vendaAtualizada.getData());
			venda.setStatus(StatusVenda.valueOf(status));
			venda.setValor(vendaAtualizada.getValor());
			vendaRepository.save(venda);
		}

		return "redirect:/listarVendas";
	}

	@GetMapping("/excluirVenda")
	public ModelAndView excluirVenda(@RequestParam Integer codigo) {
		vendaRepository.deleteById(codigo);
		return listar();
	}

	@GetMapping("/pesquisarVenda")
	public ModelAndView pesquisarVenda(@RequestParam(required = false) String nome) {
		ModelAndView mv = new ModelAndView("listarVendas");
		List<Venda> listarVendas;

		try {
			if (!StringUtils.hasText(nome)) {
				listarVendas = this.vendaRepository.findAll(Sort.by("cliente.nome"));
				logger.info("Pesquisando todas as vendas, total encontrado: {}", listarVendas.size());
			} else {
				listarVendas = this.vendaRepository.findByCliente_Nome(nome);
				if (listarVendas.isEmpty()) {
					mv.addObject("message", "Nenhuma venda encontrada com o nome do cliente fornecido.");
					logger.info("Nenhuma venda encontrada com o nome do cliente: {}", nome);
				} else {
					logger.info("Vendas encontradas com o nome do cliente {}: {}", nome, listarVendas.size());
				}
			}
		} catch (DataAccessException e) {
			listarVendas = Collections.emptyList();
			mv.addObject("error", "Erro ao acessar o banco de dados.");
			logger.error("Erro ao acessar o banco de dados ao pesquisar por nome do cliente: {}", nome, e);
		}

		mv.addObject("listarVendas", listarVendas);
		return mv;
	}

	@GetMapping("/exportarCSV")
	public void exportarCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"relatorio_vendas.csv\"");

		PrintWriter writer = response.getWriter();
		writer.write("Cliente,Data,Valor\n");

		List<Venda> vendas = vendaRepository.findAll();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		for (Venda venda : vendas) {
			writer.write(String.format("%s,%s,%.2f\n", venda.getCliente().getNome(), venda.getData().format(formatter),
					venda.getValor()));

		}

		writer.flush();
		writer.close();
	}

}
