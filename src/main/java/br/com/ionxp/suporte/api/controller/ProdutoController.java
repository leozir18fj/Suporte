package br.com.ionxp.suporte.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ionxp.suporte.domain.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@RestController
public class ProdutoController {
	
	@PersistenceContext
	private EntityManager manager;
	
	@GetMapping("/produtos")
	public List<Produto> listar(){
		
		TypedQuery<Produto> query = manager.createQuery("from Produto", Produto.class);
		return query.getResultList();
		
		
	}

}
