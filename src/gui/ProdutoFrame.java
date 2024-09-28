package gui;

import dados.Produto;
import excecoes.ProdutoJaExisteException;
import excecoes.ProdutoNaoEncontradoException;
import negocio.Fachada;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ProdutoFrame extends JFrame {
    private JTextField txtCodigoProduto;
    private JButton btnCadastrarProduto, btnListarProdutos, btnBuscarProduto, btnAtualizarProduto, btnDeletarProduto;
    private Fachada fachada;

    public ProdutoFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Produtos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Campo para o código do produto (para busca, atualização e deleção)
        JLabel lblCodigoProduto = new JLabel("Código do Produto:");
        lblCodigoProduto.setBounds(30, 30, 150, 25);
        add(lblCodigoProduto);

        txtCodigoProduto = new JTextField();
        txtCodigoProduto.setBounds(180, 30, 150, 25);
        add(txtCodigoProduto);

        // Botões para ações
        btnCadastrarProduto = new JButton("Cadastrar Produto");
        btnCadastrarProduto.setBounds(30, 70, 150, 30);
        btnCadastrarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaCadastroProduto();
            }
        });
        add(btnCadastrarProduto);

        btnListarProdutos = new JButton("Listar Produtos");
        btnListarProdutos.setBounds(200, 70, 150, 30);
        btnListarProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProdutos();
            }
        });
        add(btnListarProdutos);

        btnBuscarProduto = new JButton("Buscar Produto");
        btnBuscarProduto.setBounds(30, 110, 150, 30);
        btnBuscarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProduto();
            }
        });
        add(btnBuscarProduto);

        btnAtualizarProduto = new JButton("Atualizar Produto");
        btnAtualizarProduto.setBounds(200, 110, 150, 30);
        btnAtualizarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaAtualizarProduto();
            }
        });
        add(btnAtualizarProduto);

        btnDeletarProduto = new JButton("Deletar Produto");
        btnDeletarProduto.setBounds(30, 150, 150, 30);
        btnDeletarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarProduto();
            }
        });
        add(btnDeletarProduto);
    }

    // Método para abrir a tela de cadastro de produto
    private void abrirTelaCadastroProduto() {
        JFrame cadastroFrame = new JFrame("Cadastrar Produto");
        cadastroFrame.setSize(400, 300);
        cadastroFrame.setLayout(null);
        cadastroFrame.setLocationRelativeTo(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        cadastroFrame.add(lblNome);

        JTextField txtNome = new JTextField();
        txtNome.setBounds(150, 30, 200, 25);
        cadastroFrame.add(txtNome);

        JLabel lblPreco = new JLabel("Preço:");
        lblPreco.setBounds(30, 70, 100, 25);
        cadastroFrame.add(lblPreco);

        JTextField txtPreco = new JTextField();
        txtPreco.setBounds(150, 70, 200, 25);
        cadastroFrame.add(txtPreco);

        JLabel lblEstoque = new JLabel("Estoque:");
        lblEstoque.setBounds(30, 110, 100, 25);
        cadastroFrame.add(lblEstoque);

        JTextField txtEstoque = new JTextField();
        txtEstoque.setBounds(150, 110, 200, 25);
        cadastroFrame.add(txtEstoque);

        JButton btnEfetuarCadastro = new JButton("Efetuar Cadastro");
        btnEfetuarCadastro.setBounds(150, 160, 150, 30);
        btnEfetuarCadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = txtNome.getText();
                    float preco = Float.parseFloat(txtPreco.getText());
                    int estoque = Integer.parseInt(txtEstoque.getText());
                    fachada.cadastrarProduto(nome, preco, estoque);
                    JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
                } catch (SQLException | ProdutoJaExisteException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + ex.getMessage());
                }
            }
        });
        cadastroFrame.add(btnEfetuarCadastro);

        cadastroFrame.setVisible(true);
    }

    // Método para abrir a tela de atualização de produto
    private void abrirTelaAtualizarProduto() {
        String codigo = txtCodigoProduto.getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o código do produto!");
            return;
        }

        try {
            Produto produto = fachada.buscarProduto(Integer.parseInt(codigo));
            if (produto == null) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado!");
                return;
            }

            JFrame atualizarFrame = new JFrame("Atualizar Produto");
            atualizarFrame.setSize(400, 300);
            atualizarFrame.setLayout(null);
            atualizarFrame.setLocationRelativeTo(null);

            JLabel lblNome = new JLabel("Nome:");
            lblNome.setBounds(30, 30, 100, 25);
            atualizarFrame.add(lblNome);

            JTextField txtNome = new JTextField(produto.getNome()); // Preenche com o nome atual
            txtNome.setBounds(150, 30, 200, 25);
            atualizarFrame.add(txtNome);

            JLabel lblPreco = new JLabel("Preço:");
            lblPreco.setBounds(30, 70, 100, 25);
            atualizarFrame.add(lblPreco);

            JTextField txtPreco = new JTextField(String.valueOf(produto.getPreco())); // Preenche com o preço atual
            txtPreco.setBounds(150, 70, 200, 25);
            atualizarFrame.add(txtPreco);

            JLabel lblEstoque = new JLabel("Estoque:");
            lblEstoque.setBounds(30, 110, 100, 25);
            atualizarFrame.add(lblEstoque);

            JTextField txtEstoque = new JTextField(String.valueOf(produto.getEstoque())); // Preenche com o estoque atual
            txtEstoque.setBounds(150, 110, 200, 25);
            atualizarFrame.add(txtEstoque);

            JButton btnConfirmar = new JButton("Confirmar Atualização");
            btnConfirmar.setBounds(150, 160, 200, 30);
            btnConfirmar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String nomeAtualizado = txtNome.getText();
                        float precoAtualizado = Float.parseFloat(txtPreco.getText());
                        int estoqueAtualizado = Integer.parseInt(txtEstoque.getText());

                        fachada.atualizarProduto(Integer.parseInt(codigo), nomeAtualizado, precoAtualizado, estoqueAtualizado);
                        JOptionPane.showMessageDialog(atualizarFrame, "Produto atualizado com sucesso!");
                        atualizarFrame.dispose();
                    } catch (SQLException | ProdutoNaoEncontradoException ex) {
                        JOptionPane.showMessageDialog(atualizarFrame, "Erro ao atualizar produto: " + ex.getMessage());
                    }
                }
            });
            atualizarFrame.add(btnConfirmar);

            atualizarFrame.setVisible(true);

        } catch (SQLException | ProdutoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produto: " + ex.getMessage());
        }
    }

    // Método para listar produtos diretamente na interface
    private void listarProdutos() {
        try {
            JTextArea textArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBounds(30, 180, 340, 80);
            add(scrollPane);
            revalidate();
            repaint();
            // Coloca os produtos listados na área de texto
            for (Produto produto : fachada.listarProdutos()) {
                textArea.append(produto.toString() + "\n");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar produtos: " + ex.getMessage());
        }
    }

    // Método para buscar produto
    private void buscarProduto() {
        String codigo = txtCodigoProduto.getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o código do produto!");
            return;
        }

        try {
            Produto produto = fachada.buscarProduto(Integer.parseInt(codigo));
            if (produto == null) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            } else {
                JOptionPane.showMessageDialog(this, produto.toString());
            }
        } catch (SQLException | ProdutoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produto: " + ex.getMessage());
        }
    }

    // Método para deletar produto
    private void deletarProduto() {
        String codigo = txtCodigoProduto.getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o código do produto!");
            return;
        }

        try {
            Produto produto = fachada.buscarProduto(Integer.parseInt(codigo));
            if (produto == null) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente deletar o produto: " + produto.getNome() + "?");
            if (confirmacao == JOptionPane.YES_OPTION) {
                fachada.deletarProduto(Integer.parseInt(codigo));
                JOptionPane.showMessageDialog(this, "Produto deletado com sucesso!");
            }
        } catch (SQLException | ProdutoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar produto: " + ex.getMessage());
        }
    }
}