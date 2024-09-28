package dados;

import excecoes.ClienteNaoEncontradoException;
import excecoes.FuncionarioNaoEncontradoException;
import excecoes.PedidoNaoEncontradoException;
import excecoes.ProdutoNaoEncontradoException;

import java.sql.*;
import java.util.*;
import java.util.Date;

import static dados.ConexaoDB.getConnection;


public class PedidoDAO {
    private static PedidoDAO instance;
    private Connection connection;

    private PedidoDAO() throws SQLException {
        this.connection = ConexaoDB.getConnection();
    }

    public static PedidoDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new PedidoDAO();
        }
        return instance;
    }

    // Método para inserir um novo pedido
    public void inserirPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (cliente_cpf, funcionario_codigo, endereco, forma_pagamento, data_hora, data_vencimento, status_pagamento, valor_recebido) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pedido.getCliente().getCpf());  // Obtém o CPF do cliente
            stmt.setInt(2, pedido.getFuncionario().getCodigoFunc());  // Obtém o código do funcionário
            stmt.setString(3, pedido.getEndereco());
            stmt.setString(4, pedido.getFormaPagamento());
            stmt.setDate(5, new java.sql.Date(pedido.getDataCriacao().getTime()));
            stmt.setDate(6, new java.sql.Date(pedido.getDataVencimento().getTime()));
            stmt.setString(7, pedido.getStatusPagamento());  // Status do pagamento
            stmt.setDouble(8, pedido.getValorRecebido());  // Valor recebido

            stmt.executeUpdate();
        }
    }

    // Método para buscar um pedido por código
    public Pedido buscarPedidoPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE codigo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = ClienteDAO.getInstance().buscarClientePorCpf(rs.getString("cliente_cpf"));
                Funcionario funcionario = FuncionarioDAO.getInstance().buscarFuncionarioPorCodigo(rs.getInt("funcionario_codigo"));
                String endereco = rs.getString("endereco");
                String formaPagamento = rs.getString("forma_pagamento");
                Map<Produto, Integer> produtos = buscarProdutosPorPedido(codigo);

                Pedido pedido = new Pedido(cliente, funcionario, endereco, produtos, formaPagamento);
                pedido.setDataCriacao(rs.getDate("data_criacao"));
                pedido.setDataVencimento(rs.getDate("data_vencimento"));
                pedido.setStatusPagamento(rs.getString("status_pagamento"));
                pedido.setValorRecebido(rs.getDouble("valor_recebido"));
                return pedido;
            }
        }
        return null;
    }

    // Método para inserir os produtos de um pedido
    private void inserirProdutosPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos_produtos (pedido_id, produto_codigo, quantidade) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Map.Entry<Produto, Integer> entry : pedido.getProdutos().entrySet()) {
                stmt.setInt(1, pedido.getCodigo());
                stmt.setInt(2, entry.getKey().getCodigo());
                stmt.setInt(3, entry.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    // Método para buscar os produtos de um pedido
    private Map<Produto, Integer> buscarProdutosPorPedido(int codigoPedido) throws SQLException {
        String sql = "SELECT * FROM pedidos_produtos WHERE pedido_id = ?";
        Map<Produto, Integer> produtos = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = ProdutoDAO.getInstance().buscarProdutoPorCodigo(rs.getInt("produto_codigo"));
                int quantidade = rs.getInt("quantidade");
                produtos.put(produto, quantidade);
            }
        }
        return produtos;
    }

    // Método para listar todos os pedidos
    public List<Pedido> listarPedidos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();


            ClienteDAO clienteDAO = new ClienteDAO();
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setCodigo(rs.getInt("codigo"));

                // Buscar o cliente pelo CPF
                String cpfCliente = rs.getString("cliente_cpf");
                Cliente cliente = clienteDAO.buscarClientePorCpf(cpfCliente);  // Método de instância
                pedido.setCliente(cliente);

                // Buscar o funcionário pelo código
                int codFuncionario = rs.getInt("funcionario_codigo");
                Funcionario funcionario = funcionarioDAO.buscarFuncionarioPorCodigo(codFuncionario);  // Método de instância
                pedido.setFuncionario(funcionario);

                pedido.setEndereco(rs.getString("endereco"));
                pedido.setFormaPagamento(rs.getString("forma_pagamento"));
                pedido.setDataCriacao(rs.getDate("data_hora"));
                pedido.setDataVencimento(rs.getDate("data_vencimento"));
                pedido.setStatus(rs.getString("status"));
                pedido.setValorRecebido(rs.getDouble("valor_recebido"));

                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    // Método para atualizar um pedido
    public void atualizarPedido(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET status = ? WHERE codigo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pedido.getStatus());
            stmt.setInt(2, pedido.getCodigo());
            stmt.executeUpdate();
        }
    }
}

