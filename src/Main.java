import Controllers.ClienteController;
import Controllers.FuncionarioController;
import Entities.Cliente;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClienteController clienteController = new ClienteController();
        FuncionarioController funcionarioController = new FuncionarioController();

        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("0. Sair");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente");
            System.out.println("4. Cadastrar Funcionario");
            System.out.println("5. Desativar Funcionario");
            System.out.println("6. Buscar Funcionario");
            System.out.println("7. Listar Funcionario");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 0:
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;
                case 1:
                    System.out.println("Digite o nome do cliente:");
                    String nome = scanner.nextLine();
                    System.out.println("Digite a data de nascimento (no formato dd/MM/yyyy):");
                    String dataNascimentoStr = scanner.nextLine();
                    System.out.println("Digite o CPF:");
                    String CPF = scanner.nextLine();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dataNascimento = sdf.parse(dataNascimentoStr);
                        clienteController.cadastrarCliente(nome, dataNascimento, CPF);
                    } catch (Exception e) {
                        System.out.println("Dados inválidos.");
                    }
                    break;
                case 2:
                    clienteController.listarClientes();
                    break;
                case 3:
                    System.out.println("Digite o CPF do cliente que deseja buscar:");
                    String CPFBusca = scanner.nextLine();
                    Cliente cliente = clienteController.buscarCliente(CPFBusca);
                    if (cliente != null) {
                        System.out.println("Nome: " + cliente.getNome());
                        System.out.println("CPF: " + cliente.getCPF());
                        System.out.println("Data de Nascimento: " + cliente.getDataNascimentoFormatada());
                        System.out.println("1. Deletar Cliente");
                        System.out.println("2. Atualizar Cliente");
                        System.out.println("3. Voltar ao menu");
                        int opcaoCliente = scanner.nextInt();
                        scanner.nextLine();
                        switch (opcaoCliente) {
                            case 1:
                                clienteController.deletarCliente(CPFBusca);
                                break;
                            case 2:
                                System.out.println("Digite o novo nome do cliente:");
                                String novoNome = scanner.nextLine();
                                System.out.println("Digite a nova data de nascimento (no formato dd/MM/yyyy):");
                                String novaDataNascimentoStr = scanner.nextLine();
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date novaDataNascimento = sdf.parse(novaDataNascimentoStr);
                                    clienteController.atualizarCliente(CPFBusca, novoNome, novaDataNascimento);
                                } catch (Exception e) {
                                    System.out.println("Dados inválidos.");
                                }
                                break;
                            case 3:
                                break;
                            default:
                                System.out.println("Opção inválida.");
                        }
                    }
                    break;
                case 4:
                    System.out.println("Digite o nome do funcionário:");
                    String nomef = scanner.nextLine();
                    System.out.println("Digite o CPF do funcionário:");
                    String CPFf = scanner.nextLine();
                    System.out.println("Digite o tipo do funcionário (assalariado ou comissionado):");
                    String tipo = scanner.nextLine().toLowerCase();
                    while (!tipo.equals("assalariado") && !tipo.equals("comissionado")) {
                        System.out.println("Tipo inválido. Digite novamente (assalariado ou comissionado):");
                        tipo = scanner.nextLine().toLowerCase();
                    }
                    System.out.println("Digite o salário do funcionário:");
                    double salario = scanner.nextDouble();
                    funcionarioController.cadastrarFuncionario(nomef, CPFf, tipo, salario);
                    break;
                case 5:
                    System.out.println("Digite o código do funcionário que deseja deletar:");
                    int codFuncDelete = scanner.nextInt();
                    scanner.nextLine();
                    funcionarioController.deletarFuncionario(codFuncDelete);
                    break;
                case 6:
                    System.out.println("Digite o código do funcionário que deseja buscar:");
                    int codFuncSearch = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        funcionarioController.buscarFuncionario(codFuncSearch);
                        System.out.println("Escolha uma opção:");
                        System.out.println("1. Deletar Funcionário");
                        System.out.println("2. Alterar Funcionário");
                        System.out.println("3. Voltar");
                        int opcaoFuncionario = scanner.nextInt();
                        scanner.nextLine();
                        switch (opcaoFuncionario) {
                            case 1:
                                funcionarioController.deletarFuncionario(codFuncSearch);
                                break;
                            case 2:
                                System.out.println("Digite o novo nome do funcionário:");
                                String novoNome = scanner.nextLine();
                                System.out.println("Digite o novo CPF do funcionário:");
                                String novoCPF = scanner.nextLine();
                                System.out.println("Digite o novo salário do funcionário:");
                                double novoSalario = scanner.nextDouble();
                                funcionarioController.alterarFuncionario(codFuncSearch, novoNome, novoCPF, novoSalario);
                                break;
                            case 3:
                                break;
                            default:
                                System.out.println("Opção inválida.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        if (e.getMessage().equals("Funcionário inativo")) {
                            System.out.println("Escolha uma opção:");
                            System.out.println("1. Reativar Funcionário");
                            System.out.println("2. Voltar");
                            int opcaoReativar = scanner.nextInt();
                            scanner.nextLine();
                            switch (opcaoReativar) {
                                case 1:
                                    funcionarioController.reativaFuncionario(codFuncSearch);
                                    break;
                                case 2:
                                    break;
                                default:
                                    System.out.println("Opção inválida.");
                            }
                        }
                    }
                    break;
                case 7:
                    funcionarioController.listarFuncionarios();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}