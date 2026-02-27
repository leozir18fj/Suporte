package br.com.ionxp.suporte.api.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ionxp.suporte.domain.model.Produto;

@RestController
public class ProdutoController {
	
	@GetMapping("/produtos")
	public List<Produto> listar(){
		Produto produto = new Produto();
		produto.setId(1L);
		produto.setDescricao("Monitor");
		produto.setCodigoBarras("12345678910");
		produto.setPreco(10.20);
		
		Produto produto2 = new Produto();
		produto2.setId(2L);
		produto2.setDescricao("Teclado");
		produto2.setCodigoBarras("12345678910");
		produto2.setPreco(10.20);
		produto2.setQtdEstoque(10.0);
		
		return Arrays.asList(produto, produto2);
	}

}
