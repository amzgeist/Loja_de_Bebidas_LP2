package dados;

import java.sql.*;

public class ConexaoDB {
    private static final String URL = "jdbc:postgresql://localhost:5433/loja_database"; // URL do banco de dados
    private static final String USER = "postgres"; // Usuário do banco de dados
    private static final String PASSWORD = "12345"; // Senha do usuário

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw e;
        }
    }



    public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o ResultSet: " + e.getMessage());
            }
        }
        closeConnection(conn, stmt, rs);
    }
}
