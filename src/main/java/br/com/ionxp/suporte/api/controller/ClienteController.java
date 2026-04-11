package br.com.ionxp.suporte.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ionxp.suporte.domain.model.Cliente;
import br.com.ionxp.suporte.domain.repository.ClienteRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    
    @GetMapping
	public List<Cliente> listar(){
		return clienteRepository.findAll();
	}

    @GetMapping("/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
		return clienteRepository.findById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
    }

}
