package com.example.demo.testepratico.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codigo;

	@NotBlank(message = "Nome é obrigatório!")
	private String nome;

	@Pattern(regexp = "\\d{18}", message = "CNPJ deve ter 14 dígitos com ponto e traço!")
	@Column(unique = true)
	@Size(min = 18, max = 18)
	@NotBlank(message = "CNPJ é obrigatório!")
	private String cnpj;

	@NotBlank(message = "Email é obrigatório!")
	@Email(message = "Email deve ser válido!")
	private String email;

	@Size(min = 14, max = 14)
	@NotBlank(message = "Telefone é obrigatório!")
	private String telefone;

	@NotBlank(message = "UF é obrigatório!")
	@Size(min = 2, max = 2, message = "UF deve ter 2 caracteres!")
	@Pattern(regexp = "^[A-Z]{2}$", message = "UF deve ser composto por 2 letras maiúsculas")
	private String uf;

	@NotBlank(message = "Localização é obrigatória!")
	@Pattern(regexp = "-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?", message = "Localização deve estar no formato 'latitude,longitude'")
	private String localizacao;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	@Override
	public String toString() {
		return "Eventos [codigo=" + codigo + ",nome=" + nome + ",cnpj=" + cnpj + ",email=" + email + ",telefone="
				+ telefone + "uf=" + uf + ",localizacao" + localizacao + "]";

	}

}
