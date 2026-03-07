package br.com.ionxp.suporte.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ionxp.suporte.domain.model.Produto;
import br.com.ionxp.suporte.domain.repository.ProdutoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	
	private final ProdutoRepository produtoRepository;
	
	@GetMapping
	public List<Produto> listar(){
		
		return produtoRepository.findAll();
				
	}
	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscar(@PathVariable Long id) {
		return produtoRepository.findById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
		
//		if (optional.isPresent()) {
//			return ResponseEntity.ok(optional.get());
//		}
//		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public Produto adicionar(@RequestBody Produto produto) {
		return produtoRepository.save(produto);
	}

}
