package gui;

import negocio.Fachada;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PedidoFrame extends JFrame {

    private JTextField txtCpfCliente;
    private JTextField txtCodigoFuncionario;
    private JTextField txtEndereco;
    private JTextField txtFormaPagamento;
    private JTextField txtCodigoProduto;
    private JTextField txtQuantidadeProduto;
    private JButton btnCriarPedido;
    private JButton btnListarPedidos;

    private Map<Integer, Integer> produtos = new HashMap<>();
    private Fachada fachada;

    public PedidoFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Pedidos");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // CPF do Cliente
        JLabel lblCpfCliente = new JLabel("CPF do Cliente:");
        lblCpfCliente.setBounds(30, 50, 100, 25);
        add(lblCpfCliente);

        txtCpfCliente = new JTextField();
        txtCpfCliente.setBounds(150, 50, 200, 25);
        add(txtCpfCliente);

        // Código do Funcionário
        JLabel lblCodigoFuncionario = new JLabel("Código do Funcionário:");
        lblCodigoFuncionario.setBounds(30, 90, 150, 25);
        add(lblCodigoFuncionario);

        txtCodigoFuncionario = new JTextField();
        txtCodigoFuncionario.setBounds(180, 90, 170, 25);
        add(txtCodigoFuncionario);

        // Endereço
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setBounds(30, 130, 100, 25);
        add(lblEndereco);

        txtEndereco = new JTextField();
        txtEndereco.setBounds(150, 130, 200, 25);
        add(txtEndereco);

        // Forma de Pagamento
        JLabel lblFormaPagamento = new JLabel("Forma de Pagamento:");
        lblFormaPagamento.setBounds(30, 170, 150, 25);
        add(lblFormaPagamento);

        txtFormaPagamento = new JTextField();
        txtFormaPagamento.setBounds(180, 170, 170, 25);
        add(txtFormaPagamento);

        // Código do Produto
        JLabel lblCodigoProduto = new JLabel("Código do Produto:");
        lblCodigoProduto.setBounds(30, 210, 150, 25);
        add(lblCodigoProduto);

        txtCodigoProduto = new JTextField();
        txtCodigoProduto.setBounds(180, 210, 170, 25);
        add(txtCodigoProduto);

        // Quantidade do Produto
        JLabel lblQuantidadeProduto = new JLabel("Quantidade:");
        lblQuantidadeProduto.setBounds(30, 250, 150, 25);
        add(lblQuantidadeProduto);

        txtQuantidadeProduto = new JTextField();
        txtQuantidadeProduto.setBounds(180, 250, 170, 25);
        add(txtQuantidadeProduto);

        // Botão Criar Pedido
        btnCriarPedido = new JButton("Criar Pedido");
        btnCriarPedido.setBounds(50, 300, 150, 30);
        btnCriarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpfCliente = txtCpfCliente.getText();
                int codFuncionario = Integer.parseInt(txtCodigoFuncionario.getText());
                String endereco = txtEndereco.getText();
                String formaPagamento = txtFormaPagamento.getText();

                int codProduto = Integer.parseInt(txtCodigoProduto.getText());
                int quantidade = Integer.parseInt(txtQuantidadeProduto.getText());

                produtos.put(codProduto, quantidade);
                try {
                    fachada.criarPedido(cpfCliente, codFuncionario, endereco, produtos, formaPagamento);
                    JOptionPane.showMessageDialog(null, "Pedido criado com sucesso!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao criar pedido: " + ex.getMessage());
                }
            }
        });
        add(btnCriarPedido);

        // Botão Listar Pedidos
        btnListarPedidos = new JButton("Listar Pedidos");
        btnListarPedidos.setBounds(210, 300, 150, 30);
        btnListarPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pedidos = null;
                try {
                    fachada.listarPedidos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, pedidos);
            }
        });
        add(btnListarPedidos);
    }
}
