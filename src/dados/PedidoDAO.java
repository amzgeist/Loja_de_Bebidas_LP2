package dados;

import excecoes.ProdutoNaoEncontradoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class PedidoDAO {

    private static PedidoDAO instance;

    private PedidoDAO() {}

    public static PedidoDAO getInstance() {
        if (instance == null) {
            instance = new PedidoDAO();
        }
        return instance;
    }

    public void inserirPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (codigo, cliente_cpf, funcionario_codigo, endereco, forma_pagamento, status, data_hora, data_vencimento, valor_recebido, status_pagamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getCodigo());
            stmt.setString(2, pedido.getCliente().getCpf());
            stmt.setInt(3, pedido.getFuncionario().getCodigoFunc());
            stmt.setString(4, pedido.getEndereco());
            stmt.setString(5, pedido.getFormaPagamento());
            stmt.setString(6, pedido.getStatus());
            stmt.setTimestamp(7, new java.sql.Timestamp(pedido.getDataHora().getTime()));
            stmt.setTimestamp(8, new java.sql.Timestamp(pedido.getDataVencimento().getTime())); // Insert data_vencimento
            stmt.setDouble(9, pedido.getValorRecebido());
            stmt.setString(10, pedido.getStatusPagamento());
            stmt.executeUpdate();
        }
    }

    private void inserirProdutosPedido(Pedido pedido, Connection conn) throws SQLException {
        String sql = "INSERT INTO pedidos_produtos (pedido_id, produto_codigo, quantidade) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Map.Entry<Produto, Integer> entry : pedido.getProdutos().entrySet()) {
                stmt.setInt(1, pedido.getCodigo());
                stmt.setInt(2, entry.getKey().getCodigo());
                stmt.setInt(3, entry.getValue());
                stmt.executeUpdate();
            }
        }
    }

    public Pedido buscarPedidoPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE codigo = ?";
        Pedido pedido = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = ClienteDAO.getInstance().buscarClientePorCpf(rs.getString("cliente_cpf"));
                Funcionario funcionario = FuncionarioDAO.getInstance().buscarFuncionarioPorCodigo(rs.getInt("funcionario_codigo"));
                String endereco = rs.getString("endereco");
                String formaPagamento = rs.getString("forma_pagamento");
                String status = rs.getString("status");
                java.util.Date dataHora = new java.util.Date(rs.getTimestamp("data_hora").getTime());
                double valorRecebido = rs.getDouble("valor_recebido");

                Map<Produto, Integer> produtos = buscarProdutosPedido(codigo, conn);

                pedido = new Pedido(cliente, funcionario, endereco, produtos, formaPagamento);
                pedido.setStatus(status);
                pedido.setDataHora(dataHora);
                pedido.setValorRecebido(valorRecebido);
            }
        }
        return pedido;
    }

    private Map<Produto, Integer> buscarProdutosPedido(int codigoPedido, Connection conn) throws SQLException {
        String sql = "SELECT * FROM pedidos_produtos WHERE pedido_id = ?";
        Map<Produto, Integer> produtos = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigoPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = ProdutoDAO.getInstance().buscarProduto(rs.getInt("produto_codigo"));
                int quantidade = rs.getInt("quantidade");
                produtos.put(produto, quantidade);
            }
        } catch (ProdutoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public List<Pedido> listarPedidos() throws SQLException {
        String sql = "SELECT * FROM pedidos";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = buscarPedidoPorCodigo(rs.getInt("codigo"));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    public void atualizarPedido(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET status = ?, valor_recebido = ? WHERE codigo = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pedido.getStatus());
            stmt.setDouble(2, pedido.getValorRecebido());
            stmt.setInt(3, pedido.getCodigo());

            stmt.executeUpdate();
        }
    }
}
