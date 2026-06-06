package br.com.ionxp.suporte.api.model;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoModel {
	
	private Long id;
	private String descricao;
	private String codigoBarras;
	private Double preco;
	private Double qtdEstoque;
	private OffsetDateTime dataCadastro;

}
