package gui;

import negocio.Fachada;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FuncionarioFrame extends JFrame {

    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtSalario;
    private JTextField txtTipo;
    private JButton btnCadastrarFuncionario;
    private JButton btnListarFuncionarios;

    private Fachada fachada;

    public FuncionarioFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Funcionários");
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

        // Salário
        JLabel lblSalario = new JLabel("Salário:");
        lblSalario.setBounds(30, 130, 100, 25);
        add(lblSalario);

        txtSalario = new JTextField();
        txtSalario.setBounds(150, 130, 200, 25);
        add(txtSalario);

        // Tipo (Assalariado/Comissionado)
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(30, 170, 100, 25);
        add(lblTipo);

        txtTipo = new JTextField();
        txtTipo.setBounds(150, 170, 200, 25);
        add(txtTipo);

        // Botão Cadastrar Funcionário
        btnCadastrarFuncionario = new JButton("Cadastrar Funcionário");
        btnCadastrarFuncionario.setBounds(50, 220, 150, 30);
        btnCadastrarFuncionario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String cpf = txtCpf.getText();
                double salario = Double.parseDouble(txtSalario.getText());
                String tipo = txtTipo.getText();

                try {
                    fachada.cadastrarFuncionario(nome, cpf, tipo, salario);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Funcionário cadastrado com sucesso!");
            }
        });
        add(btnCadastrarFuncionario);

        // Botão Listar Funcionários
        btnListarFuncionarios = new JButton("Listar Funcionários");
        btnListarFuncionarios.setBounds(210, 220, 150, 30);
        btnListarFuncionarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String funcionarios = null;
                try {
                    fachada.listarFuncionarios();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, funcionarios);
            }
        });
        add(btnListarFuncionarios);
    }
}
