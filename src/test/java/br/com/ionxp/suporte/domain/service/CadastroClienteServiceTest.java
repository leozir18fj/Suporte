package br.com.ionxp.suporte.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ionxp.suporte.domain.exception.NegocioException;
import br.com.ionxp.suporte.domain.model.Cliente;
import br.com.ionxp.suporte.domain.model.Endereco;
import br.com.ionxp.suporte.domain.model.enums.TipoCliente;
import br.com.ionxp.suporte.domain.repository.ClienteRepository;

// @ExtendWith(MockitoExtension.class) — diz ao JUnit para usar o Mockito
// O Mockito vai criar os @Mock e injetar no @InjectMocks automaticamente
@ExtendWith(MockitoExtension.class)
class CadastroClienteServiceTest {

    // @Mock cria uma versão simulada do repositório
    // Nenhuma query real vai ao banco — controlamos o que ele retorna
    @Mock
    private ClienteRepository clienteRepository;

    // @InjectMocks cria o service real e injeta os @Mock acima
    @InjectMocks
    private CadastroClienteService cadastroClienteService;

    // Objetos reutilizados nos testes — criados antes de cada teste
    private Cliente clientePJ;
    private Cliente clientePF;
    private Endereco endereco;

    // @BeforeEach — executa antes de CADA teste para garantir estado limpo
    @BeforeEach
    void setUp() {
        // Cliente Pessoa Jurídica (CNPJ)
        clientePJ = new Cliente();
        clientePJ.setNome("RL Computec");
        clientePJ.setCpfCnpj("18820709000109");
        clientePJ.setTipoCliente(TipoCliente.CONTRIBUINTE);
        clientePJ.setEmail("contato@rlcomputec.com.br");
        clientePJ.setCelular("42999999999");
        clientePJ.setEnderecos(new ArrayList<>());

        // Cliente Pessoa Física (CPF)
        clientePF = new Cliente();
        clientePF.setNome("João Silva");
        clientePF.setCpfCnpj("12345678901");
        clientePF.setTipoCliente(TipoCliente.NAO_CONTRIBUINTE);
        clientePF.setEnderecos(new ArrayList<>());

        // Endereço para testes
        endereco = new Endereco();
        endereco.setRua("Rua Clara Barbosa da Costa");
        endereco.setNumero("315");
        endereco.setBairro("Centro");
        endereco.setCep("84550450");
        endereco.setMunicipio("Rebouças");
        endereco.setEstado("PR");
    }

    // @Nested — agrupa testes relacionados, deixa o relatório mais organizado
    @Nested
    @DisplayName("Testes de adicionar cliente")
    class AdicionarCliente {

        @Test
        @DisplayName("Deve salvar cliente PJ com sucesso")
        void deveSalvarClientePJComSucesso() throws NegocioException {
            // ARRANGE — configura o cenário
            // Simula que não existe nenhum cliente com esse CNPJ
            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.empty());

            // Simula o retorno do save com o cliente que foi passado
            when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> {
                    Cliente c = invocation.getArgument(0);
                    c.setId(1L); // simula o ID gerado pelo banco
                    return c;
                });

            // ACT — executa a ação que queremos testar
            Cliente salvo = cadastroClienteService.adicionar(clientePJ);

            // ASSERT — verifica se o resultado é o esperado
            assertNotNull(salvo);
            assertEquals("RL Computec", salvo.getNome());
            assertEquals("18820709000109", salvo.getCpfCnpj());
            assertEquals(TipoCliente.CONTRIBUINTE, salvo.getTipoCliente());
            assertNotNull(salvo.getDataCadastro()); // deve ter sido setada no service
            assertNotNull(salvo.getId());

