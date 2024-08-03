package gui;

import java.util.Scanner;

import excecoes.ClienteNaoEncontradoException;
import excecoes.FuncionarioNaoEncontradoException;
import excecoes.ProdutoJaExisteException;
import negocio.ClienteController;
import negocio.FuncionarioController;
import negocio.ProdutoController;
import negocio.PedidoController;
import dados.Cliente;
import dados.Funcionario;
import dados.Produto;
import dados.Pedido;

public class Main {

    public static void main(String[] args) throws ProdutoJaExisteException {
        Scanner scanner = new Scanner(System.in);
        ClienteController clienteController = ClienteController.getInstance();
        FuncionarioController funcionarioController = FuncionarioController.getInstance();
        ProdutoController produtoController = ProdutoController.getInstance();
        PedidoController pedidoController = PedidoController.getInstance();

        while (true) {
            System.out.println("\n--- Sistema de Gestão ---");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Funcionários");
            System.out.println("3. Gerenciar Produtos");
            System.out.println("4. Gerenciar Pedidos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    gerenciarClientes(scanner, clienteController);
                    break;
                case 2:
                    gerenciarFuncionarios(scanner, funcionarioController);
                    break;
                case 3:
                    gerenciarProdutos(scanner, produtoController);
                    break;
                case 4:
                    gerenciarPedidos(scanner, pedidoController, produtoController, clienteController, funcionarioController);
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private static void gerenciarClientes(Scanner scanner, ClienteController clienteController) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Gerenciar Clientes ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente");
            System.out.println("4. Atualizar Cliente");
            System.out.println("5. Deletar Cliente");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    clienteController.cadastrarCliente(scanner);
                    break;
                case 2:
                    clienteController.listarClientes();
                    break;
                case 3:
                    System.out.print("Digite o CPF do cliente que deseja buscar: ");
                    String cpf = scanner.nextLine();
                    try {
                        Cliente clienteEncontrado = clienteController.buscarCliente(cpf);  // Passando a String do CPF diretamente
                        if (clienteEncontrado != null) {
                            System.out.println("Cliente encontrado: " + clienteEncontrado.getNome());
                            // Outras ações após encontrar o cliente
                        }
                    } catch (ClienteNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                        // Opção para o usuário tentar novamente ou voltar ao menu principal
                    }
                    break;
                case 4:
                    clienteController.atualizarCliente(scanner);
                    break;
                /*case 5:
                    clienteController.deletarCliente(scanner);
                    break;*/
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private static void gerenciarFuncionarios(Scanner scanner, FuncionarioController funcionarioController) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Gerenciar Funcionários ---");
            System.out.println("1. Cadastrar Funcionário");
            System.out.println("2. Listar Funcionários");
            System.out.println("3. Buscar Funcionário");
            System.out.println("4. Atualizar Funcionário");
            System.out.println("5. Deletar Funcionário");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());


            switch (opcao) {
                case 1:
                    funcionarioController.cadastrarFuncionario(scanner);
                    break;
                case 2:
                    funcionarioController.listarFuncionarios();
                    break;
                case 3:  // Buscar Funcionário
                    System.out.print("Digite o código do funcionário para busca: ");
                    int codFunc = scanner.nextInt();
                    scanner.nextLine();  // Limpar o buffer
                    try {
                        Funcionario funcionario = funcionarioController.buscarFuncionario(codFunc);
                        // Funcionário pode ser reativado internamente se estiver inativo.
                    } catch (FuncionarioNaoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    funcionarioController.atualizarFuncionario(scanner);
                    break;
                case 5:
                    desativarFuncionario(funcionarioController, scanner);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private static void desativarFuncionario(FuncionarioController funcionarioController, Scanner scanner) {
        System.out.print("Digite o código do funcionário para desativar: ");
        if (!scanner.hasNextLine()) {
            System.out.println("Nenhuma entrada detectada. Voltando ao menu principal...");
            return;
        }
        try {
            int codFunc = Integer.parseInt(scanner.nextLine());
            funcionarioController.deletarFuncionario(codFunc);
        } catch (NumberFormatException e) {
            System.out.println("Código inválido. Por favor, digite um número inteiro.");
        }
    }

    private static void gerenciarProdutos(Scanner scanner, ProdutoController produtoController) throws ProdutoJaExisteException {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Gerenciar Produtos ---");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Buscar Produto");
            System.out.println("4. Atualizar Estoque");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    produtoController.cadastrarProduto(scanner);
                    break;
                case 2:
                    produtoController.listarProdutos();
                    break;
                case 3:  // Buscar Produto
                    System.out.print("Digite o código do produto para buscar: ");
                    int codigoProduto = Integer.parseInt(scanner.nextLine());
                    Produto produtoBuscado = produtoController.buscarProduto(codigoProduto);
                    if (produtoBuscado != null) {
                        System.out.println("Produto encontrado!");
                        System.out.println("Nome: " + produtoBuscado.getNome());
                        System.out.println("Preço: R$ " + produtoBuscado.getPreco());
                        System.out.println("Estoque: " + produtoBuscado.getEstoque());
                    } else {
                        System.out.println("Produto não encontrado.");
                    }
                    break;
                case 4:  // Atualizar Estoque
                    produtoController.atualizarEstoque(scanner);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private static void gerenciarPedidos(Scanner scanner, PedidoController pedidoController, ProdutoController produtoController, ClienteController clienteController, FuncionarioController funcionarioController) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Gerenciar Pedidos ---");
            System.out.println("1. Criar Pedido");
            System.out.println("2. Listar Pedidos");
            System.out.println("3. Buscar Pedido");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    pedidoController.criarPedido(scanner, produtoController, clienteController, funcionarioController);
                    break;
                case 2:
                    pedidoController.listarPedidos();
                    break;
                case 3:  // Buscar Pedido
                    System.out.print("Digite o código do pedido que deseja buscar: ");
                    int codigoPedido = Integer.parseInt(scanner.nextLine());
                    Pedido pedidoEncontrado = pedidoController.buscarPedido(codigoPedido);
                    if (pedidoEncontrado != null) {
                        System.out.println("Pedido encontrado!");
                        System.out.println("Código do Pedido: " + pedidoEncontrado.getCodigo());
                        // Aqui você pode adicionar mais detalhes sobre o pedido conforme necessário
                    } else {
                        System.out.println("Pedido não encontrado.");
                    }
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }
}
