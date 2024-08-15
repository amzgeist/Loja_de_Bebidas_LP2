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

    public void criarPedido(Scanner scanner, ProdutoController produtoController, ClienteController clienteController, FuncionarioController funcionarioController) throws FuncionarioInativoException, ProdutoNaoEncontradoException {
        System.out.print("Digite o CPF do cliente: ");
        String cpfCliente = scanner.nextLine();
        Cliente cliente;

        try {
            cliente = clienteController.buscarCliente(cpfCliente);
        } catch (ClienteNaoEncontradoException e) {
            System.out.println("Cliente não encontrado.");
            System.out.print("Deseja cadastrar um novo cliente? (Sim/Não): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("sim")) {
                clienteController.cadastrarCliente(scanner);
                cliente = clienteController.buscarCliente(cpfCliente); // Tentativa de buscar o cliente novamente após cadastro
            } else {
                return;
            }
        }

        System.out.print("Digite o código do funcionário que está criando o pedido: ");
        int codFunc = Integer.parseInt(scanner.nextLine());
        Funcionario funcionario;

        try {
            funcionario = funcionarioController.buscarFuncionario(codFunc);
        } catch (FuncionarioNaoEncontradoException e) {
            System.out.println(e.getMessage());
            return;
        } catch (FuncionarioInativoException e) {
            System.out.println("Funcionário inativo. Deseja reativá-lo? (Sim/Não): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("sim")) {
                funcionarioController.reativarFuncionario(codFunc);
                funcionario = funcionarioController.buscarFuncionario(codFunc);
            } else {
                return;
            }
        }

        System.out.print("Digite o endereço para entrega: ");
        String endereco = scanner.nextLine();

        Map<Produto, Integer> produtos = new HashMap<>();
        while (true) {
            System.out.print("Digite o código do produto: ");
            int codigoProduto = Integer.parseInt(scanner.nextLine());
            Produto produto;

            try {
                produto = produtoController.buscarProduto(codigoProduto);
            } catch (ProdutoNaoEncontradoException e) {
                System.out.println(e.getMessage());
                continue;
            }

            System.out.print("Digite a quantidade: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            if (quantidade > produto.getEstoque()) {
                System.out.println("Estoque insuficiente. Quantidade disponível: " + produto.getEstoque());
                continue;
            }

            produtos.put(produto, quantidade);
            produto.setEstoque(produto.getEstoque() - quantidade);

            System.out.print("Deseja adicionar, remover produtos ou finalizar pedido? (Adicionar/Remover/Finalizar): ");
            String opcao = scanner.nextLine().trim().toLowerCase();

            switch (opcao) {
                case "adicionar":
                    continue;
                case "remover":
                    System.out.print("Digite o código do produto que deseja remover: ");
                    int codigoRemover = Integer.parseInt(scanner.nextLine());
                    Produto produtoRemover = produtoController.buscarProduto(codigoRemover);
                    if (produtos.containsKey(produtoRemover)) {
                        int quantidadeRemovida = produtos.remove(produtoRemover);
                        produto.setEstoque(produto.getEstoque() + quantidadeRemovida);
                        System.out.println("Produto removido do pedido.");
                    } else {
                        System.out.println("Produto não encontrado no pedido.");
                    }
                    break;
                case "finalizar":
                    if (produtos.isEmpty()) {
                        System.out.println("Não é possível finalizar um pedido sem produtos.");
                    } else {
                        break;
                    }
                default:
                    System.out.println("Opção inválida.");
                    continue;
            }
            break;
        }

        System.out.println("Escolha a forma de pagamento:");
        System.out.println("1. Cartão de Crédito");
        System.out.println("2. Cartão de Débito");
        System.out.println("3. Pix");
        System.out.println("4. Dinheiro");
        int formaPagamentoEscolha = Integer.parseInt(scanner.nextLine());
        String formaPagamento;

        switch (formaPagamentoEscolha) {
            case 1:
                formaPagamento = "Cartão de Crédito";
                break;
            case 2:
                formaPagamento = "Cartão de Débito";
                break;
            case 3:
                formaPagamento = "Pix";
                break;
            case 4:
                formaPagamento = "Dinheiro";
                break;
            default:
                formaPagamento = "Não especificado";
                System.out.println("Opção de pagamento inválida. Usando valor padrão: Não especificado.");
                break;
        }

        Pedido pedido = new Pedido(cliente, funcionario, endereco, produtos, formaPagamento);
        pedido.exibirResumo();

        System.out.print("Deseja confirmar o pedido? (Sim/Não): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();

        if (confirmacao.equals("sim")) {
            pedidos.add(pedido);
            System.out.println("Pedido confirmado com sucesso. Status: " + pedido.getStatus());
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


    public Pedido buscarPedido(int codigoPedido) throws PedidoNaoEncontradoException {
        for (Pedido pedido : pedidos) {
            if (pedido.getCodigo() == codigoPedido) {
                return pedido;
            }
        }
        throw new PedidoNaoEncontradoException("Pedido com código " + codigoPedido + " não encontrado.");
    }

    public void exibirDetalhesPedido(Pedido pedido, Scanner scanner) {
        pedido.exibirResumo(); // Exibe o resumo do pedido

        System.out.println("O que você deseja fazer?");
        System.out.println("1. Atualizar status do pedido");
        System.out.println("2. Cancelar pedido");
        System.out.println("3. Adicionar pagamento");
        System.out.println("4. Voltar ao menu anterior");

        int opcao = Integer.parseInt(scanner.nextLine());

        switch (opcao) {
            case 1:
                atualizarStatusPedido(pedido.getCodigo(), scanner);
                break;
            case 2:
                try {
                    cancelarPedido(pedido.getCodigo());
                } catch (PedidoNaoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 3:
                adicionarPagamento(pedido, scanner);
                break;
            case 4:
                // Voltar ao menu anterior
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    public void atualizarStatusPedido(int codigoPedido, Scanner scanner) {
        try {
            Pedido pedido = buscarPedido(codigoPedido); // Correto: `codigoPedido` é um `int`, que é esperado pelo método.


            if (pedido.getStatus().equals("Pedido Confirmado. Aguardando Faturamento")) {
                System.out.println("Deseja confirmar o pagamento do pedido? (Sim/Não): ");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("Sim") && pedido.getValorRecebido() >= pedido.calcularTotal()) {
                    pedido.setStatus("Pedido Faturado. Aguardando montagem");
                    System.out.println("Status do pedido atualizado para 'Pedido Faturado. Aguardando montagem'.");
                } else {
                    System.out.println("O pedido não pode ser atualizado. Verifique se o pagamento foi feito.");
                }
            } else if (pedido.getStatus().equals("Pedido Faturado. Aguardando montagem")) {
                System.out.println("Deseja seguir com a entrega? (Sim/Não): ");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Aguardando entrega");
                    System.out.println("Status do pedido atualizado para 'Aguardando entrega'.");
                } else {
                    System.out.println("Operação cancelada.");
                }
            } else if (pedido.getStatus().equals("Aguardando entrega")) {
                System.out.println("Deseja confirmar a saída do pedido? (Sim/Não): ");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Pedido a caminho");
                    System.out.println("Status do pedido atualizado para 'Pedido a caminho'.");
                } else {
                    System.out.println("Operação cancelada.");
                }
            } else if (pedido.getStatus().equals("Pedido a caminho")) {
                System.out.println("Deseja confirmar a entrega do pedido? (Sim/Não): ");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Entrega concluída. Pedido Finalizado");
                    System.out.println("Status do pedido atualizado para 'Entrega concluída. Pedido Finalizado'.");
                } else {
                    System.out.println("Operação cancelada.");
                }
            } else {
                System.out.println("O pedido não pode ser atualizado.");
            }
        } catch (PedidoNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }


    public void cancelarPedido(int codigoPedido) throws PedidoNaoEncontradoException {
        Pedido pedido = buscarPedido(codigoPedido);
        if (pedido.getStatus().equals("Pedido Faturado. Aguardando montagem") ||
                pedido.getStatus().equals("Pedido a caminho") ||
                pedido.getStatus().equals("Entrega concluída. Pedido Finalizado")) {
            System.out.println("Não é possível cancelar um pedido já faturado ou em andamento.");
        } else {
            // Revertendo o estoque dos produtos
            for (Map.Entry<Produto, Integer> entry : pedido.getProdutos().entrySet()) {
                Produto produto = entry.getKey();
                int quantidade = entry.getValue();
                produto.setEstoque(produto.getEstoque() + quantidade);
            }
            pedido.setStatus("Cancelado");
            System.out.println("Pedido cancelado com sucesso.");
        }
    }

    public void adicionarPagamento(Pedido pedido, Scanner scanner) {
        double total = pedido.calcularTotal();
        double valorRestante = total - pedido.getValorRecebido();

        System.out.println("Total do pedido: R$" + total);
        System.out.println("Valor já pago: R$" + pedido.getValorRecebido());
        System.out.println("Valor restante: R$" + valorRestante);

        System.out.print("Digite o valor do pagamento: ");
        double valorPagamento = Double.parseDouble(scanner.nextLine());

        if (valorPagamento > 0 && valorPagamento <= valorRestante) {
            pedido.setValorRecebido(pedido.getValorRecebido() + valorPagamento);
            System.out.println("Pagamento adicionado com sucesso.");

            if (pedido.getValorRecebido() == total) {
                pedido.setStatusPagamento("Pago");
                System.out.println("Pagamento completo. Status do pedido atualizado para 'Pago'.");
            }
        } else {
            System.out.println("Valor inválido. O pagamento deve ser maior que zero e não pode exceder o valor restante.");
        }
    }
}
