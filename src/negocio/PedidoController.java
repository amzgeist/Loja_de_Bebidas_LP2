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


    public static PedidoController getInstance() throws SQLException {
        if (instance == null) {
            instance = new PedidoController();
        }
        return instance;
    }

    private PedidoDAO pedidoDAO;
    private ProdutoDAO produtoDAO;
    private FuncionarioDAO funcionarioDAO;
    private ClienteDAO clienteDAO;

    public PedidoController() throws SQLException {
        this.pedidoDAO = PedidoDAO.getInstance();
        this.produtoDAO = ProdutoDAO.getInstance();
        this.funcionarioDAO = FuncionarioDAO.getInstance();
        this.clienteDAO = ClienteDAO.getInstance();
    }

    // Método para criar um pedido
    public void criarPedido(String cpfCliente, int codFuncionario, String endereco, Map<Integer, Integer> produtos, String formaPagamento) throws SQLException, ProdutoNaoEncontradoException, FuncionarioNaoEncontradoException {
        Pedido pedido = new Pedido();

        // Buscar cliente e funcionário pelo código ou CPF
        Cliente cliente = clienteDAO.buscarClientePorCpf(cpfCliente);
        Funcionario funcionario = funcionarioDAO.buscarFuncionarioPorCodigo(codFuncionario);

        // Verifica se cliente e funcionário existem
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente com CPF " + cpfCliente + " não encontrado.");
        }
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(codFuncionario);  // Passa o int diretamente
        }

        // Definir os valores do pedido
        pedido.setCliente(cliente);  // Usa o objeto cliente
        pedido.setFuncionario(funcionario);  // Usa o objeto funcionário
        pedido.setEndereco(endereco);
        pedido.setFormaPagamento(formaPagamento);

        // Adicionar os produtos ao pedido
        for (Map.Entry<Integer, Integer> entry : produtos.entrySet()) {
            int codigoProduto = entry.getKey();
            int quantidade = entry.getValue();

            Produto produto = produtoDAO.buscarProdutoPorCodigo(codigoProduto);
            if (produto != null) {
                pedido.adicionarProduto(produto, quantidade);
            } else {
                throw new ProdutoNaoEncontradoException("Produto com código " + codigoProduto + " não encontrado.");
            }
        }

        // Inserir o pedido no banco de dados
        pedidoDAO.inserirPedido(pedido);
    }

    // Método para buscar um pedido por código
    public Pedido buscarPedidoPorCodigo(int codigo) throws SQLException, PedidoNaoEncontradoException {
        Pedido pedido = pedidoDAO.buscarPedidoPorCodigo(codigo);

        if (pedido == null) {
            throw new PedidoNaoEncontradoException("Pedido com o código " + codigo + " não encontrado.");
        }

        return pedido;
    }

    // Método para listar pedidos
    public void listarPedidos() throws SQLException {
        pedidoDAO.listarPedidos();
    }

    // Método para atualizar o status de um pedido
    public void atualizarStatusPedido(int codigoPedido, String novoStatus) throws SQLException, PedidoNaoEncontradoException {
        Pedido pedido = buscarPedidoPorCodigo(codigoPedido);
        pedido.setStatus(novoStatus);
        pedidoDAO.atualizarPedido(pedido);
    }

    // Método para adicionar pagamento ao pedido
    public void adicionarPagamento(int codigoPedido, double valor) throws SQLException, PedidoNaoEncontradoException {
        Pedido pedido = buscarPedidoPorCodigo(codigoPedido);
        pedido.setValorRecebido(pedido.getValorRecebido() + valor);

        if (pedido.getValorRecebido() >= pedido.calcularTotal()) {
            pedido.setStatusPagamento("Pago");
        }

        pedidoDAO.atualizarPedido(pedido);
    }

    // Método para cancelar um pedido
    public void cancelarPedido(int codigoPedido) throws SQLException, PedidoNaoEncontradoException {
        Pedido pedido = buscarPedidoPorCodigo(codigoPedido);
        pedido.setStatus("Cancelado");
        pedidoDAO.atualizarPedido(pedido);
    }
}

