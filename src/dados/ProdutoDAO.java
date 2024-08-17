package dados;

import excecoes.ProdutoNaoEncontradoException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private static ProdutoDAO instance;
    private Connection connection;

    private ProdutoDAO() {
        try {
            connection = ConexaoDB.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ProdutoDAO getInstance() {
        if (instance == null) {
            instance = new ProdutoDAO();
        }
        return instance;
    }

    public Produto buscarProduto(int codigo) throws ProdutoNaoEncontradoException {
        String sql = "SELECT * FROM produtos WHERE codigo = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    Float preco = rs.getFloat("preco");
                    int estoque = rs.getInt("estoque");

                    return new Produto(codigo, nome, preco, estoque);
                } else {
                    throw new ProdutoNaoEncontradoException("Produto com código " + codigo + " não encontrado.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o produto: " + e.getMessage(), e);
        }
    }

    public void inserirProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setFloat(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.executeUpdate();
        }
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        Connection connection = ConexaoDB.getConnection();
        String sql = "UPDATE produtos SET nome = ?, preco = ?, estoque = ? WHERE codigo = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, produto.getNome());
        statement.setFloat(2, produto.getPreco());
        statement.setInt(3, produto.getEstoque());
        statement.setInt(4, produto.getCodigo());
        statement.executeUpdate();
    }

    public void deletarProduto(int codigo) throws SQLException {
        String query = "DELETE FROM produtos WHERE codigo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getFloat("preco"),
                        rs.getInt("estoque")
                );
                produtos.add(produto);
            }
        }
        return produtos;
    }
}
