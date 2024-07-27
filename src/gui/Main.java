package gui;

import negocio.ClienteController;
import negocio.FuncionarioController;
import dados.Cliente;
import dados.Funcionario;
import excecoes.FuncionarioInativoException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClienteController clienteController = ClienteController.getInstance();
        FuncionarioController funcionarioController = FuncionarioController.getInstance();

        while (true) {
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente");
            System.out.println("4. Cadastrar Funcionário");
            System.out.println("5. Listar Funcionários");
            System.out.println("6. Buscar Funcionário");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1 -> {
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Data de Nascimento (dd/MM/yyyy): ");
                    String dataStr = scanner.nextLine();
                    System.out.print("CPF: ");
                    String CPF = scanner.nextLine();

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dataNascimento = sdf.parse(dataStr);
                        clienteController.cadastrarCliente(nome, dataNascimento, CPF);
                    } catch (Exception e) {
                        System.out.println("Data inválida.");
                    }
                }
                case 2 -> clienteController.listarClientes();
                case 3 -> {
                    System.out.print("CPF: ");
                    String CPF = scanner.nextLine();
                    Cliente cliente = clienteController.buscarCliente(CPF);
                    if (cliente != null) {
                        System.out.println("Nome: " + cliente.getNome());
                        System.out.println("CPF: " + cliente.getCPF());
                        System.out.println("Data de Nascimento: " + cliente.getDataNascimentoFormatada());

                        // Menu para operações adicionais com cliente
                        boolean backToMainMenu = false;
                        while (!backToMainMenu) {
                            System.out.println("\n--- Cliente Menu ---");
                            System.out.println("1. Atualizar Cliente");
                            System.out.println("2. Deletar Cliente");
                            System.out.println("3. Voltar ao Menu Principal");
                            System.out.print("Escolha uma opção: ");
                            int clienteOption = scanner.nextInt();
                            scanner.nextLine(); // consume newline

                            switch (clienteOption) {
                                case 1 -> {
                                    System.out.print("Novo Nome: ");
                                    String novoNome = scanner.nextLine();
                                    System.out.print("Nova Data de Nascimento (dd/MM/yyyy): ");
                                    String novaDataStr = scanner.nextLine();

                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        Date novaDataNascimento = sdf.parse(novaDataStr);
                                        clienteController.atualizarCliente(CPF, novoNome, novaDataNascimento);
                                    } catch (Exception e) {
                                        System.out.println("Data inválida.");
                                    }
                                }
                                case 2 -> {
                                    clienteController.deletarCliente(CPF);
                                    backToMainMenu = true;
                                }
                                case 3 -> backToMainMenu = true;
                                default -> System.out.println("Opção inválida.");
                            }
                        }
                    }
                }
                case 4 -> {
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("CPF: ");
                    String CPF = scanner.nextLine();
                    System.out.print("Tipo: ");
                    String tipo = scanner.nextLine();
                    System.out.print("Salário: ");
                    double salario = scanner.nextDouble();
                    funcionarioController.cadastrarFuncionario(nome, CPF, tipo, salario);
                }
                case 5 -> funcionarioController.listarFuncionarios();
                case 6 -> {
                    System.out.print("Código do Funcionário: ");
                    int codFunc = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    try {
                        Funcionario funcionario = funcionarioController.buscarFuncionario(codFunc);
                        if (funcionario != null) {
                            System.out.println("Nome: " + funcionario.getNome());
                            System.out.println("Código: " + funcionario.getCodFunc());
                            System.out.println("CPF: " + funcionario.getCPF());
                            System.out.println("Tipo: " + funcionario.getTipo());
                            System.out.println("Salário: " + funcionario.getSalario());

                            // Menu para operações adicionais com funcionário
                            boolean backToMainMenu = false;
                            while (!backToMainMenu) {
                                System.out.println("\n--- Funcionário Menu ---");
                                System.out.println("1. Atualizar Funcionário");
                                System.out.println("2. Deletar Funcionário");
                                System.out.println("3. Voltar ao Menu Principal");
                                System.out.print("Escolha uma opção: ");
                                int funcOption = scanner.nextInt();
                                scanner.nextLine(); // consume newline

                                switch (funcOption) {
                                    case 1 -> {
                                        System.out.print("Novo Nome: ");
                                        String novoNome = scanner.nextLine();
                                        System.out.print("Novo CPF: ");
                                        String novoCPF = scanner.nextLine();
                                        System.out.print("Novo Salário: ");
                                        double novoSalario = scanner.nextDouble();
                                        scanner.nextLine(); // consume newline
                                        funcionarioController.alterarFuncionario(codFunc, novoNome, novoCPF, novoSalario);
                                    }
                                    case 2 -> {
                                        funcionarioController.deletarFuncionario(codFunc);
                                        backToMainMenu = true;
                                    }
                                    case 3 -> backToMainMenu = true;
                                    default -> System.out.println("Opção inválida.");
                                }
                            }
                        }
                    } catch (FuncionarioInativoException e) {
                        // Obtendo o nome do funcionário inativo
                        String errorMessage = e.getMessage();
                        String funcionarioNome = errorMessage.substring(errorMessage.indexOf("Funcionário ") + 12, errorMessage.indexOf(" está inativo"));

                        System.out.println("Funcionário " + funcionarioNome + " está inativo. Deseja reativá-lo?");
                        System.out.println("1. Sim");
                        System.out.println("2. Não, voltar ao menu principal");
                        int reativaOption = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        if (reativaOption == 1) {
                            funcionarioController.reativaFuncionario(codFunc);
                        }
                    }
                }
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        }
    }
}
