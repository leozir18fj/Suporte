package br.com.ionxp.suporte.domain.model.enums;

import lombok.Getter;

@Getter
public enum TipoEndereco {
	PRINCIPAL("Principal"),
	CASA("Casa"),
	TRABALHO("Trabalho"),
	ENTREGA("Entrega");
	
	private String descricao;
	
	private TipoEndereco(String descricao) {
		this.descricao = descricao;
	}
}
