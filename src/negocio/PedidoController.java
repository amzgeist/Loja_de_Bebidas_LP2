package negocio;

import dados.Cliente;
import dados.Funcionario;
import dados.Pedido;
import dados.Produto;
import excecoes.ClienteNaoEncontradoException;
import excecoes.FuncionarioInativoException;
import excecoes.FuncionarioNaoEncontradoException;
import excecoes.ProdutoNaoEncontradoException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PedidoController {
    private ArrayList<Pedido> pedidos;
    private static PedidoController instance;

    private PedidoController() {
        this.pedidos = new ArrayList<>();
    }

    public static PedidoController getInstance() {
        if (instance == null) {
            instance = new PedidoController();
        }
        return instance;
    }

    public void criarPedido(Scanner scanner) throws ProdutoNaoEncontradoException {
        Cliente cliente = null;
        while (cliente == null) {
            System.out.print("Digite o CPF do cliente: ");
            String cpfCliente = scanner.nextLine();
            try {
                cliente = ClienteController.getInstance().buscarCliente(cpfCliente);
            } catch (ClienteNaoEncontradoException e) {
                System.out.println(e.getMessage());
                System.out.print("Deseja cadastrar um novo cliente? (S/N): ");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("S")) {
                    ClienteController.getInstance().cadastrarCliente(scanner);
                    try {
                        cliente = ClienteController.getInstance().buscarCliente(cpfCliente);
                    } catch (ClienteNaoEncontradoException ex) {
                        System.out.println("Erro ao cadastrar cliente. Tente novamente.");
                    }
                }
            }
        }

        Funcionario funcionario = null;
        while (funcionario == null) {
            System.out.print("Digite o código do funcionário: ");
            int codFunc = Integer.parseInt(scanner.nextLine());
            try {
                funcionario = FuncionarioController.getInstance().buscarFuncionario(codFunc);
                System.out.println("Funcionário encontrado: " + funcionario.getNome() + " (CPF: " + funcionario.getCpf() + ")");
            } catch (FuncionarioNaoEncontradoException e) {
                System.out.println(e.getMessage());
                return;
            } catch (FuncionarioInativoException e) {
                Funcionario func = e.getFuncionario();
                System.out.println("Funcionário " + func.getNome() + " está inativo. Deseja reativá-lo? (S/N)");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("S")) {
                    FuncionarioController.getInstance().reativarFuncionario(codFunc);
                    try {
                        funcionario = FuncionarioController.getInstance().buscarFuncionario(codFunc);
                        System.out.println("Funcionário reativado: " + funcionario.getNome() + " (CPF: " + funcionario.getCpf() + ")");
                    } catch (FuncionarioNaoEncontradoException | FuncionarioInativoException ex) {
                        System.out.println("Erro ao reativar funcionário. Tente novamente.");
                    }
                } else {
                    return;
                }
            }
        }

        Map<Produto, Integer> produtos = new HashMap<>();
        boolean finalizarPedido = false;

        while (!finalizarPedido) {
            System.out.print("Digite o código do produto: ");
            int codProduto = Integer.parseInt(scanner.nextLine());
            Produto produto = ProdutoController.getInstance().buscarProduto(codProduto);
            System.out.println("Produto encontrado: " + produto.getNome() + " - R$" + produto.getPreco());
            System.out.print("Digite a quantidade: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            produtos.put(produto, quantidade);

            while (true) {
                System.out.print("Deseja adicionar, remover produtos ou finalizar pedido? (Adicionar/Remover/Finalizar): ");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("Finalizar")) {
                    finalizarPedido = true;
                    break;
                } else if (resposta.equalsIgnoreCase("Adicionar")) {
                    break;
                } else if (resposta.equalsIgnoreCase("Remover")) {
                    System.out.print("Digite o código do produto a ser removido: ");
                    int codRemover = Integer.parseInt(scanner.nextLine());
                    Produto produtoRemover = ProdutoController.getInstance().buscarProduto(codRemover);
                    if (produtoRemover != null && produtos.containsKey(produtoRemover)) {
                        produtos.remove(produtoRemover);
                        System.out.println("Produto removido do pedido.");
                    } else {
                        System.out.println("Produto não encontrado no pedido.");
                    }
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            }
        }

        System.out.print("Digite o endereço de entrega: ");
        String endereco = scanner.nextLine();

        System.out.print("Digite a forma de pagamento (1. Cartão de Crédito / 2. Cartão de Débito / 3. Pix / 4. Dinheiro): ");
        int formaPagamento = Integer.parseInt(scanner.nextLine());

        Pedido pedido = new Pedido(cliente, funcionario, endereco, produtos, formaPagamento);

        System.out.println("Resumo do Pedido:");
        pedido.exibirResumo();

        System.out.print("Deseja confirmar o pedido? (S/N): ");
        String confirmar = scanner.nextLine();
        if (confirmar.equalsIgnoreCase("S")) {
            pedidos.add(pedido);
            System.out.println("Pedido criado com sucesso!");
        } else {
            System.out.println("Pedido cancelado.");
        }
    }

    public void listarPedidos() {
        for (Pedido pedido : pedidos) {
            pedido.exibirResumo();
            System.out.println("-----------------------------------");
        }
    }

    public void buscarPedido(int numeroPedido) {
        for (Pedido pedido : pedidos) {
            if (pedido.getCodigo() == numeroPedido) {
                pedido.exibirResumo();
                System.out.println("-----------------------------------");
                return;
            }
        }
        System.out.println("Pedido não encontrado.");
    }
}
