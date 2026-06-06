package br.com.ionxp.suporte.api.model.input;

import java.util.List;

import br.com.ionxp.suporte.domain.model.enums.TipoCliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteInputModel {
	
	@NotBlank
	private String nome;
	private String cpfCnpj;
	private String ieRg;
	private String email;
	private String celular;
	@NotNull
	private TipoCliente tipoCliente;
	private List<EnderecoInputModel> enderecos;

}
