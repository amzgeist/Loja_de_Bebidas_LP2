package negocio;

import dados.*;
import excecoes.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PedidoController {

    private static PedidoController instance;
    private PedidoDAO pedidoDAO;

    private PedidoController() {
        this.pedidoDAO = PedidoDAO.getInstance();
    }

    public static PedidoController getInstance() {
        if (instance == null) {
            instance = new PedidoController();
        }
        return instance;
    }

    public void criarPedido(Scanner scanner, ProdutoController produtoController, ClienteController clienteController, FuncionarioController funcionarioController) throws SQLException, ProdutoNaoEncontradoException, FuncionarioInativoException, ClienteNaoEncontradoException, FuncionarioNaoEncontradoException {
        System.out.print("Digite o CPF do cliente: ");
        String cpfCliente = scanner.nextLine();
        Cliente cliente = clienteController.buscarCliente(cpfCliente);

        if (cliente == null) {
            throw new ClienteNaoEncontradoException(cpfCliente);
        }

        System.out.print("Digite o código do funcionário que está criando o pedido: ");
        int codFunc = Integer.parseInt(scanner.nextLine());
        Funcionario funcionario = funcionarioController.buscarFuncionario(codFunc);

        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(codFunc);
        }

        if (!funcionario.isAtivo()) {
            throw new FuncionarioInativoException(funcionario.getNome());
        }

        Map<Produto, Integer> produtos = new HashMap<>();

        while (true) {
            System.out.print("Digite o código do produto: ");
            int codProduto = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoController.buscarProduto(codProduto);

            if (produto == null) {
                throw new ProdutoNaoEncontradoException(String.valueOf(codProduto));
            }

            System.out.print("Digite a quantidade: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            if (quantidade > produto.getEstoque()) {
                System.out.println("Quantidade indisponível em estoque. Disponível: " + produto.getEstoque());
                continue;
            }

            produtos.put(produto, quantidade);

            System.out.print("Deseja adicionar outro produto? (Sim/Não): ");
            String opcao = scanner.nextLine();

            if (opcao.equalsIgnoreCase("Não")) {
                break;
            }
        }

        if (produtos.isEmpty()) {
            System.out.println("Não é possível criar um pedido sem produtos.");
            return;
        }

        System.out.print("Digite o endereço de entrega: ");
        String endereco = scanner.nextLine();

        System.out.println("Escolha a forma de pagamento:");
        System.out.println("1. Cartão de Crédito");
        System.out.println("2. Cartão de Débito");
        System.out.println("3. Pix");
        System.out.println("4. Dinheiro");

        int formaPagamento = Integer.parseInt(scanner.nextLine());
        String formaPagamentoStr;

        switch (formaPagamento) {
            case 1:
                formaPagamentoStr = "Cartão de Crédito";
                break;
            case 2:
                formaPagamentoStr = "Cartão de Débito";
                break;
            case 3:
                formaPagamentoStr = "Pix";
                break;
            case 4:
                formaPagamentoStr = "Dinheiro";
                break;
            default:
                formaPagamentoStr = "Não especificado";
                break;
        }

        Pedido pedido = new Pedido(cliente, funcionario, endereco, produtos, formaPagamentoStr);
        pedido.exibirResumo();

        System.out.print("Deseja confirmar o pedido? (Sim/Não): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("Sim")) {
            pedidoDAO.inserirPedido(pedido);

            // Atualiza o estoque dos produtos
            for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
                Produto produto = entry.getKey();
                int quantidadeVendida = entry.getValue();
                produto.setEstoque(produto.getEstoque() - quantidadeVendida);
                produtoController.atualizarProdutoNoBanco(produto);
            }

            System.out.println("Pedido confirmado e registrado com sucesso.");
        } else {
            System.out.println("Pedido cancelado.");
        }
    }


    public void listarPedidos() throws SQLException {
        List<Pedido> pedidos = PedidoDAO.getInstance().listarPedidos();
        exibirPedidos(pedidos);
    }

    public Pedido buscarPedido(int codigoPedido, Scanner scanner) throws PedidoNaoEncontradoException, SQLException {
        Pedido pedido = pedidoDAO.buscarPedidoPorCodigo(codigoPedido);
        if (pedido == null) {
            throw new PedidoNaoEncontradoException("Pedido com código " + codigoPedido + " não encontrado.");
        }
        return pedido;
    }

    public void exibirPedidos(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
        } else {
            for (Pedido pedido : pedidos) {
                pedido.exibirResumo();
            }
        }
    }

    public void atualizarStatusPedido(int numeroPedido, Scanner scanner) throws SQLException, PedidoNaoEncontradoException {
        Pedido pedido = PedidoDAO.getInstance().buscarPedidoPorCodigo(numeroPedido);
        pedido.exibirResumo();

        switch (pedido.getStatus()) {
            case "Pedido Confirmado. Aguardando Faturamento":
                System.out.print("Deseja confirmar o pagamento do pedido? (Sim/Não): ");
                String confirmacaoPagamento = scanner.nextLine();
                if (confirmacaoPagamento.equalsIgnoreCase("Sim")) {
                    if (pedido.getValorRecebido() >= pedido.calcularTotal()) {
                        pedido.setStatus("Pedido Faturado. Aguardando montagem");
                        PedidoDAO.getInstance().atualizarPedido(pedido);
                        System.out.println("Status do pedido atualizado para 'Pedido Faturado. Aguardando montagem'.");
                    } else {
                        System.out.println("O pagamento total ainda não foi recebido.");
                    }
                }
                break;
            case "Pedido Faturado. Aguardando montagem":
                System.out.print("Deseja seguir com a entrega? (Sim/Não): ");
                String confirmacaoMontagem = scanner.nextLine();
                if (confirmacaoMontagem.equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Aguardando entrega");
                    PedidoDAO.getInstance().atualizarPedido(pedido);
                    System.out.println("Status do pedido atualizado para 'Aguardando entrega'.");
                }
                break;
            case "Aguardando entrega":
                System.out.print("Deseja confirmar saída do pedido? (Sim/Não): ");
                String confirmacaoSaida = scanner.nextLine();
                if (confirmacaoSaida.equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Pedido a caminho");
                    PedidoDAO.getInstance().atualizarPedido(pedido);
                    System.out.println("Status do pedido atualizado para 'Pedido a caminho'.");
                }
                break;
            case "Pedido a caminho":
                System.out.print("Deseja confirmar entrega do pedido? (Sim/Não): ");
                String confirmacaoEntrega = scanner.nextLine();
                if (confirmacaoEntrega.equalsIgnoreCase("Sim")) {
                    pedido.setStatus("Entrega concluída. Pedido Finalizado");
                    PedidoDAO.getInstance().atualizarPedido(pedido);
                    System.out.println("Status do pedido atualizado para 'Entrega concluída. Pedido Finalizado'.");
                }
                break;
            default:
                System.out.println("Status desconhecido.");
                break;
        }
    }

    public void adicionarPagamento(int numeroPedido, Scanner scanner) throws SQLException, PedidoNaoEncontradoException {
        Pedido pedido = PedidoDAO.getInstance().buscarPedidoPorCodigo(numeroPedido);
        System.out.println("Valor total do pedido: R$ " + pedido.calcularTotal());
        System.out.println("Valor já recebido: R$ " + pedido.getValorRecebido());

        System.out.print("Digite o valor a ser adicionado ao pagamento: ");
        double valorPagamento = Double.parseDouble(scanner.nextLine());

        if (valorPagamento <= 0) {
            System.out.println("O valor do pagamento deve ser maior que 0.");
        } else if (valorPagamento + pedido.getValorRecebido() > pedido.calcularTotal()) {
            System.out.println("O valor excede o total do pedido.");
        } else {
            pedido.setValorRecebido(pedido.getValorRecebido() + valorPagamento);
            if (pedido.getValorRecebido() >= pedido.calcularTotal()) {
                pedido.setStatusPagamento("Pago");
            }
            PedidoDAO.getInstance().atualizarPedido(pedido);
            System.out.println("Pagamento adicionado com sucesso.");
        }
    }

    public void exibirDetalhesPedido(Pedido pedido, Scanner scanner) throws SQLException, PedidoNaoEncontradoException {
        pedido.exibirResumo();

        System.out.println("O que deseja fazer?");
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
                cancelarPedido(pedido.getCodigo(), scanner);
                break;
            case 3:
                adicionarPagamento(pedido.getCodigo(), scanner);
                break;
            case 4:
                System.out.println("Voltando ao menu anterior...");
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    public void cancelarPedido(int codigoPedido, Scanner scanner) throws SQLException, PedidoNaoEncontradoException {
        Pedido pedido = PedidoDAO.getInstance().buscarPedidoPorCodigo(codigoPedido);
        if (pedido.getStatus().equals("Pedido Faturado. Aguardando montagem") || pedido.getStatus().equals("Aguardando entrega") || pedido.getStatus().equals("Pedido a caminho")) {
            System.out.println("Não é possível cancelar um pedido já faturado.");
            return;
        }

        for (Map.Entry<Produto, Integer> entry : pedido.getProdutos().entrySet()) {
            Produto produto = entry.getKey();
            int quantidade = entry.getValue();
            produto.setEstoque(produto.getEstoque() + quantidade);
            ProdutoDAO.getInstance().atualizarProduto(produto);
        }

        pedido.setStatus("Cancelado");
        PedidoDAO.getInstance().atualizarPedido(pedido);
        System.out.println("Pedido cancelado com sucesso.");
    }
}
