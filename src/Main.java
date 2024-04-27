import Controllers.ClienteController;
import Controllers.FuncionarioController;
import Controllers.ProdutoController;
import Entities.Cliente;
import Entities.Produto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClienteController clienteController = new ClienteController();
        FuncionarioController funcionarioController = new FuncionarioController();
        ProdutoController produtoController = new ProdutoController();

        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("0. Sair");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente");
            System.out.println("4. Cadastrar Funcionario");
            System.out.println("5. Buscar Funcionario");
            System.out.println("6. Listar Funcionario");
            System.out.println("7. Cadastrar um Produto");
            System.out.println("8. Listar Produtos");
            System.out.println("9. Buscar um Produto");
            System.out.println("10. Atualizar Estoque");
            System.out.println("11. Apagar um Produto");

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
                case 6:
                    funcionarioController.listarFuncionarios();
                    break;
                case 7:
                    String nomeP = "";
                    float preco = 0;
                    int estoque = 0;
                    boolean dadosValidos = false;
                    while (!dadosValidos) {
                        try {
                            scanner.nextLine();
                            System.out.print("Digite o nome do produto: ");
                            nomeP = scanner.nextLine();

                            System.out.print("Digite o preço do produto: ");
                            preco = scanner.nextFloat();
                            if (preco <= 0) {
                                throw new IllegalArgumentException("O preço do produto deve ser um valor positivo.");
                            }

                            System.out.print("Digite o estoque do produto: ");
                            estoque = scanner.nextInt();
                            if (estoque < 0) {
                                throw new IllegalArgumentException("O estoque do produto deve ser um valor não negativo.");
                            }

                            dadosValidos = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Valor inserido inválido. Certifique-se de inserir um valor numérico válido.");
                            scanner.nextLine();
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    produtoController.cadastrarProduto(nomeP, preco, estoque);
                    break;
                case 8:
                    produtoController.listarProdutos();
                    break;
                case 9:
                    System.out.print("Digite o código do produto: ");
                    int codigoBusca = scanner.nextInt();
                    Produto produtoBuscado = produtoController.buscarProdutoPorCodigo(codigoBusca);
                    if (produtoBuscado != null) {
                        System.out.println("Produto encontrado:");
                        System.out.println("Código: " + produtoBuscado.getCodigo());
                        System.out.println("Nome: " + produtoBuscado.getNome());
                        System.out.println("Preço: " + produtoBuscado.getPreco());
                        System.out.println("Estoque: " + produtoBuscado.getEstoque());
                    } else {
                        System.out.println("Produto não encontrado.");
                    }
                    break;
                case 10:
                    System.out.print("Digite o código do produto: ");
                    int codigoAtualizacao = scanner.nextInt();
                    System.out.print("Digite o novo estoque: ");
                    int novoEstoque = scanner.nextInt();
                    produtoController.atualizarEstoque(codigoAtualizacao, novoEstoque);
                    break;
                case 11:
                    System.out.print("Digite o código do produto: ");
                    int codigoRemocao = scanner.nextInt();
                    produtoController.apagarProduto(codigoRemocao);
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}