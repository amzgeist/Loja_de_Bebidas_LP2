package dados;

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

    public Funcionario buscarFuncionarioPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM funcionarios WHERE codigo = ?";
        Funcionario funcionario = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                funcionario = new Funcionario(rs.getString("nome"), rs.getString("cpf"),
                        rs.getString("tipo"), rs.getDouble("salario"));
                funcionario.setAtivo(rs.getBoolean("ativo"));
                funcionario.setCodigoFunc(rs.getInt("codigo"));
            }
        }

        return funcionario;
    }

    public List<Funcionario> listarFuncionarios() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT codigo, nome, cpf, tipo, salario, ativo FROM funcionarios";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("codigo");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String tipo = rs.getString("tipo");
                double salario = rs.getDouble("salario");
                boolean ativo = rs.getBoolean("ativo");

                Funcionario funcionario = new Funcionario(nome, cpf, tipo, salario);
                funcionario.setCodigoFunc(id);  // Ajuste para usar `id` como `codigo`
                funcionario.setAtivo(ativo);

                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erro ao listar funcionários", e);
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

    public void reativarFuncionario(int codigo) throws SQLException {
        String sql = "UPDATE funcionarios SET ativo = true WHERE codigo = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            stmt.executeUpdate();
        }
    }
}
