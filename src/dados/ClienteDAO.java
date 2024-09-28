package dados;

import excecoes.ClienteNaoEncontradoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClienteDAO {
    private static ClienteDAO instance;
    private Connection connection;

    ClienteDAO() throws SQLException {
        this.connection = ConexaoDB.getConnection();
    }

    public static ClienteDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new ClienteDAO();
        }
        return instance;
    }

    public void cadastrarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, cpf, data_nascimento) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setDate(3, new java.sql.Date(cliente.getDataNascimento().getTime()));
            stmt.executeUpdate();
        }
    }

    public Cliente buscarClientePorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    Date dataNascimento = rs.getDate("data_nascimento");
                    return new Cliente(nome, cpf, dataNascimento);
                }
            }
        }
        return null;
    }

    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                java.util.Date dataNascimento = new java.util.Date(rs.getDate("data_nascimento").getTime());
                clientes.add(new Cliente(nome, cpf, dataNascimento));
            }
        }
        return clientes;
    }

    public void deletarCliente(String cpf) throws SQLException {
        String sql = "DELETE FROM clientes WHERE cpf = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }

    public void atualizarCliente(String cpf, String nome, Date dataNascimento) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, data_nascimento = ? WHERE cpf = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setDate(2, new java.sql.Date(dataNascimento.getTime()));
            stmt.setString(3, cpf);
            stmt.executeUpdate();
        }
    }


}
