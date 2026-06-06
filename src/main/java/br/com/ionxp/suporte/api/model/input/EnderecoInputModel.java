package br.com.ionxp.suporte.api.model.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoInputModel {
	private String rua;
	private String cep;
	private String numero;
	private String complemento;
	private String bairro;
	private String municipio;
	private String estado;
}
