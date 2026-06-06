package br.com.ionxp.suporte.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.ionxp.suporte.api.model.ClienteModel;
import br.com.ionxp.suporte.api.model.input.ClienteInputModel;
import br.com.ionxp.suporte.domain.model.Cliente;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ClienteAssembler {

	private final ModelMapper modelMapper;
	
	public Cliente toEntity(ClienteInputModel clienteInputModel) {
		return modelMapper.map(clienteInputModel, Cliente.class);
	}
	
	public ClienteModel toModel(Cliente cliente) {
		return modelMapper.map(cliente, ClienteModel.class);
	}
	
	public List<ClienteModel> toCollectionModel(List<Cliente> clientes){
		return clientes.stream().map(this::toModel).toList();
	}
}
