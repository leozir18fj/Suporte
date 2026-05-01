package br.com.ionxp.suporte.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ionxp.suporte.domain.exception.NegocioException;
import br.com.ionxp.suporte.domain.model.Cliente;
import br.com.ionxp.suporte.domain.model.Endereco;
import br.com.ionxp.suporte.domain.repository.ClienteRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CadastroClienteService {

	private ClienteRepository clienteRepository;

	@Transactional
	public Cliente adicionar(Cliente cliente) throws NegocioException {
		if (cliente.getId() != null) {
			throw new NegocioException("Cliente não deve possuir ID");
		}
		boolean cpfCnpjEmUso = clienteRepository.findByCpfCnpj(cliente.getCpfCnpj())
				.filter(c -> !c.equals(cliente)).isPresent();
		if (cpfCnpjEmUso) {
			throw new NegocioException("CPF/ CNPJ já cadastrado no sistema");
		}
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
	
	public Cliente atualizar(Cliente cliente) {
		if (cliente.getEnderecos() != null) {

			List<Endereco> lista = new ArrayList<>(cliente.getEnderecos());
			cliente.setEnderecos(new ArrayList<>());

			lista.forEach(cliente::adicionarEndereco);
		}
		Cliente clienteExistente = clienteRepository.findById(cliente.getId())
			    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

			cliente.setDataCadastro(clienteExistente.getDataCadastro());

		return clienteRepository.save(cliente);
	}

}
