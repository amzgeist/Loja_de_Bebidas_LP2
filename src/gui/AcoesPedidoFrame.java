package gui;

import excecoes.PedidoNaoEncontradoException;
import negocio.Fachada;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AcoesPedidoFrame extends JFrame {

    private JButton btnAdicionarPagamento;
    private JButton btnAtualizarStatus;
    private JButton btnCancelarPedido;
    private int codigoPedido;
    private Fachada fachada;

    public AcoesPedidoFrame(Fachada fachada, int codigoPedido) {
        this.fachada = fachada;
        this.codigoPedido = codigoPedido;

        setTitle("Ações sobre o Pedido");
        setSize(400, 200);
        setLayout(null);
        setLocationRelativeTo(null);

        // Botão Adicionar Pagamento
        btnAdicionarPagamento = new JButton("Adicionar Pagamento");
        btnAdicionarPagamento.setBounds(30, 30, 150, 30);
        btnAdicionarPagamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarPagamento();
            }
        });
        add(btnAdicionarPagamento);

        // Botão Atualizar Status
        btnAtualizarStatus = new JButton("Atualizar Status");
        btnAtualizarStatus.setBounds(30, 70, 150, 30);
        btnAtualizarStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarStatus();
            }
        });
        add(btnAtualizarStatus);

        // Botão Cancelar Pedido
        btnCancelarPedido = new JButton("Cancelar Pedido");
        btnCancelarPedido.setBounds(30, 110, 150, 30);
        btnCancelarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarPedido();
            }
        });
        add(btnCancelarPedido);

        setVisible(true);
    }

    private void adicionarPagamento() {
        try {
            double valorPagamento = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o valor do pagamento:"));
            fachada.adicionarPagamento(codigoPedido, valorPagamento);
            JOptionPane.showMessageDialog(this, "Pagamento adicionado com sucesso!");
        } catch (SQLException | PedidoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar pagamento: " + ex.getMessage());
        }
    }

    private void atualizarStatus() {
        try {
            String novoStatus = JOptionPane.showInputDialog(this, "Digite o novo status:");
            fachada.atualizarStatusPedido(codigoPedido, novoStatus);
            JOptionPane.showMessageDialog(this, "Status atualizado com sucesso.");
        } catch (SQLException | PedidoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar status: " + ex.getMessage());
        }
    }

    private void cancelarPedido() {
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar este pedido?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                fachada.cancelarPedido(codigoPedido);
                JOptionPane.showMessageDialog(this, "Pedido cancelado com sucesso.");
                dispose(); // Fecha a janela após cancelar o pedido
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar pedido: " + ex.getMessage());
            } catch (PedidoNaoEncontradoException e) {
                throw new RuntimeException(e);
            }
        }
    }
}