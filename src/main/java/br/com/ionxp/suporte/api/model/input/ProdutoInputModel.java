package br.com.ionxp.suporte.api.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoInputModel {
	
	@NotBlank
	@Size(max = 60)
	private String descricao;
	private String codigoBarras;
	private Double preco;
	private Double qtdEstoque;

}
