package br.com.ionxp.suporte.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.ionxp.suporte.api.model.ClienteModel;
import br.com.ionxp.suporte.domain.model.Cliente;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// STRICT evita que o ModelMapper tente adivinhar mapeamentos ambíguos
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		modelMapper.typeMap(Cliente.class, ClienteModel.class).addMappings(mapper -> {
			mapper.skip(ClienteModel::setTipoCliente);
			mapper.skip(ClienteModel::setTipoClienteDescricao);
		}).setPostConverter(context -> {
			Cliente source = context.getSource();
			ClienteModel dest = context.getDestination();
			if (source.getTipoCliente() != null) {
				dest.setTipoCliente(source.getTipoCliente().name());
				dest.setTipoClienteDescricao(source.getTipoCliente().getDescricao());
			}
			return dest;
		});

		return modelMapper;
	}
}
