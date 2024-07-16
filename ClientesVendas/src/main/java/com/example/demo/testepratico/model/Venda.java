package com.example.demo.testepratico.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
public class Venda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codigo;

	@OneToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;

	@NotNull(message = "Data é obrigatória!")
	private LocalDate data;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Status é obrigatório!")
	private StatusVenda status;

	@NotNull(message = "Valor é obrigatório!")
	@DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que zero!")
	private BigDecimal valor;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	// Método para obter o nome do cliente diretamente
	public String getNome() {
		return cliente != null ? cliente.getNome() : null;
	}

}
