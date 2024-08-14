package negocio;

import dados.Cliente;
import dados.Funcionario;
import dados.Pedido;
import dados.Produto;
import excecoes.*;

import java.util.*;

public class PedidoController {
    private static PedidoController instance;
    private List<Pedido> pedidos;

    private PedidoController() {
        this.pedidos = new ArrayList<>();
    }

    public static PedidoController getInstance() {
        if (instance == null) {
            instance = new PedidoController();
        }
        return instance;
    }

    public void criarPedido(Scanner scanner, ProdutoController produtoController, ClienteController clienteController, FuncionarioController funcionarioController)
            throws ProdutoNaoEncontradoException, FuncionarioInativoException, ClienteNaoEncontradoException {

        System.out.print("Digite o CPF do cliente: ");
        String cpfCliente = scanner.nextLine();
        Cliente cliente;
        try {
            cliente = clienteController.buscarCliente(cpfCliente);
        } catch (ClienteNaoEncontradoException e) {
            System.out.println("Cliente não encontrado. Deseja cadastrar um novo cliente? (Sim/Não)");
            if (scanner.nextLine().equalsIgnoreCase("Sim")) {
                clienteController.cadastrarCliente(scanner);
                cliente = clienteController.buscarCliente(cpfCliente);
            } else {
                return;
            }
        }

        System.out.print("Digite o código do funcionário: ");
        int codFunc = Integer.parseInt(scanner.nextLine());
        Funcionario funcionario = funcionarioController.buscarFuncionario(codFunc);

        System.out.println("Funcionário: " + funcionario.getNome() + " (CPF: " + funcionario.getCpf() + ")");

        Map<Produto, Integer> produtos = new HashMap<>();
        boolean continuar = true;

        while (continuar) {
            System.out.print("Digite o código do produto: ");
            int codigoProduto = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoController.buscarProduto(codigoProduto);

            System.out.println("Produto: " + produto.getNome() + " (Preço: R$ " + produto.getPreco() + ")");
            System.out.print("Digite a quantidade: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            if (produto.getEstoque() < quantidade) {
                System.out.println("Quantidade indisponível em estoque. Restam apenas " + produto.getEstoque() + " unidades.");
            } else {
                produtos.put(produto, quantidade);
                produto.setEstoque(produto.getEstoque() - quantidade);  // Subtraindo do estoque
                System.out.println("Produto adicionado ao pedido.");
            }

            System.out.print("Escolha uma opção: 1- Adicionar Produto\n 2- Remover Produto\n 3- Finalizar Pedido: ");
            String opcao = scanner.nextLine().toLowerCase();

            switch (opcao) {
                case "1":  // Adicionar Produto
                    System.out.print("Digite o código do produto: ");
                    int codProduto = Integer.parseInt(scanner.nextLine());
                    Produto produtoo = produtoController.buscarProduto(codProduto);

                    System.out.print("Digite a quantidade: ");
                    int quantidadee = Integer.parseInt(scanner.nextLine());

                    if (produto.getEstoque() < quantidadee) {
                        System.out.println("Quantidade indisponível em estoque. Restam apenas " + produtoo.getEstoque() + " unidades.");
                    } else {
                        produtos.put(produto, quantidade);
                        produto.setEstoque(produto.getEstoque() - quantidade);  // Subtraindo do estoque
                        System.out.println("Produto adicionado ao pedido.");
                    }
                    break;

                case "2":  // Remover Produto
                    if (produtos.isEmpty()) {
                        System.out.println("Não há produtos no pedido para remover.");
                        break;
                    }
                    System.out.print("Digite o código do produto que deseja remover: ");
                    int codigoRemover = Integer.parseInt(scanner.nextLine());
                    Produto produtoRemover = produtoController.buscarProduto(codigoRemover);
                    if (produtos.containsKey(produtoRemover)) {
                        int qtdRemovida = produtos.remove(produtoRemover);
                        produtoRemover.setEstoque(produtoRemover.getEstoque() + qtdRemovida);  // Devolvendo ao estoque
                        System.out.println("Produto removido do pedido.");
                    } else {
                        System.out.println("Produto não encontrado no pedido.");
                    }
                    break;

                case "3":  // Finalizar Pedido
                    if (produtos.isEmpty()) {
                        System.out.println("Não é possível finalizar um pedido sem produtos. Adicione pelo menos um produto.");
                    } else {
                        continuar = false;
                    }
                    break;

                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
            }
        }

        System.out.print("Digite o endereço para entrega: ");
        String endereco = scanner.nextLine();

        System.out.println("Escolha a forma de pagamento: ");
        System.out.println("1. Cartão de Crédito");
        System.out.println("2. Cartão de Débito");
        System.out.println("3. Pix");
        System.out.println("4. Dinheiro");
        int formaPagamento = Integer.parseInt(scanner.nextLine());

        Pedido pedido = new Pedido(cliente, funcionario, endereco, produtos, getFormaPagamento(formaPagamento));

        pedido.exibirResumo();

        System.out.print("Deseja confirmar o pedido? (Sim/Não): ");
        if (scanner.nextLine().equalsIgnoreCase("Sim")) {
            pedido.setStatus("Pedido Confirmado. Aguardando Faturamento");
            pedidos.add(pedido);
            System.out.println("Pedido criado com sucesso!");
        } else {
            System.out.println("Pedido cancelado.");
        }
    }

