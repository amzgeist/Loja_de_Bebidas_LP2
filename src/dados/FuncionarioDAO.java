package dados;

import excecoes.FuncionarioNaoEncontradoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    private static FuncionarioDAO instance;

    FuncionarioDAO() {

    }

    public static FuncionarioDAO getInstance() {
        if (instance == null) {
            instance = new FuncionarioDAO();
        }
        return instance;
    }

    public void inserirFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionarios (nome, cpf, tipo, salario) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getTipo());
            stmt.setDouble(4, funcionario.getSalario());

            stmt.executeUpdate();
        }
    }

    public String buscarUltimoCodigo() throws SQLException {
        String sql = "SELECT MAX(codigo) AS ultimo_codigo FROM funcionarios";
        String ultimoCodigo = "0"; // Código inicial, caso não haja funcionários ainda

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                ultimoCodigo = rs.getString("ultimo_codigo");
            }
        }

        return ultimoCodigo;
    }

    public Funcionario buscarFuncionarioPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM funcionarios WHERE codigo = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String tipo = rs.getString("tipo");
                double salario = rs.getDouble("salario");
                boolean ativo = rs.getBoolean("ativo");

                return new Funcionario(codigo, nome, cpf, tipo, salario, ativo);
            }
        }
        return null;
    }


    public List<Funcionario> listarFuncionarios() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();

        String sql = "SELECT codigo, nome, cpf, tipo, salario, ativo FROM funcionarios";
        try (Connection connection = ConexaoDB.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("tipo"),
                        rs.getDouble("salario"),
                        rs.getBoolean("ativo")
                );
                funcionarios.add(funcionario);
            }
        }

        return funcionarios;
    }

    public void atualizarFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionarios SET nome = ?, cpf = ?, tipo = ?, salario = ? WHERE codigo = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getTipo());
            stmt.setDouble(4, funcionario.getSalario());
            stmt.setInt(5, funcionario.getCodigoFunc());

            stmt.executeUpdate();
        }
    }

    public void desativarFuncionario(int codigo) throws SQLException {
        String sql = "UPDATE funcionarios SET ativo = false WHERE codigo = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            stmt.executeUpdate();
        }
    }

    public int gerarCodigoUnico() {
        String sql = "SELECT MAX(codigo) AS max_codigo FROM funcionarios";
        int novoCodigo = 1;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                novoCodigo = rs.getInt("max_codigo") + 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return novoCodigo;
    }

    public void reativarFuncionario(int codigo) throws SQLException {
        String sql = "UPDATE funcionarios SET ativo = true WHERE codigo = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            stmt.executeUpdate();
        }
    }
}
