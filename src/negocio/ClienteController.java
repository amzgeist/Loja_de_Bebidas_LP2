package negocio;

import dados.Cliente;
import excecoes.ClienteNaoEncontradoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteController {
    private List<Cliente> clientes;
    private static ClienteController instance;

    private ClienteController() {
        clientes = new ArrayList<>();
    }

    public static ClienteController getInstance() {
        if (instance == null) {
            instance = new ClienteController();
        }
        return instance;
    }

    public void cadastrarCliente(Scanner scanner) {
        System.out.print("Digite o nome do cliente: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o CPF do cliente: ");
        String cpf = scanner.nextLine();

        // Verificar se o cliente já existe
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                System.out.println("Cliente com CPF " + cpf + " já está cadastrado.");
                return;
            }
        }

        System.out.print("Digite a data de nascimento do cliente (DD/MM/AAAA): ");
        String dataNascimento = scanner.nextLine();

        Cliente cliente = new Cliente(nome, cpf, dataNascimento);
        clientes.add(cliente);
        System.out.println("Cliente cadastrado com sucesso.");
    }

    public void listarClientes() {
        System.out.println("----- Lista de Clientes -----");
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    public Cliente buscarCliente(String cpf) throws ClienteNaoEncontradoException {
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        throw new ClienteNaoEncontradoException("Cliente com CPF " + cpf + " não encontrado.");
    }

    // Método para editar cliente
    public void atualizarCliente(Scanner scanner) {
        System.out.print("Digite o CPF do cliente que deseja editar: ");
        String cpf = scanner.nextLine();

        try {
            Cliente cliente = buscarCliente(cpf);
            System.out.println("Cliente encontrado!");
            System.out.println("1. Alterar Nome");
            System.out.println("2. Alterar Data de Nascimento");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    System.out.print("Digite o novo nome: ");
                    cliente.setNome(scanner.nextLine());
                    System.out.println("Nome atualizado com sucesso.");
                    break;
                case 2:
                    System.out.print("Digite a nova data de nascimento (DD/MM/AAAA): ");
                    cliente.setDataNascimento(scanner.nextLine());
                    System.out.println("Data de nascimento atualizada com sucesso.");
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        } catch (ClienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }
}
