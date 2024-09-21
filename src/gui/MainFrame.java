package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    private JButton btnGerenciarClientes;
    private JButton btnGerenciarFuncionarios;
    private JButton btnGerenciarProdutos;
    private JButton btnGerenciarPedidos;

    public MainFrame() {
        setTitle("Sistema de Loja de Bebidas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Botão Gerenciar Clientes
        btnGerenciarClientes = new JButton("Gerenciar Clientes");
        btnGerenciarClientes.setBounds(100, 50, 200, 30);
        btnGerenciarClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ClienteFrame().setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(btnGerenciarClientes);

        // Botão Gerenciar Funcionários
        btnGerenciarFuncionarios = new JButton("Gerenciar Funcionários");
        btnGerenciarFuncionarios.setBounds(100, 100, 200, 30);
        btnGerenciarFuncionarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FuncionarioFrame().setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(btnGerenciarFuncionarios);

        // Botão Gerenciar Produtos
        btnGerenciarProdutos = new JButton("Gerenciar Produtos");
        btnGerenciarProdutos.setBounds(100, 150, 200, 30);
        btnGerenciarProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ProdutoFrame().setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(btnGerenciarProdutos);

        // Botão Gerenciar Pedidos
        btnGerenciarPedidos = new JButton("Gerenciar Pedidos");
        btnGerenciarPedidos.setBounds(100, 200, 200, 30);
        btnGerenciarPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new PedidoFrame().setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(btnGerenciarPedidos);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
