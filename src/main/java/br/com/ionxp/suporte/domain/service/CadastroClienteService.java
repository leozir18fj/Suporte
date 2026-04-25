package br.com.ionxp.suporte.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ionxp.suporte.domain.model.Cliente;
import br.com.ionxp.suporte.domain.model.Endereco;
import br.com.ionxp.suporte.domain.repository.ClienteRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CadastroClienteService {

	private ClienteRepository clienteRepository;

	@Transactional
	public Cliente adicionar(Cliente cliente) {
		cliente.setDataCadastro(LocalDateTime.now());
		if (cliente.getEnderecos() != null) {

			List<Endereco> lista = new ArrayList<>(cliente.getEnderecos());
			cliente.setEnderecos(new ArrayList<>());

			lista.forEach(cliente::adicionarEndereco);
			//for (Endereco e : lista) {
		    //cliente.adicionarEndereco(e);
			//}
		}
		return clienteRepository.save(cliente);
	}

}
