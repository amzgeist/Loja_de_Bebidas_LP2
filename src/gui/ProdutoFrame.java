package gui;

import excecoes.ProdutoJaExisteException;
import excecoes.ProdutoNaoEncontradoException;
import negocio.Fachada;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ProdutoFrame extends JFrame {

    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtEstoque;
    private JTextField txtCodigo;
    private JButton btnCadastrarProduto;
    private JButton btnListarProdutos;
    private JButton btnBuscarProduto;
    private JButton btnAtualizarProduto;
    private JButton btnDeletarProduto;

    private Fachada fachada;

    public ProdutoFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Produtos");
        setSize(400, 400);
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

        // Código
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(30, 170, 100, 25);
        add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(150, 170, 200, 25);
        add(txtCodigo);

        // Botão Cadastrar Produto
        btnCadastrarProduto = new JButton("Cadastrar Produto");
        btnCadastrarProduto.setBounds(50, 220, 150, 30);
        btnCadastrarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                float preco = Float.parseFloat(txtPreco.getText());
                int estoque = Integer.parseInt(txtEstoque.getText());

                try {
                    fachada.cadastrarProduto(nome, preco, estoque);
                    JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + ex.getMessage());
                } catch (ProdutoJaExisteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(btnCadastrarProduto);

        // Botão Listar Produtos
        btnListarProdutos = new JButton("Listar Produtos");
        btnListarProdutos.setBounds(210, 220, 150, 30);
        btnListarProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fachada.listarProdutos();
                    JOptionPane.showMessageDialog(null, "Produtos listados com sucesso!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + ex.getMessage());
                }
            }
        });
        add(btnListarProdutos);

        // Botão Buscar Produto
        btnBuscarProduto = new JButton("Buscar Produto");
        btnBuscarProduto.setBounds(50, 260, 150, 30);
        btnBuscarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigo = Integer.parseInt(txtCodigo.getText());
                try {
                    String produtoInfo = null;
                    try {
                        produtoInfo = fachada.buscarProduto(codigo);
                    } catch (ProdutoNaoEncontradoException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (produtoInfo != null) {
                        JOptionPane.showMessageDialog(null, produtoInfo);
                    } else {
                        JOptionPane.showMessageDialog(null, "Produto não encontrado.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao buscar produto: " + ex.getMessage());
                }
            }
        });
        add(btnBuscarProduto);

        // Botão Atualizar Produto
        btnAtualizarProduto = new JButton("Atualizar Produto");
        btnAtualizarProduto.setBounds(210, 260, 150, 30);
        btnAtualizarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigo = Integer.parseInt(txtCodigo.getText());
                String nome = txtNome.getText();
                float preco = Float.parseFloat(txtPreco.getText());
                int estoque = Integer.parseInt(txtEstoque.getText());

                try {
                    fachada.atualizarProduto(codigo, nome, preco, estoque);
                    JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!");
                } catch (SQLException | ProdutoNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar produto: " + ex.getMessage());
                }
            }
        });
        add(btnAtualizarProduto);

        // Botão Deletar Produto
        btnDeletarProduto = new JButton("Deletar Produto");
        btnDeletarProduto.setBounds(130, 300, 150, 30);
        btnDeletarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigo = Integer.parseInt(txtCodigo.getText());
                try {
                    fachada.deletarProduto(codigo);
                    JOptionPane.showMessageDialog(null, "Produto deletado com sucesso!");
                } catch (SQLException | ProdutoNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao deletar produto: " + ex.getMessage());
                }
            }
        });
        add(btnDeletarProduto);
    }
}