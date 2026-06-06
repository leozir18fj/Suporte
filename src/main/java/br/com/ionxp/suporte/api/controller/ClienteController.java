package br.com.ionxp.suporte.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ionxp.suporte.api.assembler.ClienteAssembler;
import br.com.ionxp.suporte.api.model.ClienteModel;
import br.com.ionxp.suporte.api.model.input.ClienteInputModel;
import br.com.ionxp.suporte.domain.exception.NegocioException;
import br.com.ionxp.suporte.domain.model.Cliente;
import br.com.ionxp.suporte.domain.model.enums.TipoCliente;
import br.com.ionxp.suporte.domain.repository.ClienteRepository;
import br.com.ionxp.suporte.domain.service.CadastroClienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private final ClienteRepository clienteRepository;
	private final CadastroClienteService cadastroClienteService;
	private final ClienteAssembler clienteAssembler;

	@GetMapping
	public List<ClienteModel> listar() {
		return clienteAssembler.toCollectionModel(clienteRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteModel> buscar(@PathVariable Long id) {
		return clienteRepository.findById(id)
				.map(clienteAssembler::toModel)
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteModel adicionar(@Valid @RequestBody ClienteInputModel clienteInputModel) throws NegocioException {
		Cliente novoCliente = clienteAssembler.toEntity(clienteInputModel);
		Cliente clienteCadastrado = cadastroClienteService.adicionar(novoCliente);
		return clienteAssembler.toModel(clienteCadastrado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente)
			throws NegocioException {

		if (!clienteRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		cliente.setId(id);
		Cliente clienteAtualizado = cadastroClienteService.atualizar(cliente);
		return ResponseEntity.ok(clienteAtualizado);
	}

	@GetMapping("/tipos-cliente")
	public List<Map<String, String>> listarTipos() {
		return Arrays.stream(TipoCliente.values()).map(tipo -> Map.of("label", tipo.getDescricao(), // bonito
				"value", tipo.name() // técnico
		)).toList();
	}

}
