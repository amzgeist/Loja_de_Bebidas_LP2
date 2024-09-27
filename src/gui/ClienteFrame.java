package gui;

import negocio.Fachada;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

public class ClienteFrame extends JFrame {
    private JTextField txtCpf;
    private JButton btnCadastrarCliente;
    private JButton btnListarClientes;
    private JButton btnBuscarCliente;
    private JButton btnAtualizarCliente;
    private JButton btnDeletarCliente;
    private Fachada fachada;

    public ClienteFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Clientes");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // CPF
        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 50, 100, 25);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(150, 50, 200, 25);
        add(txtCpf);

        // Botões
        btnCadastrarCliente = new JButton("Cadastrar Cliente");
        btnCadastrarCliente.setBounds(30, 100, 150, 30);
        btnCadastrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaCadastroCliente();
            }
        });
        add(btnCadastrarCliente);

        btnListarClientes = new JButton("Listar Clientes");
        btnListarClientes.setBounds(200, 100, 150, 30);
        btnListarClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String clientes = fachada.listarClientes();
                    JOptionPane.showMessageDialog(null, clientes);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao listar clientes: " + ex.getMessage());
                }
            }
        });
        add(btnListarClientes);

        btnBuscarCliente = new JButton("Buscar Cliente");
        btnBuscarCliente.setBounds(30, 150, 150, 30);
        btnBuscarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });
        add(btnBuscarCliente);

        btnAtualizarCliente = new JButton("Atualizar Cliente");
        btnAtualizarCliente.setBounds(200, 150, 150, 30);
        btnAtualizarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarParaAtualizar();
            }
        });
        add(btnAtualizarCliente);

        btnDeletarCliente = new JButton("Deletar Cliente");
        btnDeletarCliente.setBounds(30, 200, 150, 30);
        btnDeletarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarParaDeletar();
            }
        });
        add(btnDeletarCliente);
    }

    private void abrirTelaCadastroCliente() {
        JFrame cadastroFrame = new JFrame("Cadastrar Cliente");
        cadastroFrame.setSize(400, 300);
        cadastroFrame.setLayout(null);
        cadastroFrame.setLocationRelativeTo(null);

        // Nome
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 50, 100, 25);
        cadastroFrame.add(lblNome);

        JTextField txtNome = new JTextField();
        txtNome.setBounds(150, 50, 200, 25);
        cadastroFrame.add(txtNome);

        // CPF
        JLabel lblCpfCadastro = new JLabel("CPF:");
        lblCpfCadastro.setBounds(30, 90, 100, 25);
        cadastroFrame.add(lblCpfCadastro);

        JTextField txtCpfCadastro = new JTextField();
        txtCpfCadastro.setBounds(150, 90, 200, 25);
        cadastroFrame.add(txtCpfCadastro);

        // Data de Nascimento
        JLabel lblDataNascimento = new JLabel("Data de Nascimento:");
        lblDataNascimento.setBounds(30, 130, 150, 25);
        cadastroFrame.add(lblDataNascimento);

        JTextField txtDataNascimento = new JTextField();
        txtDataNascimento.setBounds(150, 130, 200, 25);
        cadastroFrame.add(txtDataNascimento);

        // Botão Cadastrar
        JButton btnEfetuarCadastro = new JButton("Efetuar Cadastro");
        btnEfetuarCadastro.setBounds(150, 180, 150, 30);
        btnEfetuarCadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String cpf = txtCpfCadastro.getText();
                String dataNascimento = txtDataNascimento.getText();
                try {
                    try {
                        fachada.cadastrarCliente(nome, cpf, dataNascimento);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(cadastroFrame, "Cliente cadastrado com sucesso!");
                    cadastroFrame.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(cadastroFrame, "Erro ao cadastrar cliente: " + ex.getMessage());
                }
            }
        });
        cadastroFrame.add(btnEfetuarCadastro);

        cadastroFrame.setVisible(true);
    }

    private void buscarCliente() {
        String cpf = txtCpf.getText();
        try {
            String cliente = fachada.buscarCliente(cpf);
            JOptionPane.showMessageDialog(null, cliente);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar cliente: " + ex.getMessage());
        }
    }

    private void buscarParaAtualizar() {
        String cpf = txtCpf.getText();
        try {
            String cliente = fachada.buscarCliente(cpf);
            if (cliente != null) {
                abrirTelaAtualizarCliente(cpf);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar cliente: " + ex.getMessage());
        }
    }

    private void abrirTelaAtualizarCliente(String cpf) {
        JFrame atualizarFrame = new JFrame("Atualizar Cliente");
        atualizarFrame.setSize(400, 300);
        atualizarFrame.setLayout(null);
        atualizarFrame.setLocationRelativeTo(null);

        // Nome
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 50, 100, 25);
        atualizarFrame.add(lblNome);

        JTextField txtNome = new JTextField();
        txtNome.setBounds(150, 50, 200, 25);
        atualizarFrame.add(txtNome);

        // Data de Nascimento
        JLabel lblDataNascimento = new JLabel("Data de Nascimento:");
        lblDataNascimento.setBounds(30, 90, 150, 25);
        atualizarFrame.add(lblDataNascimento);

        JTextField txtDataNascimento = new JTextField();
        txtDataNascimento.setBounds(150, 90, 200, 25);
        atualizarFrame.add(txtDataNascimento);

        // Botão Confirmar Atualização
        JButton btnConfirmarAtualizacao = new JButton("Confirmar Atualização");
        btnConfirmarAtualizacao.setBounds(150, 150, 150, 30);
        btnConfirmarAtualizacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String dataNascimento = txtDataNascimento.getText();
                try {
                    fachada.atualizarCliente(cpf, nome, dataNascimento);
                    JOptionPane.showMessageDialog(atualizarFrame, "Cliente atualizado com sucesso!");
                    atualizarFrame.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(atualizarFrame, "Erro ao atualizar cliente: " + ex.getMessage());
                }
            }
        });
        atualizarFrame.add(btnConfirmarAtualizacao);

        atualizarFrame.setVisible(true);
    }

    private void buscarParaDeletar() {
        String cpf = txtCpf.getText();
        try {
            String cliente = fachada.buscarCliente(cpf);
            if (cliente != null) {
                int confirm = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar este cliente?\n" + cliente, "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    fachada.deletarCliente(cpf);
                    JOptionPane.showMessageDialog(null, "Cliente deletado com sucesso!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar cliente: " + ex.getMessage());
        }
    }
}