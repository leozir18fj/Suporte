package br.com.ionxp.suporte.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Produto {
	
	private Long id;
	private String descricao;
	private String codigoBarras;
	private Double preco;
	private Double qtdEstoque;

}
