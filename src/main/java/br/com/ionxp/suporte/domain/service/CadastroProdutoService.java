package br.com.ionxp.suporte.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ionxp.suporte.domain.exception.NegocioException;
import br.com.ionxp.suporte.domain.model.Produto;
import br.com.ionxp.suporte.domain.repository.ProdutoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CadastroProdutoService {
	
	private final ProdutoRepository produtoRepository;
	
	@Transactional
	public Produto adicionar(Produto produto) throws NegocioException {
		boolean codigoBarrasEmUso = produtoRepository.
				findByCodigoBarras(produto.getCodigoBarras())
				.filter(p -> !p.equals(produto))
				.isPresent();
		if (codigoBarrasEmUso) {
			throw new NegocioException("Código de barras já utilizado em outro produto");
		}
		return produtoRepository.save(produto);
	}
	
	@Transactional
	public void excluir(Long produtoId) {
		produtoRepository.deleteById(produtoId);
	}

}
