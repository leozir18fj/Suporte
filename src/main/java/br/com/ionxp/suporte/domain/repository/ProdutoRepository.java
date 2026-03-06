package br.com.ionxp.suporte.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ionxp.suporte.domain.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	
	List<Produto> findByDescricao(String nome);
	List<Produto> findByDescricaoContaining(String nome);

}
