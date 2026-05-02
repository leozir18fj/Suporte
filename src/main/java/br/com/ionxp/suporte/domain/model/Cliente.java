package br.com.ionxp.suporte.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cliente {
	
	@NotNull(groups = ValidationGroups.ClienteId.class)
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
	@NotNull(groups = ValidationGroups.ClienteId.class)
	@Enumerated(EnumType.STRING)
	private TipoCliente tipoCliente;
	@JsonProperty(access = Access.READ_ONLY)
	private LocalDateTime dataCadastro;
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Endereco> enderecos;
	
	public void adicionarEndereco(Endereco endereco) {
	    endereco.setCliente(this);
	    this.enderecos.add(endereco);
	}
	
	public String getTipoClienteDescricao() {
	    return tipoCliente != null ? tipoCliente.getDescricao() : null;
	}
	
}
