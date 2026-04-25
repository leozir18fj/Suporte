package br.com.ionxp.suporte.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.ionxp.suporte.domain.model.enums.TipoCliente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cliente {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nome;
	private String cpfCnpj;
	private String ieRg;
	private String email;
	private String celular;
	@Enumerated(EnumType.STRING)
	private TipoCliente tipoCliente;
	private LocalDateTime dataCadastro;
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Endereco> enderecos;
	
	public void adicionarEndereco(Endereco endereco) {
	    endereco.setCliente(this);
	    this.enderecos.add(endereco);
	}
	
}
