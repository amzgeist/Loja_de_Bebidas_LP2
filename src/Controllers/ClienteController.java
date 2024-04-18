package Controllers;

import Entities.Cliente;

import java.util.ArrayList;
import java.util.Date;

public class ClienteController {
    private ArrayList<Cliente> clientes = new ArrayList<>();

    public void cadastrarCliente(String nome, Date dataNascimento, String CPF) {
        for (Cliente cliente : clientes) {
            if (cliente.getCPF().equals(CPF)) {
                System.out.println("CPF já cadastrado.");
                return;
            }
        }
        clientes.add(new Cliente(nome, dataNascimento, CPF));
        System.out.println("Cliente cadastrado com sucesso.");
    }

    public void atualizarCliente(String CPF, String nome, Date dataNascimento) {
        for (Cliente cliente : clientes) {
            if (cliente.getCPF().equals(CPF)) {
                cliente.nome = nome;
                cliente.dataNascimento = dataNascimento;
                System.out.println("Cliente atualizado com sucesso.");
                return;
            }
        }
        System.out.println("Cliente não encontrado.");
    }

    public void deletarCliente(String CPF) {
        for (Cliente cliente : clientes) {
            if (cliente.getCPF().equals(CPF)) {
                clientes.remove(cliente);
                System.out.println("Cliente removido com sucesso.");
                return;
            }
        }
        System.out.println("Cliente não encontrado.");
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
        System.out.println("CPF não encontrado.");
        return null;
    }
}
