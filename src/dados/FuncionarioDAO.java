package dados;

import excecoes.FuncionarioNaoEncontradoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    private static FuncionarioDAO instance;

    private FuncionarioDAO() {
        // Construtor privado para o padrão Singleton
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
        String sql = "SELECT MAX(codigo_func) AS ultimo_codigo FROM funcionarios";
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

    public Funcionario buscarFuncionarioPorCodigo(int codigo) {
        String sql = "SELECT codigo, nome, cpf, tipo, salario, ativo FROM funcionarios WHERE codigo = ?";
        Funcionario funcionario = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                funcionario = new Funcionario(
                        rs.getInt("codigo"),  // Código do funcionário como String
                        rs.getString("nome"),                 // Nome do funcionário
                        rs.getString("cpf"),                  // CPF do funcionário
                        rs.getString("tipo"),                 // Tipo do funcionário
                        rs.getDouble("salario"),              // Salário do funcionário
                        rs.getBoolean("ativo")                // Status ativo do funcionário
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionario;
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
        String sql = "UPDATE funcionarios SET nome = ?, cpf = ?, tipo = ?, salario = ?, ativo = ? WHERE codigo = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getTipo());
            stmt.setDouble(4, funcionario.getSalario());
            stmt.setBoolean(5, funcionario.isAtivo());
            stmt.setInt(6, funcionario.getCodigoFunc());

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
