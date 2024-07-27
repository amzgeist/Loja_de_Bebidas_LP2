package negocio;

import dados.Cliente;
import excecoes.ClienteNaoEncontradoException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClienteController {
    private static ClienteController instance;
    private final ArrayList<Cliente> clientes;

    private ClienteController() {
        clientes = new ArrayList<>();
    }

    public static ClienteController getInstance() {
        if (instance == null) {
            instance = new ClienteController();
        }
        return instance;
    }

    public void cadastrarCliente(String nome, Date dataNascimento, String CPF) {
        for (Cliente cliente : clientes) {
            if (cliente.getCPF().equals(CPF)) {
                System.out.println("CPF já cadastrado.");
                return;
            }
        }
        clientes.add(new Cliente(nome, CPF, dataNascimento));
        System.out.println("Cliente cadastrado com sucesso.");
    }

    public void atualizarCliente(String CPF, String nome, Date dataNascimento) {
        for (Cliente cliente : clientes) {
            if (cliente.getCPF().equals(CPF)) {
                cliente.setNome(nome);
                cliente.setDataNascimento(dataNascimento);
                System.out.println("Cliente atualizado com sucesso.");
                return;
            }
        }
        throw new ClienteNaoEncontradoException("Cliente não encontrado.");
    }

    public void deletarCliente(String CPF) {
        for (Cliente cliente : clientes) {
            if (cliente.getCPF().equals(CPF)) {
                clientes.remove(cliente);
                System.out.println("Cliente removido com sucesso.");
                return;
            }
        }
        throw new ClienteNaoEncontradoException("Cliente não encontrado.");
    }

    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("NENHUM CLIENTE CADASTRADO");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println("Nome: " + cliente.getNome());
            System.out.println("CPF: " + cliente.getCPF());
            System.out.println("Data de Nascimento: " + cliente.getDataNascimentoFormatada());
            System.out.println();
        }
    }

    public Cliente buscarCliente(String CPF) {
        for (Cliente cliente : clientes) {
            if (cliente.getCPF().equals(CPF)) {
                return cliente;
            }
        }
        throw new ClienteNaoEncontradoException("CPF não encontrado.");
    }
}
