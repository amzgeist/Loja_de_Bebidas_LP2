package gui;

import excecoes.PedidoNaoEncontradoException;
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
    private JTextField txtCodigoPedido;
    private JButton btnCriarPedido;
    private JButton btnListarPedidos;
    private JButton btnBuscarPedido;
    private JButton btnAdicionarPagamento;
    private JButton btnAtualizarStatus;

    private Map<Integer, Integer> produtos = new HashMap<>();
    private Fachada fachada;

    public PedidoFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Pedidos");
        setSize(400, 450);
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

        // Código do Pedido (Para Buscar)
        JLabel lblCodigoPedido = new JLabel("Código do Pedido:");
        lblCodigoPedido.setBounds(30, 290, 150, 25);
        add(lblCodigoPedido);

        txtCodigoPedido = new JTextField();
        txtCodigoPedido.setBounds(180, 290, 170, 25);
        add(txtCodigoPedido);

        // Botão Criar Pedido
        btnCriarPedido = new JButton("Criar Pedido");
        btnCriarPedido.setBounds(50, 330, 150, 30);
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
        btnListarPedidos.setBounds(210, 330, 150, 30);
        btnListarPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pedidos = null;
                try {
                    fachada.listarPedidos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao listar pedidos: " + ex.getMessage());
                }
                JOptionPane.showMessageDialog(null, pedidos);
            }
        });
        add(btnListarPedidos);

        // Botão Buscar Pedido
        btnBuscarPedido = new JButton("Buscar Pedido");
        btnBuscarPedido.setBounds(50, 370, 150, 30);
        btnBuscarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigoPedido = Integer.parseInt(txtCodigoPedido.getText());
                try {
                    String pedidoInfo = fachada.buscarPedido(codigoPedido);
                    if (pedidoInfo != null) {
                        JOptionPane.showMessageDialog(null, pedidoInfo);
                    } else {
                        JOptionPane.showMessageDialog(null, "Pedido não encontrado.");
                    }
                } catch (SQLException | PedidoNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao buscar pedido: " + ex.getMessage());
                }
            }
        });
        add(btnBuscarPedido);

        // Botão Adicionar Pagamento
        btnAdicionarPagamento = new JButton("Adicionar Pagamento");
        btnAdicionarPagamento.setBounds(210, 370, 150, 30);
        btnAdicionarPagamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigoPedido = Integer.parseInt(txtCodigoPedido.getText());
                double valorPagamento = Double.parseDouble(JOptionPane.showInputDialog("Digite o valor do pagamento:"));

                try {
                    fachada.adicionarPagamento(codigoPedido, valorPagamento);
                    JOptionPane.showMessageDialog(null, "Pagamento adicionado com sucesso!");
                } catch (SQLException | PedidoNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar pagamento: " + ex.getMessage());
                }
            }
        });
        add(btnAdicionarPagamento);

        // Botão Atualizar Status
        btnAtualizarStatus = new JButton("Atualizar Status");
        btnAtualizarStatus.setBounds(50, 410, 150, 30);
        btnAtualizarStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigoPedido = Integer.parseInt(txtCodigoPedido.getText());
                String novoStatus = JOptionPane.showInputDialog("Digite o novo status do pedido:");

                try {
                    fachada.atualizarStatusPedido(codigoPedido, novoStatus);
                    JOptionPane.showMessageDialog(null, "Status do pedido atualizado com sucesso!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar status do pedido: " + ex.getMessage());
                } catch (PedidoNaoEncontradoException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(btnAtualizarStatus);
}}