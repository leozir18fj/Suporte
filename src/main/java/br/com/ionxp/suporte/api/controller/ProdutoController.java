package br.com.ionxp.suporte.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ionxp.suporte.domain.exception.NegocioException;
import br.com.ionxp.suporte.domain.model.Produto;
import br.com.ionxp.suporte.domain.repository.ProdutoRepository;
import br.com.ionxp.suporte.domain.service.CadastroProdutoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	
	private final ProdutoRepository produtoRepository;
	private final CadastroProdutoService cadastroProdutoService;
	
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
	@ResponseStatus(HttpStatus.CREATED)
	public Produto adicionar(@Valid @RequestBody Produto produto) throws NegocioException {
		return cadastroProdutoService.adicionar(produto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizar(@Valid @PathVariable Long id, @RequestBody Produto produto) throws NegocioException {
		
		if (!produtoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		produto.setId(id);
		Produto produtoAtualizado = cadastroProdutoService.adicionar(produto);
		return ResponseEntity.ok(produtoAtualizado);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		if (!produtoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		cadastroProdutoService.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<String> capturar(NegocioException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
