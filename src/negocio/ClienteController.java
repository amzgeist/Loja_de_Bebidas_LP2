package negocio;

import dados.Cliente;
import excecoes.ClienteNaoEncontradoException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ClienteController {
    private final List<Cliente> clientes;
    private static ClienteController instance;

    private ClienteController() {
        clientes = new ArrayList<>();
        inicializarClientes();

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

        System.out.print("Digite a data de nascimento (DD/MM/AAAA): ");
        String dataStr = scanner.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            Date dataNascimento = sdf.parse(dataStr);
            Cliente cliente = new Cliente(nome, cpf, dataNascimento);
            clientes.add(cliente);
            System.out.println("Cliente cadastrado com sucesso.");
        } catch (ParseException e) {
            System.out.println("Erro de formato de data. Por favor, digite a data no formato DD/MM/AAAA.");
        }
    }

    public void listarClientes() {
        System.out.println("----- Lista de Clientes -----");
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    public Cliente buscarCliente(String CPF) throws ClienteNaoEncontradoException {
        for (Cliente cliente : this.clientes) {
            if (cliente.getCpf().equals(CPF)) {
                exibirDetalhesCliente(cliente);
                return cliente;
            }
        }
        throw new ClienteNaoEncontradoException("Cliente com CPF " + CPF + " não encontrado.");
    }

    private void exibirDetalhesCliente(Cliente cliente) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Detalhes do Cliente:");
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("CPF: " + cliente.getCpf());
        if (cliente.getDataNascimento() != null) {
            System.out.println("Data de Nascimento: " + sdf.format(cliente.getDataNascimento()));
        } else {
            System.out.println("Data de Nascimento: Não disponível");
        }
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
                    String dataString = scanner.nextLine();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    sdf.setLenient(false);  // Isso garante que as datas devem ser estritamente conforme o formato especificado
                    try {
                        Date novaDataNascimento = sdf.parse(dataString);
                        cliente.setDataNascimento(novaDataNascimento);
                        System.out.println("Data de nascimento atualizada com sucesso.");
                    } catch (ParseException e) {
                        System.out.println("Formato de data inválido. Por favor, use o formato DD/MM/AAAA.");
                    }
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

    private void inicializarClientes() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            clientes.add(new Cliente("Antonio Dias", "11122233344", sdf.parse("02/04/2000")));
            clientes.add(new Cliente("Patricia Gomes", "22233344455", sdf.parse("15/07/1998")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
