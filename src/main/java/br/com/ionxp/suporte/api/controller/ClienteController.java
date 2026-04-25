package br.com.ionxp.suporte.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping
	public List<Cliente> listar() {
		return clienteRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
		return clienteRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) throws NegocioException {
		return cadastroClienteService.adicionar(cliente);
	}

	@GetMapping("/tipos-cliente")
	public List<Map<String, String>> listarTipos() {
		return Arrays.stream(TipoCliente.values()).map(tipo -> Map.of("label", tipo.getDescricao(), // bonito
				"value", tipo.name() // técnico
		)).toList();
	}

}
