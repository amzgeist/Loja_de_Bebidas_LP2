package negocio;

import dados.Pedido;
import dados.Cliente;
import dados.Funcionario;
import dados.Produto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import excecoes.ClienteNaoEncontradoException;
import excecoes.FuncionarioNaoEncontradoException;
import excecoes.FuncionarioInativoException;
import excecoes.ProdutoNaoEncontradoException;

public class PedidoController {
    private ArrayList<Pedido> pedidos;
    private static PedidoController instance;

    private PedidoController() {
        pedidos = new ArrayList<>();
    }

    public static PedidoController getInstance() {
        if (instance == null) {
            instance = new PedidoController();
        }
        return instance;
    }

    public void criarPedido(Scanner scanner, ProdutoController produtoController, ClienteController clienteController, FuncionarioController funcionarioController) {
        try {
            System.out.print("Digite o CPF do cliente: ");
            String cpf = scanner.nextLine();
            Cliente cliente = clienteController.buscarCliente(cpf);
            if (cliente == null) {
                System.out.println("Cliente não encontrado, iniciando cadastro...");
                clienteController.cadastrarCliente(scanner);
                cliente = clienteController.buscarCliente(cpf);
            }

            System.out.print("Digite o código do funcionário: ");
            int codFunc = Integer.parseInt(scanner.nextLine());
            Funcionario funcionario = funcionarioController.buscarFuncionario(codFunc);
            if (funcionario == null || !funcionario.isAtivo()) {
                throw new FuncionarioNaoEncontradoException("Funcionário não encontrado ou inativo.");
            }

            HashMap<Produto, Integer> produtos = new HashMap<>();
            String maisProdutos;
            do {
                System.out.print("Digite o código do produto: ");
                int codigoProduto = Integer.parseInt(scanner.nextLine());
                Produto produto = produtoController.buscarProduto(codigoProduto);
                if (produto == null) {
                    throw new ProdutoNaoEncontradoException("Produto não encontrado.");
                }
                System.out.println("Produto selecionado: " + produto.getNome() + " - Preço: R$" + produto.getPreco());
                System.out.print("Digite a quantidade desejada: ");
                int quantidade = Integer.parseInt(scanner.nextLine());
                produtos.put(produto, quantidade);

                System.out.print("Deseja adicionar mais produtos? (Sim/Não): ");
                maisProdutos = scanner.nextLine();
            } while (maisProdutos.equalsIgnoreCase("Sim"));

            System.out.print("Digite o endereço de entrega: ");
            String endereco = scanner.nextLine();

            System.out.println("Formas de pagamento:");
            System.out.println("1. Cartão de Crédito");
            System.out.println("2. Cartão de Débito");
            System.out.println("3. Pix");
            System.out.println("4. Dinheiro");
            System.out.print("Selecione a forma de pagamento: ");
            int formaPagamento = Integer.parseInt(scanner.nextLine());

            Pedido pedido = new Pedido(cliente, funcionario, endereco, produtos, formaPagamento);
            pedidos.add(pedido);
            System.out.println("Pedido criado com sucesso. Código do Pedido: " + pedido.getCodigo());

            // Exibindo resumo do pedido
            System.out.println("Resumo do Pedido:");
            listarProdutosPedido(pedido);
            System.out.println("Total do Pedido: R$" + pedido.calcularTotal());
            System.out.println("Deseja finalizar o pedido? (Sim/Não):");
            String finalizar = scanner.nextLine();
            if (finalizar.equalsIgnoreCase("Sim")) {
                System.out.println("Pedido finalizado e aguardando pagamento.");
            } else {
                System.out.println("Pedido cancelado.");
            }

        } catch (NumberFormatException | ClienteNaoEncontradoException | FuncionarioNaoEncontradoException | ProdutoNaoEncontradoException e) {
            System.out.println("Erro ao criar pedido: " + e.getMessage());
        }
    }

    public void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
            listarProdutosPedido(pedido);
            System.out.println("-------------------------------");
        }
    }

    public Pedido buscarPedido(int codigo) {
        for (Pedido pedido : pedidos) {
            if (pedido.getCodigo() == codigo) {
                System.out.println("Pedido encontrado:");
                listarProdutosPedido(pedido);
                return pedido;
            }
        }
        System.out.println("Pedido não encontrado.");
        return null;
    }

    private void listarProdutosPedido(Pedido pedido) {
        System.out.println("Produtos no pedido:");
        for (Produto produto : pedido.getProdutos().keySet()) {
            int quantidade = pedido.getProdutos().get(produto);
            System.out.println("- " + produto.getNome() + " - Quantidade: " + quantidade + ", Preço unitário: R$ " + produto.getPreco() + ", Subtotal: R$ " + (quantidade * produto.getPreco()));
        }
        System.out.println("Total: R$ " + pedido.calcularTotal());
    }
}