            // Verifica que o save foi chamado exatamente 1 vez
            verify(clienteRepository, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve salvar cliente PF com sucesso")
        void deveSalvarClientePFComSucesso() throws NegocioException {
            when(clienteRepository.findByCpfCnpj("12345678901"))
                .thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(clientePF);

            Cliente salvo = cadastroClienteService.adicionar(clientePF);

            assertNotNull(salvo);
            assertEquals(TipoCliente.NAO_CONTRIBUINTE, salvo.getTipoCliente());
        }

        @Test
        @DisplayName("Deve salvar cliente com endereço corretamente")
        void deveSalvarClienteComEnderecoCorretamente() throws NegocioException {
            clientePJ.getEnderecos().add(endereco);

            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente salvo = cadastroClienteService.adicionar(clientePJ);

            // Verifica que o endereço foi vinculado ao cliente
            assertFalse(salvo.getEnderecos().isEmpty());
            assertEquals(1, salvo.getEnderecos().size());
            assertEquals(salvo, salvo.getEnderecos().get(0).getCliente());
        }

        @Test
        @DisplayName("Deve salvar cliente com múltiplos endereços")
        void deveSalvarClienteComMultiplosEnderecos() throws NegocioException {
            Endereco endereco2 = new Endereco();
            endereco2.setRua("Rua das Flores");
            endereco2.setNumero("100");
            endereco2.setBairro("Jardim");
            endereco2.setCep("84550000");
            endereco2.setMunicipio("Rebouças");
            endereco2.setEstado("PR");

            clientePJ.getEnderecos().add(endereco);
            clientePJ.getEnderecos().add(endereco2);

            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente salvo = cadastroClienteService.adicionar(clientePJ);

            assertEquals(2, salvo.getEnderecos().size());
            // todos os endereços devem apontar para o mesmo cliente
            salvo.getEnderecos().forEach(e -> assertEquals(salvo, e.getCliente()));
        }

        @Test
        @DisplayName("Deve lançar exceção quando CNPJ já está em uso")
        void deveLancarExcecaoQuandoCnpjJaEmUso() {
            // Simula que já existe um cliente com esse CNPJ no banco
            Cliente clienteExistente = new Cliente();
            clienteExistente.setId(99L); // id diferente = cliente diferente
            clienteExistente.setCpfCnpj("18820709000109");

            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.of(clienteExistente));

            // assertThrows verifica que a exceção é lançada
            NegocioException ex = assertThrows(NegocioException.class, () -> {
                cadastroClienteService.adicionar(clientePJ);
            });

            assertEquals("CPF/ CNPJ já cadastrado no sistema", ex.getMessage());

            // Garante que o save NUNCA foi chamado
            verify(clienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF já está em uso")
        void deveLancarExcecaoQuandoCpfJaEmUso() {
            Cliente clienteExistente = new Cliente();
            clienteExistente.setId(99L);
            clienteExistente.setCpfCnpj("12345678901");

            when(clienteRepository.findByCpfCnpj("12345678901"))
                .thenReturn(Optional.of(clienteExistente));

            assertThrows(NegocioException.class, () -> {
                cadastroClienteService.adicionar(clientePF);
            });

            verify(clienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente já possui ID")
        void deveLancarExcecaoQuandoClienteJaPossuiId() {
            // Simula tentativa de adicionar cliente que já tem ID
            clientePJ.setId(1L);

            NegocioException ex = assertThrows(NegocioException.class, () -> {
                cadastroClienteService.adicionar(clientePJ);
            });

            assertEquals("Cliente não deve possuir ID", ex.getMessage());
            verify(clienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve setar data de cadastro automaticamente")
        void deveSetarDataDeCadastroAutomaticamente() throws NegocioException {
            when(clienteRepository.findByCpfCnpj(any()))
                .thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

            LocalDateTime antes = LocalDateTime.now().minusSeconds(1);
            Cliente salvo = cadastroClienteService.adicionar(clientePJ);
            LocalDateTime depois = LocalDateTime.now().plusSeconds(1);

            // A data de cadastro deve estar entre o momento antes e depois do save
            assertNotNull(salvo.getDataCadastro());
            assertTrue(salvo.getDataCadastro().isAfter(antes));
            assertTrue(salvo.getDataCadastro().isBefore(depois));
        }
    }

    @Nested
    @DisplayName("Testes de atualizar cliente")
    class AtualizarCliente {

        @Test
        @DisplayName("Deve atualizar cliente com sucesso")
        void deveAtualizarClienteComSucesso() throws NegocioException {
            // Cliente existente no banco
            Cliente clienteExistente = new Cliente();
            clienteExistente.setId(1L);
            clienteExistente.setNome("Nome Antigo");
            clienteExistente.setCpfCnpj("18820709000109");
            clienteExistente.setDataCadastro(LocalDateTime.now().minusDays(10));
            clienteExistente.setEnderecos(new ArrayList<>());

            // Cliente com dados novos para atualizar
            clientePJ.setId(1L);
            clientePJ.setNome("Nome Novo");

            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.of(clienteExistente)); // mesmo id = não duplica

            when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(clienteExistente));

            when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente atualizado = cadastroClienteService.atualizar(clientePJ);

            assertNotNull(atualizado);
            // A data de cadastro deve ser preservada
            assertNotNull(atualizado.getDataCadastro());
            verify(clienteRepository, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve preservar data de cadastro ao atualizar")
        void devePreservarDataDeCadastroAoAtualizar() throws NegocioException {
            LocalDateTime dataOriginal = LocalDateTime.of(2024, 1, 15, 10, 0);

            Cliente clienteExistente = new Cliente();
            clienteExistente.setId(1L);
            clienteExistente.setCpfCnpj("18820709000109");
            clienteExistente.setDataCadastro(dataOriginal);
            clienteExistente.setEnderecos(new ArrayList<>());

            clientePJ.setId(1L);

            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.of(clienteExistente));
            when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(clienteExistente));
            when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente atualizado = cadastroClienteService.atualizar(clientePJ);

            // Data original deve ser mantida
            assertEquals(dataOriginal, atualizado.getDataCadastro());
        }

        @Test
        @DisplayName("Deve lançar exceção quando CNPJ já está em uso por outro cliente")
        void deveLancarExcecaoQuandoCnpjEmUsoPorOutroCliente() {
            // Cliente diferente com o mesmo CNPJ
            Cliente clienteDiferente = new Cliente();
            clienteDiferente.setId(99L); // id diferente!
            clienteDiferente.setCpfCnpj("18820709000109");

            clientePJ.setId(1L);

            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.of(clienteDiferente));

            assertThrows(NegocioException.class, () -> {
                cadastroClienteService.atualizar(clientePJ);
            });

            verify(clienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente não encontrado")
        void deveLancarExcecaoQuandoClienteNaoEncontrado() {
            clientePJ.setId(999L);

            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.empty());
            when(clienteRepository.findById(999L))
                .thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> {
                cadastroClienteService.atualizar(clientePJ);
            });
        }

        @Test
        @DisplayName("Deve atualizar endereços do cliente corretamente")
        void deveAtualizarEnderecosDoClienteCorretamente() throws NegocioException {
            Cliente clienteExistente = new Cliente();
            clienteExistente.setId(1L);
            clienteExistente.setCpfCnpj("18820709000109");
            clienteExistente.setDataCadastro(LocalDateTime.now());
            clienteExistente.setEnderecos(new ArrayList<>());

            clientePJ.setId(1L);
            clientePJ.getEnderecos().add(endereco);

            when(clienteRepository.findByCpfCnpj("18820709000109"))
                .thenReturn(Optional.of(clienteExistente));
            when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(clienteExistente));
            when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

            Cliente atualizado = cadastroClienteService.atualizar(clientePJ);

            assertEquals(1, atualizado.getEnderecos().size());
        }
    }
}
