package br.com.ionxp.suporte.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.ionxp.suporte.domain.model.Cliente;
import br.com.ionxp.suporte.domain.model.enums.TipoCliente;
import br.com.ionxp.suporte.domain.repository.ClienteRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ClienteRepository clienteRepository;

	@BeforeEach
	void limparBanco() {
		clienteRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve listar clientes cadastrados")
	void deveListarClientes() throws Exception {
		Cliente cliente = novoCliente("Maria Silva", "12345678901", TipoCliente.CONTRIBUINTE);
		clienteRepository.save(cliente);

		mockMvc.perform(get("/clientes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].nome").value("Maria Silva"))
				.andExpect(jsonPath("$[0].cpfCnpj").value("12345678901"))
				.andExpect(jsonPath("$[0].tipoCliente").value("CONTRIBUINTE"));
	}

	@Test
	@DisplayName("Deve buscar cliente por id")
	void deveBuscarClientePorId() throws Exception {
		Cliente cliente = clienteRepository.save(novoCliente("Joao Santos", "12345678902", TipoCliente.ISENTO));

		mockMvc.perform(get("/clientes/{id}", cliente.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(cliente.getId()))
				.andExpect(jsonPath("$.nome").value("Joao Santos"))
				.andExpect(jsonPath("$.tipoCliente").value("ISENTO"));
	}

	@Test
	@DisplayName("Deve retornar 404 ao buscar cliente inexistente")
	void deveRetornarNotFoundAoBuscarClienteInexistente() throws Exception {
		mockMvc.perform(get("/clientes/{id}", 999L))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve adicionar cliente")
	void deveAdicionarCliente() throws Exception {
		String json = """
				{
				  "nome": "Empresa XPTO",
				  "cpfCnpj": "12345678000190",
				  "ieRg": "123456",
				  "email": "contato@xpto.com",
				  "celular": "11999999999",
				  "tipoCliente": "NAO_CONTRIBUINTE"
				}
				""";

		mockMvc.perform(post("/clientes")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.nome").value("Empresa XPTO"))
				.andExpect(jsonPath("$.cpfCnpj").value("12345678000190"))
				.andExpect(jsonPath("$.dataCadastro").exists());
	}

	@Test
	@DisplayName("Deve adicionar cliente com endereco")
	void deveAdicionarClienteComEndereco() throws Exception {
		String json = """
				{
				  "nome": "Cliente com Endereco",
				  "cpfCnpj": "12345678903",
				  "email": "cliente@email.com",
				  "celular": "11988888888",
				  "tipoCliente": "CONTRIBUINTE",
				  "enderecos": [
				    {
				      "rua": "Rua das Flores",
				      "cep": "01001000",
				      "numero": "100",
				      "bairro": "Centro",
				      "municipio": "Sao Paulo",
				      "estado": "SP"
				    }
				  ]
				}
				""";

		mockMvc.perform(post("/clientes")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.enderecos", hasSize(1)))
				.andExpect(jsonPath("$.enderecos[0].rua").value("Rua das Flores"))
				.andExpect(jsonPath("$.enderecos[0].cep").value("01001000"));
	}

	@Test
	@DisplayName("Deve retornar 400 ao adicionar cliente sem nome")
	void deveRetornarBadRequestAoAdicionarClienteSemNome() throws Exception {
		String json = """
				{
				  "cpfCnpj": "12345678904",
				  "tipoCliente": "CONTRIBUINTE"
				}
				""";

		mockMvc.perform(post("/clientes")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Deve retornar 400 ao adicionar CPF/CNPJ duplicado")
	void deveRetornarBadRequestAoAdicionarCpfCnpjDuplicado() throws Exception {
		clienteRepository.save(novoCliente("Cliente Existente", "12345678905", TipoCliente.CONTRIBUINTE));

		String json = """
				{
				  "nome": "Cliente Novo",
				  "cpfCnpj": "12345678905",
				  "tipoCliente": "ISENTO"
				}
				""";

		mockMvc.perform(post("/clientes")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Deve atualizar cliente")
	void deveAtualizarCliente() throws Exception {
		Cliente cliente = clienteRepository.save(novoCliente("Nome Antigo", "12345678906", TipoCliente.CONTRIBUINTE));

		String json = """
				{
				  "nome": "Nome Atualizado",
				  "cpfCnpj": "12345678906",
				  "ieRg": "987654",
				  "email": "novo@email.com",
				  "celular": "11977777777",
				  "tipoCliente": "ISENTO",
				  "enderecos": []
				}
				""";

		mockMvc.perform(put("/clientes/{id}", cliente.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(cliente.getId()))
				.andExpect(jsonPath("$.nome").value("Nome Atualizado"))
				.andExpect(jsonPath("$.email").value("novo@email.com"))
				.andExpect(jsonPath("$.tipoCliente").value("ISENTO"));
	}

	@Test
	@DisplayName("Deve retornar 404 ao atualizar cliente inexistente")
	void deveRetornarNotFoundAoAtualizarClienteInexistente() throws Exception {
		String json = """
				{
				  "nome": "Cliente Inexistente",
				  "cpfCnpj": "12345678907",
				  "tipoCliente": "CONTRIBUINTE",
				  "enderecos": []
				}
				""";

		mockMvc.perform(put("/clientes/{id}", 999L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve listar os tipos de cliente")
	void deveListarTiposCliente() throws Exception {
		mockMvc.perform(get("/clientes/tipos-cliente"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].value").value("CONTRIBUINTE"))
				.andExpect(jsonPath("$[1].value").value("NAO_CONTRIBUINTE"))
				.andExpect(jsonPath("$[2].value").value("ISENTO"));
	}

	private Cliente novoCliente(String nome, String cpfCnpj, TipoCliente tipoCliente) {
		Cliente cliente = new Cliente();
		cliente.setNome(nome);
		cliente.setCpfCnpj(cpfCnpj);
		cliente.setIeRg("123456");
		cliente.setEmail("cliente@email.com");
		cliente.setCelular("11999999999");
		cliente.setTipoCliente(tipoCliente);
		cliente.setEnderecos(new ArrayList<>());
		return cliente;
	}
}