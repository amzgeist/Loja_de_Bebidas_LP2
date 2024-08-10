package gui;

import dados.Funcionario;
import excecoes.*;
import negocio.Fachada;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ProdutoNaoEncontradoException, PedidoNaoEncontradoException, FuncionarioInativoException {
        Scanner scanner = new Scanner(System.in);
        Fachada fachada = new Fachada();
        int opcao;

        do {
            System.out.println("----- Menu -----");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Funcionarios");
            System.out.println("3. Gerenciar Produtos");
            System.out.println("4. Gerenciar Pedidos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    gerenciarClientes(fachada, scanner);
                    break;
                case 2:
                    gerenciarFuncionarios(fachada, scanner);
                    break;
                case 3:
                    gerenciarProdutos(fachada, scanner);
                    break;
                case 4:
                    gerenciarPedidos(fachada, scanner);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void gerenciarClientes(Fachada fachada, Scanner scanner) {
        int opcaoClientes;
        do {
            System.out.println("----- Gerenciar Clientes -----");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcaoClientes = Integer.parseInt(scanner.nextLine());

            switch (opcaoClientes) {
                case 1:
                    fachada.cadastrarCliente(scanner);
                    break;
                case 2:
                    fachada.listarClientes();
                    break;
                case 3:
                    System.out.print("Digite o CPF do cliente: ");
                    String cpf = scanner.nextLine();
                    try {
                        fachada.buscarCliente(cpf);
                    } catch (ClienteNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcaoClientes != 0);
    }

    private static void gerenciarFuncionarios(Fachada fachada, Scanner scanner) {
        int opcaoFuncionarios;
        do {
            System.out.println("----- Gerenciar Funcionários -----");
            System.out.println("1. Cadastrar Funcionário");
            System.out.println("2. Listar Funcionários");
            System.out.println("3. Buscar Funcionário");
            System.out.println("4. Atualizar Funcionário");
            System.out.println("5. Calcular Salário Funcionário");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcaoFuncionarios = Integer.parseInt(scanner.nextLine());
    
            switch (opcaoFuncionarios) {
                case 1:
                    fachada.cadastrarFuncionario(scanner);
                    break;
                case 2:
                    fachada.listarFuncionarios();
                    break;
                case 3:
                    System.out.print("Digite o código do funcionário que deseja buscar: ");
                    int codigo = Integer.parseInt(scanner.nextLine());
                    try {
                        Funcionario funcionario = fachada.buscarFuncionario(codigo);
                        System.out.println("Funcionário encontrado:");
                        System.out.println("Nome: " + funcionario.getNome());
                        System.out.println("CPF: " + funcionario.getCpf());
                        System.out.println("Tipo: " + funcionario.getTipo());
                        System.out.println("Salário: R$ " + funcionario.getSalario());
                        System.out.println("Ativo: " + (funcionario.isAtivo() ? "Sim" : "Não"));
                    } catch (FuncionarioNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    } catch (FuncionarioInativoException e) {
                        Funcionario funcionario = e.getFuncionario();
                        System.out.println(e.getMessage());
                        System.out.print("Deseja reativar o funcionário " + funcionario.getNome() + "? (Sim/Não): ");
                        String resposta = scanner.nextLine();
                        if (resposta.equalsIgnoreCase("Sim")) {
                            fachada.reativarFuncionario(funcionario.getCodigoFunc());
                        }
                    }
                    break;
                case 4:
                    fachada.atualizarFuncionario(scanner);
                    break;
                case 5:
                    System.out.print("Digite o código do funcionário para calcular o salário: ");
                    int codigoCalculo = Integer.parseInt(scanner.nextLine());
                    fachada.calcularSalarioFuncionario(codigoCalculo, scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcaoFuncionarios != 0);
    }
    

    private static void gerenciarProdutos(Fachada fachada, Scanner scanner) {
        int opcaoProdutos;
        do {
            System.out.println("----- Gerenciar Produtos -----");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Buscar Produto");
            System.out.println("4. Atualizar Produto");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcaoProdutos = Integer.parseInt(scanner.nextLine());

            switch (opcaoProdutos) {
                case 1:
                    try {
                        fachada.cadastrarProduto(scanner);
                    } catch (ProdutoJaExisteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    fachada.listarProdutos();
                    break;
                case 3:
                    System.out.print("Digite o código do produto: ");
                    int codigoProduto = Integer.parseInt(scanner.nextLine());
                    try {
                        fachada.buscarProduto(codigoProduto);
                    } catch (ProdutoNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    fachada.atualizarProduto(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcaoProdutos != 0);
    }

    private static void gerenciarPedidos(Fachada fachada, Scanner scanner) throws ProdutoNaoEncontradoException, PedidoNaoEncontradoException, FuncionarioInativoException {
        int opcaoPedidos;
        do {
            System.out.println("----- Gerenciar Pedidos -----");
            System.out.println("1. Criar Pedido");
            System.out.println("2. Listar Pedidos");
            System.out.println("3. Buscar Pedido");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcaoPedidos = Integer.parseInt(scanner.nextLine());

            switch (opcaoPedidos) {
                case 1:
                    try {
                        fachada.criarPedido(scanner);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    fachada.listarPedidos();
                    break;
                case 3:
                    System.out.print("Digite o número do pedido que deseja buscar: ");
                    int numeroPedido = Integer.parseInt(scanner.nextLine());
                    try {
                        fachada.buscarPedido(numeroPedido, scanner);
                    } catch (PedidoNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        } while (opcaoPedidos != 0);
    }
}
