package dados;

import excecoes.ProdutoNaoEncontradoException;

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

    public Produto buscarProdutoPorCodigo(int codigo) throws SQLException {
        Produto produto = null;
        String sql = "SELECT * FROM produtos WHERE codigo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto = new Produto(
                            rs.getString("nome"),
                            rs.getFloat("preco"),
                            rs.getInt("estoque")
                    );
                }
            }
        }
        return produto;
    }

    public boolean produtoExiste(String nome) throws SQLException {
        String query = "SELECT COUNT(*) FROM produtos WHERE nome = ?";
        try (Connection connection = ConexaoDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // Se houver um produto com o nome, retorna true
                }
            }
        }
        return false;  // Se nenhum produto foi encontrado com o nome, retorna false
    }

    public void inserirProduto(String nome, float preco, int estoque) throws SQLException {
        String query = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
        try (Connection connection = ConexaoDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setFloat(2, preco);
            stmt.setInt(3, estoque);
            stmt.executeUpdate();
        }
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, estoque = ? WHERE codigo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setFloat(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.setInt(4, produto.getCodigo());

            stmt.executeUpdate();
        }
    }

    public void deletarProduto(int codigo) throws SQLException {
        String sql = "DELETE FROM produtos WHERE codigo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }

    public void listarProdutos() throws SQLException {
        String query = "SELECT * FROM produtos";
        try (Connection connection = ConexaoDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("Código: " + rs.getInt("codigo"));
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("Preço: " + rs.getFloat("preco"));
                System.out.println("Estoque: " + rs.getInt("estoque"));
                System.out.println("----------------------------------");
            }
        }
    }
    }

