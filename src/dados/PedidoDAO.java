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

    private PedidoDAO() {}

    public static PedidoDAO getInstance() {
        if (instance == null) {
            instance = new PedidoDAO();
        }
        return instance;
    }

    public void inserirPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (cliente_cpf, funcionario_codigo, endereco, forma_pagamento, data_hora, data_vencimento) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pedido.getCliente().getCpf()); // Usando CPF como identificador
            stmt.setInt(2, pedido.getFuncionario().getCodigoFunc()); // Código do funcionário
            stmt.setString(3, pedido.getEndereco());
            stmt.setString(4, pedido.getFormaPagamento());
            stmt.setTimestamp(5, new Timestamp(pedido.getDataHora().getTime()));
            stmt.setDate(6, new java.sql.Date(pedido.getDataVencimento().getTime()));

            stmt.executeUpdate();

            // Recupera o código do pedido gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int pedidoId = generatedKeys.getInt(1);

                    // Chama o método para inserir os produtos relacionados ao pedido
                    inserirProdutosPedido(pedidoId, pedido.getProdutos());
                }
            }
        }
    }

    public void criarPedido(String cpfCliente, int codFuncionario, String endereco, Map<Integer, Integer> produtos, String formaPagamento)
            throws SQLException, ClienteNaoEncontradoException, FuncionarioNaoEncontradoException, ProdutoNaoEncontradoException {

        // Implementação da lógica para inserir o pedido no banco de dados, verificando se o cliente, funcionário e produtos existem
        String sqlPedido = "INSERT INTO pedidos (cliente_cpf, funcionario_codigo, endereco, forma_pagamento) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {

            // Insere o pedido no banco de dados
            stmt.setString(1, cpfCliente);
            stmt.setInt(2, codFuncionario);
            stmt.setString(3, endereco);
            stmt.setString(4, formaPagamento);
            stmt.executeUpdate();

            // Recupera o ID gerado do pedido
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int pedidoId = generatedKeys.getInt(1);

                // Insere os produtos relacionados ao pedido
                for (Map.Entry<Integer, Integer> entry : produtos.entrySet()) {
                    int produtoCodigo = entry.getKey();
                    int quantidade = entry.getValue();

                    // Chama o método que insere os produtos no pedido
                    inserirProdutosPedido(pedidoId, produtoCodigo, quantidade);
                }
            } else {
                throw new SQLException("Falha ao criar o pedido, nenhum ID foi retornado.");
            }
        }
    }

    public void inserirProdutosPedido(int pedidoId, Map<Produto, Integer> produtos) throws SQLException {
        String sql = "INSERT INTO pedidos_produtos (pedido_id, produto_codigo, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
                Produto produto = entry.getKey();
                int quantidade = entry.getValue();

                stmt.setInt(1, pedidoId); // ID do pedido
                stmt.setInt(2, produto.getCodigo()); // Código do produto
                stmt.setInt(3, quantidade); // Quantidade

                stmt.addBatch(); // Adiciona ao batch para executar todos de uma vez
            }

            stmt.executeBatch(); // Executa todas as inserções
        }
    }

    private void inserirProdutosPedido(int pedidoId, int produtoCodigo, int quantidade) throws SQLException {
        String sql = "INSERT INTO pedidos_produtos (pedido_id, produto_codigo, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);          // O ID do pedido
            stmt.setInt(2, produtoCodigo);     // O código do produto
            stmt.setInt(3, quantidade);        // A quantidade de produtos no pedido
            stmt.executeUpdate();
        }
    }

    public Pedido buscarPedidoPorCodigo(int codigoPedido) throws PedidoNaoEncontradoException {
        Pedido pedido = null;
        String sql = "SELECT p.codigo, p.endereco, p.forma_pagamento, p.status, p.data_hora, p.data_vencimento, " +
                "c.nome AS cliente_nome, c.cpf AS cliente_cpf, " +
                "f.codigo AS funcionario_codigo, f.nome AS funcionario_nome, f.cpf AS funcionario_cpf, f.tipo AS funcionario_tipo, f.salario AS funcionario_salario, f.ativo AS funcionario_ativo " +
                "FROM pedidos p " +
                "JOIN clientes c ON p.cliente_cpf = c.cpf " +
                "JOIN funcionarios f ON p.funcionario_codigo = f.codigo " +
                "WHERE p.codigo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String endereco = rs.getString("endereco");
                    String formaPagamento = rs.getString("forma_pagamento");
                    String status = rs.getString("status");
                    Date dataCriacao = rs.getDate("data_hora");
                    Date dataVencimento = rs.getDate("data_vencimento");

                    Cliente cliente = new Cliente(rs.getString("cliente_nome"), rs.getString("cliente_cpf"), null);

                    Funcionario funcionario = new Funcionario(
                            rs.getInt("funcionario_codigo"),
                            rs.getString("funcionario_nome"),
                            rs.getString("funcionario_cpf"),
                            rs.getString("funcionario_tipo"),
                            rs.getDouble("funcionario_salario"),
                            rs.getBoolean("funcionario_ativo")
                    );

                    Map<Produto, Integer> produtos = buscarProdutosPorPedido(conn, codigoPedido);

                    pedido = new Pedido(cliente, funcionario, endereco, produtos, formaPagamento);
                    pedido.setDataHora(dataCriacao);
                    pedido.setDataVencimento(dataVencimento);
                } else {
                    throw new PedidoNaoEncontradoException(String.valueOf(codigoPedido));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedido;
    }



    private Map<Produto, Integer> buscarProdutosPorPedido(Connection conn, int codigoPedido) {
        String sql = "SELECT pp.produto_codigo, pp.quantidade, pr.nome, pr.preco, pr.estoque "
                + "FROM pedidos_produtos pp "
                + "JOIN produtos pr ON pp.produto_codigo = pr.codigo "
                + "WHERE pp.pedido_id = ?";

        Map<Produto, Integer> produtos = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigoPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("produto_codigo"),
                        rs.getString("nome"),
                        rs.getFloat("preco"),
                        rs.getInt("estoque")
                );
                produto.setCodigo(rs.getInt("produto_codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco((float) rs.getDouble("preco"));
                produto.setEstoque(rs.getInt("estoque"));

                int quantidade = rs.getInt("quantidade");

                produtos.put(produto, quantidade);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos do pedido: " + e.getMessage());
        }

        return produtos;
    }

    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome AS cliente_nome, c.cpf AS cliente_cpf, c.data_nascimento AS data_nascimento, " +
                "f.codigo AS funcionario_codigo, f.nome AS funcionario_nome, f.cpf AS funcionario_cpf, " +
                "f.tipo AS funcionario_tipo, f.salario AS funcionario_salario, f.ativo AS funcionario_ativo " +
                "FROM pedidos p " +
                "JOIN clientes c ON p.cliente_cpf = c.cpf " +
                "JOIN funcionarios f ON p.funcionario_codigo = f.codigo";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cliente_cpf"),
                        rs.getString("cliente_nome"),
                        rs.getDate("data_nascimento")
                );

                Funcionario funcionario = new Funcionario(
                        rs.getInt("funcionario_codigo"),
                        rs.getString("funcionario_nome"),
                        rs.getString("funcionario_cpf"),
                        rs.getString("funcionario_tipo"),
                        rs.getDouble("funcionario_salario"),
                        rs.getBoolean("funcionario_ativo")
                );

                String endereco = rs.getString("endereco");
                Map<Produto, Integer> produtos = buscarProdutosPorPedido(conn, rs.getInt("codigo"));
                String formaPagamento = rs.getString("forma_pagamento");

                Pedido pedido = new Pedido(
                        cliente,
                        funcionario,
                        endereco,
                        produtos,
                        formaPagamento
                );

                pedido.setDataHora(rs.getTimestamp("data_hora"));
                pedido.setDataVencimento(rs.getDate("data_vencimento"));
                pedido.setValorRecebido(rs.getDouble("valor_recebido"));
                pedido.setStatus(rs.getString("status"));
                pedido.setStatusPagamento(rs.getString("status_pagamento"));

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }




    public int atualizarPedido(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET valor_recebido = ?, status_pagamento = ? WHERE codigo = ?";

        try (Connection conn = getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pedido.getStatus());
            stmt.setDouble(2, pedido.getValorRecebido());
            stmt.setInt(3, pedido.getCodigo());

            stmt.executeUpdate();
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected; // Retorna o número de linhas atualizadas
        }
    }
}
