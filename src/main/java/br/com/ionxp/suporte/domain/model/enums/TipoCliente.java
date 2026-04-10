package br.com.ionxp.suporte.domain.model.enums;

import lombok.Getter;

@Getter
public enum TipoCliente {
	
	CONTRIBUINTE("Contribuinte"),
    NAO_CONTRIBUINTE("Não Contribuinte"),
    ISENTO("Isento");

    private String descricao;

    TipoCliente(String descricao) {
        this.descricao = descricao;
    }

}
