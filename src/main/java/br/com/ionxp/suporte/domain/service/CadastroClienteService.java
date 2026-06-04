package br.com.ionxp.suporte.domain.service;

import java.time.OffsetDateTime;
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
		if (verificaCpfCnpjEmUso(cliente)) {
			throw new NegocioException("CPF/ CNPJ já cadastrado no sistema");
		}
		cliente.setDataCadastro(OffsetDateTime.now());
		if (cliente.getEnderecos() != null) {

			List<Endereco> lista = new ArrayList<>(cliente.getEnderecos());
			cliente.setEnderecos(new ArrayList<>());

			lista.forEach(cliente::adicionarEndereco);
			// for (Endereco e : lista) {
			// cliente.adicionarEndereco(e);
			// }
		}
		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente atualizar(Cliente cliente) throws NegocioException {
		if (verificaCpfCnpjEmUso(cliente)) {
			throw new NegocioException("CPF/ CNPJ já cadastrado no sistema");
		}

		Cliente clienteExistente = clienteRepository.findById(cliente.getId())
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

		clienteExistente.setNome(cliente.getNome());
		clienteExistente.setIeRg(cliente.getIeRg());
		clienteExistente.setEmail(cliente.getEmail());
		clienteExistente.setCelular(cliente.getCelular());
		clienteExistente.setTipoCliente(cliente.getTipoCliente());
		clienteExistente.setDataCadastro(clienteExistente.getDataCadastro());

		// limpa endereços antigos
		clienteExistente.getEnderecos().clear();
		//adiciona os novos corretamente
		if (cliente.getEnderecos() != null) {

			for (Endereco e : cliente.getEnderecos()) {
				clienteExistente.adicionarEndereco(e);
			}
		}

		return clienteRepository.save(clienteExistente);
	}

	private boolean verificaCpfCnpjEmUso(Cliente cliente) {
		return clienteRepository.findByCpfCnpj(cliente.getCpfCnpj()).filter(c -> !c.equals(cliente)).isPresent();
	}

}
