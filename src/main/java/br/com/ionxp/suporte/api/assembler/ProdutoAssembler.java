package br.com.ionxp.suporte.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.ionxp.suporte.api.model.ProdutoModel;
import br.com.ionxp.suporte.api.model.input.ProdutoInputModel;
import br.com.ionxp.suporte.domain.model.Produto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ProdutoAssembler {

	private final ModelMapper modelMapper;
	
	public Produto toEntity(ProdutoInputModel produtoInputModel) {
		return modelMapper.map(produtoInputModel, Produto.class);
	}
	
	public ProdutoModel toModel(Produto produto) {
		return modelMapper.map(produto, ProdutoModel.class);
	}
	
	public List<ProdutoModel> toCollectionModel(List<Produto> produtos){
		return produtos.stream().map(this::toModel).toList();
	}
}
