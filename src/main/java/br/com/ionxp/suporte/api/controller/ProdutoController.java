package br.com.ionxp.suporte.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ionxp.suporte.domain.model.Produto;
import br.com.ionxp.suporte.domain.repository.ProdutoRepository;

@RestController
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping("/produtos")
	public List<Produto> listar(){
		
		return produtoRepository.findAll();
		
		
	}

}
