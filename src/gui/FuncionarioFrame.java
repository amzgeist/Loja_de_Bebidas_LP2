package gui;

import dados.Funcionario;
import excecoes.FuncionarioNaoEncontradoException;
import negocio.Fachada;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioFrame extends JFrame {

    private JTextField txtCodigoFuncionario;
    private JButton btnCadastrarFuncionario;
    private JButton btnListarFuncionarios;
    private JButton btnBuscarFuncionario;
    private JButton btnAtualizarFuncionario;
    private JButton btnDesativarFuncionario;

    private Fachada fachada;

    public FuncionarioFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Funcionários");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Campo para digitar o código do funcionário
        JLabel lblCodigoFuncionario = new JLabel("Código do Funcionário:");
        lblCodigoFuncionario.setBounds(30, 20, 150, 25);
        add(lblCodigoFuncionario);

        txtCodigoFuncionario = new JTextField();
        txtCodigoFuncionario.setBounds(180, 20, 150, 25);
        add(txtCodigoFuncionario);

        // Botões de Ação
        btnCadastrarFuncionario = new JButton("Cadastrar Funcionário");
        btnCadastrarFuncionario.setBounds(30, 60, 150, 30);
        add(btnCadastrarFuncionario);

        btnListarFuncionarios = new JButton("Listar Funcionários");
        btnListarFuncionarios.setBounds(200, 60, 150, 30);
        add(btnListarFuncionarios);

        btnBuscarFuncionario = new JButton("Buscar Funcionário");
        btnBuscarFuncionario.setBounds(30, 100, 150, 30);
        add(btnBuscarFuncionario);

        btnAtualizarFuncionario = new JButton("Atualizar Funcionário");
        btnAtualizarFuncionario.setBounds(200, 100, 150, 30);
        add(btnAtualizarFuncionario);

        btnDesativarFuncionario = new JButton("Desativar Funcionário");
        btnDesativarFuncionario.setBounds(30, 140, 150, 30);
        add(btnDesativarFuncionario);

        // Ações dos botões
        btnCadastrarFuncionario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioCadastro();
            }
        });

        btnListarFuncionarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarFuncionarios();
            }
        });

        btnBuscarFuncionario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFuncionario();
            }
        });

        btnAtualizarFuncionario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarFuncionario();
            }
        });

        btnDesativarFuncionario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desativarFuncionario();
            }
        });
    }

    // Método para listar funcionários diretamente na interface
    private void listarFuncionarios() {
        try {
            List<Funcionario> funcionarios = fachada.funcionarioController.listarFuncionarios();
            StringBuilder sb = new StringBuilder();

            for (Funcionario funcionario : funcionarios) {
                sb.append("Código: ").append(funcionario.getCodigoFunc())
                        .append(", Nome: ").append(funcionario.getNome())
                        .append(", CPF: ").append(funcionario.getCpf())
                        .append(", Tipo: ").append(funcionario.getTipo())
                        .append(", Salário: ").append(funcionario.getSalario())
                        .append(", Ativo: ").append(funcionario.isAtivo())
                        .append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBounds(30, 180, 400, 200);
            add(scrollPane);
            revalidate();
            repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar funcionários: " + ex.getMessage());
        }
    }

    // Buscar funcionário (mantido como está)
    private void buscarFuncionario() {
        try {
            String codigo = JOptionPane.showInputDialog("Digite o código do funcionário:");
            if (codigo != null) {
                String funcionario = String.valueOf(fachada.buscarFuncionario(Integer.parseInt(codigo)));
                JOptionPane.showMessageDialog(this, funcionario);
            }
        } catch (SQLException | NumberFormatException | FuncionarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar funcionário: " + ex.getMessage());
        }
    }

    // Mostrar tela de cadastro
    private void mostrarFormularioCadastro() {
        JFrame cadastroFrame = new JFrame("Cadastro de Funcionário");
        cadastroFrame.setSize(400, 300);
        cadastroFrame.setLayout(null);
        cadastroFrame.setLocationRelativeTo(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        cadastroFrame.add(lblNome);

        JTextField txtNome = new JTextField();
        txtNome.setBounds(130, 30, 200, 25);
        cadastroFrame.add(txtNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 70, 100, 25);
        cadastroFrame.add(lblCpf);

        JTextField txtCpf = new JTextField();
        txtCpf.setBounds(130, 70, 200, 25);
        cadastroFrame.add(txtCpf);

        JLabel lblSalario = new JLabel("Salário:");
        lblSalario.setBounds(30, 110, 100, 25);
        cadastroFrame.add(lblSalario);

        JTextField txtSalario = new JTextField();
        txtSalario.setBounds(130, 110, 200, 25);
        cadastroFrame.add(txtSalario);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(30, 150, 100, 25);
        cadastroFrame.add(lblTipo);

        JComboBox<String> comboBoxTipo = new JComboBox<>(new String[]{"Assalariado", "Comissionado"});
        comboBoxTipo.setBounds(130, 150, 200, 25);
        cadastroFrame.add(comboBoxTipo);

        JButton btnEfetuarCadastro = new JButton("Efetuar Cadastro");
        btnEfetuarCadastro.setBounds(150, 200, 150, 30);
        cadastroFrame.add(btnEfetuarCadastro);

        btnEfetuarCadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String cpf = txtCpf.getText();
                double salario = Double.parseDouble(txtSalario.getText());
                String tipo = (String) comboBoxTipo.getSelectedItem();

                try {
                    fachada.cadastrarFuncionario(nome, cpf, tipo, salario);
                    JOptionPane.showMessageDialog(cadastroFrame, "Funcionário cadastrado com sucesso!");
                    cadastroFrame.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(cadastroFrame, "Erro ao cadastrar funcionário: " + ex.getMessage());
                }
            }
        });

        cadastroFrame.setVisible(true);
    }

    // Mostrar tela de atualização de funcionário
    private void atualizarFuncionario() {
        String codigo = txtCodigoFuncionario.getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o código do funcionário!");
            return;
        }

        try {
            Funcionario funcionario = fachada.buscarFuncionario(Integer.parseInt(codigo));
            if (funcionario == null) {
                JOptionPane.showMessageDialog(this, "Funcionário não encontrado!");
                return;
            }

            JFrame atualizarFrame = new JFrame("Atualizar Funcionário");
            atualizarFrame.setSize(400, 300);
            atualizarFrame.setLayout(null);
            atualizarFrame.setLocationRelativeTo(null);

            JLabel lblNome = new JLabel("Nome:");
            lblNome.setBounds(30, 30, 100, 25);
            atualizarFrame.add(lblNome);

            JTextField txtNome = new JTextField(funcionario.getNome());
            txtNome.setBounds(130, 30, 200, 25);
            atualizarFrame.add(txtNome);

            JLabel lblSalario = new JLabel("Salário:");
            lblSalario.setBounds(30, 70, 100, 25);
            atualizarFrame.add(lblSalario);

            JTextField txtSalario = new JTextField(String.valueOf(funcionario.getSalario()));
            txtSalario.setBounds(130, 70, 200, 25);
            atualizarFrame.add(txtSalario);

            JLabel lblTipo = new JLabel("Tipo:");
            lblTipo.setBounds(30, 110, 100, 25);
            atualizarFrame.add(lblTipo);

            JComboBox<String> comboBoxTipo = new JComboBox<>(new String[]{"Assalariado", "Comissionado"});
            comboBoxTipo.setSelectedItem(funcionario.getTipo());
            comboBoxTipo.setBounds(130, 110, 200, 25);
            atualizarFrame.add(comboBoxTipo);

            JButton btnConfirmarAtualizacao = new JButton("Confirmar Atualização");
            btnConfirmarAtualizacao.setBounds(150, 200, 150, 30);
            atualizarFrame.add(btnConfirmarAtualizacao);

            btnConfirmarAtualizacao.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nomeAtualizado = txtNome.getText();
                    double salarioAtualizado = Double.parseDouble(txtSalario.getText());
                    String tipoAtualizado = (String) comboBoxTipo.getSelectedItem();

                    try {
                        fachada.atualizarFuncionario(Integer.parseInt(codigo), nomeAtualizado, funcionario.getCpf(), tipoAtualizado, salarioAtualizado);
                        JOptionPane.showMessageDialog(atualizarFrame, "Funcionário atualizado com sucesso!");
                        atualizarFrame.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(atualizarFrame, "Erro ao atualizar funcionário: " + ex.getMessage());
                    } catch (FuncionarioNaoEncontradoException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            atualizarFrame.setVisible(true);

        } catch (SQLException | FuncionarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar funcionário: " + ex.getMessage());
        }
    }

    // Desativar funcionário
    private void desativarFuncionario() {
        String codigo = txtCodigoFuncionario.getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o código do funcionário!");
            return;
        }

        try {
            fachada.desativarFuncionario(Integer.parseInt(codigo));
            JOptionPane.showMessageDialog(this, "Funcionário desativado com sucesso!");
        } catch (SQLException | FuncionarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao desativar funcionário: " + ex.getMessage());
        }
    }
}