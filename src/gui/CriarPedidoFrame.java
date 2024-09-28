package gui;

import excecoes.FuncionarioNaoEncontradoException;
import excecoes.ProdutoNaoEncontradoException;
import negocio.Fachada;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;

public class CriarPedidoFrame extends JFrame {

    private JTextField txtCpfCliente;
    private JTextField txtCodigoFuncionario;
    private JTextField txtEndereco;
    private JComboBox<String> cmbFormaPagamento;
    private JButton btnCriarPedido;
    private JButton btnCancelar;
    private Map<Integer, Integer> produtos;
    private Fachada fachada;

    public CriarPedidoFrame(Fachada fachada, Map<Integer, Integer> produtos) {
        this.fachada = fachada;
        this.produtos = produtos;

        setTitle("Criar Pedido");
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblCpfCliente = new JLabel("CPF do Cliente:");
        lblCpfCliente.setBounds(30, 30, 150, 25);
        add(lblCpfCliente);

        txtCpfCliente = new JTextField();
        txtCpfCliente.setBounds(180, 30, 150, 25);
        add(txtCpfCliente);

        JLabel lblCodigoFuncionario = new JLabel("Código do Funcionário:");
        lblCodigoFuncionario.setBounds(30, 70, 150, 25);
        add(lblCodigoFuncionario);

        txtCodigoFuncionario = new JTextField();
        txtCodigoFuncionario.setBounds(180, 70, 150, 25);
        add(txtCodigoFuncionario);

        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setBounds(30, 110, 150, 25);
        add(lblEndereco);

        txtEndereco = new JTextField();
        txtEndereco.setBounds(180, 110, 150, 25);
        add(txtEndereco);

        JLabel lblFormaPagamento = new JLabel("Forma de Pagamento:");
        lblFormaPagamento.setBounds(30, 150, 150, 25);
        add(lblFormaPagamento);

        String[] formasPagamento = {"Cartão de Crédito", "Cartão de Débito", "Dinheiro", "PIX"};
        cmbFormaPagamento = new JComboBox<>(formasPagamento);
        cmbFormaPagamento.setBounds(180, 150, 150, 25);
        add(cmbFormaPagamento);

        // Botão Criar Pedido
        btnCriarPedido = new JButton("Criar Pedido");
        btnCriarPedido.setBounds(30, 200, 150, 30);
        btnCriarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    criarPedido();
                } catch (FuncionarioNaoEncontradoException ex) {
                    throw new RuntimeException(ex);
                } catch (ProdutoNaoEncontradoException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(btnCriarPedido);

        // Botão Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(180, 200, 150, 30);
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(btnCancelar);

        setVisible(true);
    }

    private void criarPedido() throws FuncionarioNaoEncontradoException, ProdutoNaoEncontradoException {
        try {
            String cpfCliente = txtCpfCliente.getText();
            int codigoFuncionario = Integer.parseInt(txtCodigoFuncionario.getText());
            String endereco = txtEndereco.getText();
            String formaPagamento = cmbFormaPagamento.getSelectedItem().toString();

            // Chama o método da fachada para criar o pedido
            fachada.criarPedido(cpfCliente, codigoFuncionario, endereco, produtos, formaPagamento);
            JOptionPane.showMessageDialog(this, "Pedido criado com sucesso!");
            dispose(); // Fecha a janela após criar o pedido
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar pedido: " + ex.getMessage());
        }
    }
}