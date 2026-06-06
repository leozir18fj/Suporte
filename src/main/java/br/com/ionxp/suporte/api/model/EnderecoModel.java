package br.com.ionxp.suporte.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoModel {
	private Long id;
	private String rua;
	private String cep;
	private String numero;
	private String complemento;
	private String bairro;
	private String municipio;
	private String estado;
}
