package gui;

import negocio.Fachada;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdicionarProdutosFrame extends JFrame {

    private JTextField txtCodigoProduto;
    private JTextField txtQuantidadeProduto;
    private JButton btnAdicionarProduto;
    private JButton btnCancelar;
    private JButton btnProsseguir;
    private Map<Integer, Integer> produtos;
    private Fachada fachada;

    public AdicionarProdutosFrame(Fachada fachada) {
        this.fachada = fachada;
        this.produtos = new HashMap<>();

        setTitle("Adicionar Produtos ao Pedido");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblCodigoProduto = new JLabel("Código do Produto:");
        lblCodigoProduto.setBounds(30, 30, 150, 25);
        add(lblCodigoProduto);

        txtCodigoProduto = new JTextField();
        txtCodigoProduto.setBounds(180, 30, 150, 25);
        add(txtCodigoProduto);

        JLabel lblQuantidadeProduto = new JLabel("Quantidade:");
        lblQuantidadeProduto.setBounds(30, 70, 150, 25);
        add(lblQuantidadeProduto);

        txtQuantidadeProduto = new JTextField();
        txtQuantidadeProduto.setBounds(180, 70, 150, 25);
        add(txtQuantidadeProduto);

        // Botão Adicionar Produto
        btnAdicionarProduto = new JButton("Adicionar Produto");
        btnAdicionarProduto.setBounds(30, 120, 150, 30);
        btnAdicionarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarProduto();
            }
        });
        add(btnAdicionarProduto);

        // Botão Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(30, 170, 150, 30);
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(btnCancelar);

        // Botão Prosseguir
        btnProsseguir = new JButton("Prosseguir");
        btnProsseguir.setBounds(180, 170, 150, 30);
        btnProsseguir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaCriarPedido();
            }
        });
        add(btnProsseguir);

        setVisible(true);
    }

    private void adicionarProduto() {
        int codigoProduto = Integer.parseInt(txtCodigoProduto.getText());
        int quantidade = Integer.parseInt(txtQuantidadeProduto.getText());
        produtos.put(codigoProduto, quantidade); // Adiciona o produto e a quantidade ao pedido
        JOptionPane.showMessageDialog(this, "Produto adicionado ao pedido.");
        txtCodigoProduto.setText("");
        txtQuantidadeProduto.setText("");
    }

    private void abrirTelaCriarPedido() {
        new CriarPedidoFrame(fachada, produtos); // Passa os produtos adicionados para a próxima tela
        dispose();
    }
}