    private String getFormaPagamento(int opcao) {
        switch (opcao) {
            case 1:
                return "Cartão de Crédito";
            case 2:
                return "Cartão de Débito";
            case 3:
                return "Pix";
            case 4:
                return "Dinheiro";
            default:
                return "Não especificado";
        }
    }

    public void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado.");
            return;
        }

        for (Pedido pedido : pedidos) {
            System.out.println("Pedido: " + pedido.getCodigo());
            System.out.println("Cliente: " + pedido.getCliente().getNome() + " (CPF: " + pedido.getCliente().getCpf() + ")");
            System.out.println("Funcionário: " + pedido.getFuncionario().getNome() + " (Código: " + pedido.getFuncionario().getCodigoFunc() + ")");
            System.out.println("Endereço: " + pedido.getEndereco());
            System.out.println("Itens do Pedido:");

            for (Map.Entry<Produto, Integer> entry : pedido.getProdutos().entrySet()) {
                Produto produto = entry.getKey();
                int quantidade = entry.getValue();
                System.out.println("  Produto: " + produto.getNome() + " (Código: " + produto.getCodigo() + ")");
                System.out.println("  Quantidade: " + quantidade);
                System.out.println("  Subtotal: R$ " + (produto.getPreco() * quantidade));
            }

            System.out.println("Total do Pedido: R$ " + pedido.calcularTotal());
            System.out.println("Forma de Pagamento: " + pedido.getFormaPagamento());
            System.out.println("Status: " + pedido.getStatus());
            System.out.println("------------------------------------------------");
        }
    }

    public Pedido encontrarPedidoPorCodigo(int numeroPedido) throws PedidoNaoEncontradoException {
        for (Pedido pedido : pedidos) {
            if (pedido.getCodigo() == numeroPedido) {
                return pedido;
            }
        }
        throw new PedidoNaoEncontradoException("Pedido com número " + numeroPedido + " não encontrado.");
    }

    public void buscarPedido(int numeroPedido, Scanner scanner) {
        try {
            Pedido pedido = encontrarPedidoPorCodigo(numeroPedido); // Chama o método correto
            pedido.exibirResumo();

            // Menu para opções adicionais após exibir o resumo do pedido
            boolean sair = false;
            while (!sair) {
                System.out.println("Escolha uma opção:");
                System.out.println("1. Atualizar status do pedido");
                System.out.println("2. Cancelar pedido");
                System.out.println("3. Voltar ao menu anterior");
                System.out.print("Opção: ");
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        atualizarStatusPedido(pedido, scanner);
                        break;
                    case 2:
                        cancelarPedido(pedido);
                        sair = true;
                        break;
                    case 3:
                        sair = true;
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } catch (PedidoNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizarStatusPedido(Pedido pedido, Scanner scanner) {
        String statusAtual = pedido.getStatus();
        switch (statusAtual) {
            case "Pedido Confirmado. Aguardando Faturamento":
                System.out.println("Deseja confirmar o pagamento do pedido? (Sim/Não)");
                if (scanner.nextLine().equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Pedido Faturado. Aguardando montagem");
                    System.out.println("Status atualizado para 'Pedido Faturado. Aguardando montagem'.");
                }
                break;
            case "Pedido Faturado. Aguardando montagem":
                System.out.println("Deseja seguir com a entrega? (Sim/Não)");
                if (scanner.nextLine().equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Aguardando entrega");
                    System.out.println("Status atualizado para 'Aguardando entrega'.");
                }
                break;
            case "Aguardando entrega":
                System.out.println("Deseja confirmar a saída do pedido? (Sim/Não)");
                if (scanner.nextLine().equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Pedido a caminho");
                    System.out.println("Status atualizado para 'Pedido a caminho'.");
                }
                break;
            case "Pedido a caminho":
                System.out.println("Deseja confirmar a entrega do pedido? (Sim/Não)");
                if (scanner.nextLine().equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Entrega concluída. Pedido Finalizado.");
                    System.out.println("Status atualizado para 'Entrega concluída. Pedido Finalizado.'.");
                }
                break;
            default:
                System.out.println("O pedido já foi finalizado ou cancelado e não pode mais ser atualizado.");
        }
    }

    private void cancelarPedido(Pedido pedido) {
        if (pedido.getStatus().equals("Pedido Faturado. Aguardando montagem") ||
                pedido.getStatus().equals("Aguardando entrega") ||
                pedido.getStatus().equals("Pedido a caminho") ||
                pedido.getStatus().equals("Entrega concluída. Pedido Finalizado.")) {
            System.out.println("Não é possível cancelar um pedido que já foi faturado ou entregue.");
        } else {
            pedido.setStatus("Cancelado");
            System.out.println("Pedido cancelado com sucesso.");
        }
    }
}
