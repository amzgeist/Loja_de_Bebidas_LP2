package gui;

import negocio.Fachada;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ClienteFrame extends JFrame {

    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtDataNascimento;
    private JButton btnCadastrarCliente;
    private JButton btnListarClientes;

    private Fachada fachada;

    public ClienteFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Clientes");
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

        // CPF
        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 90, 100, 25);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(150, 90, 200, 25);
        add(txtCpf);

        // Data de Nascimento
        JLabel lblDataNascimento = new JLabel("Data de Nascimento:");
        lblDataNascimento.setBounds(30, 130, 150, 25);
        add(lblDataNascimento);

        txtDataNascimento = new JTextField();
        txtDataNascimento.setBounds(150, 130, 200, 25);
        add(txtDataNascimento);

        // Botão Cadastrar Cliente
        btnCadastrarCliente = new JButton("Cadastrar Cliente");
        btnCadastrarCliente.setBounds(50, 180, 150, 30);
        btnCadastrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String cpf = txtCpf.getText();
                String dataNascimento = txtDataNascimento.getText();

                // Correção da chamada de fachada
                fachada.cadastrarCliente(txtNome.getText(), txtCpf.getText(), txtDataNascimento.getText());
                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
            }
        });
        add(btnCadastrarCliente);

        // Botão Listar Clientes
        btnListarClientes = new JButton("Listar Clientes");
        btnListarClientes.setBounds(210, 180, 150, 30);
        btnListarClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientes = null;
                try {
                    fachada.listarClientes();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, clientes);
            }
        });
        add(btnListarClientes);
    }
}
