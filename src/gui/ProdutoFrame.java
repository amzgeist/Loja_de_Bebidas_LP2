package gui;

import negocio.Fachada;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ProdutoFrame extends JFrame {

    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtEstoque;
    private JButton btnCadastrarProduto;
    private JButton btnListarProdutos;

    private Fachada fachada;

    public ProdutoFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Produtos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Nome
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 50, 100, 25);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(150, 50, 200, 25);
        add(txtNome);

        // Preço
        JLabel lblPreco = new JLabel("Preço:");
        lblPreco.setBounds(30, 90, 100, 25);
        add(lblPreco);

        txtPreco = new JTextField();
        txtPreco.setBounds(150, 90, 200, 25);
        add(txtPreco);

        // Estoque
        JLabel lblEstoque = new JLabel("Estoque:");
        lblEstoque.setBounds(30, 130, 100, 25);
        add(lblEstoque);

        txtEstoque = new JTextField();
        txtEstoque.setBounds(150, 130, 200, 25);
        add(txtEstoque);

        // Botão Cadastrar Produto
        btnCadastrarProduto = new JButton("Cadastrar Produto");
        btnCadastrarProduto.setBounds(50, 180, 150, 30);
        btnCadastrarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                float preco = Float.parseFloat(txtPreco.getText());
                int estoque = Integer.parseInt(txtEstoque.getText());

                // Correção da chamada de fachada
                fachada.cadastrarProduto(nome, preco, estoque);
                JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
            }
        });
        add(btnCadastrarProduto);

        // Botão Listar Produtos
        btnListarProdutos = new JButton("Listar Produtos");
        btnListarProdutos.setBounds(210, 180, 150, 30);
        btnListarProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String produtos = null;
                try {
                    fachada.listarProdutos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, produtos);
            }
        });
        add(btnListarProdutos);
    }
}
