package br.com.ionxp.suporte.api.model;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteModel {

	private Long id;
	private String nome;
	private String cpfCnpj;
	private String ieRg;
	private String email;
	private String celular;
	private String tipoCliente;
	private String tipoClienteDescricao;
	private OffsetDateTime dataCadastro;
	private List<EnderecoModel> enderecos;

}
