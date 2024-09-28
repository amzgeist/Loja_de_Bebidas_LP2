package gui;

import dados.Pedido;
import excecoes.PedidoNaoEncontradoException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;
import negocio.Fachada;

public class PedidoFrame extends JFrame {

    private JTextField txtCodigoPedido;
    private JButton btnCriarPedido;
    private JButton btnListarPedidos;
    private JButton btnBuscarPedido;
    private Fachada fachada;
    private JTextArea textAreaPedidos;

    public PedidoFrame() throws SQLException {
        fachada = new Fachada();

        setTitle("Gerenciamento de Pedidos");
        setSize(600, 400); // Aumentei o tamanho para acomodar melhor os componentes
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Código do Pedido
        JLabel lblCodigoPedido = new JLabel("Código do Pedido:");
        lblCodigoPedido.setBounds(30, 30, 150, 25);
        add(lblCodigoPedido);

        txtCodigoPedido = new JTextField();
        txtCodigoPedido.setBounds(180, 30, 150, 25);
        add(txtCodigoPedido);

        // Botões
        btnCriarPedido = new JButton("Criar Pedido");
        btnCriarPedido.setBounds(30, 80, 150, 30);
        btnCriarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaAdicionarProdutos();
            }
        });
        add(btnCriarPedido);

        btnListarPedidos = new JButton("Listar Pedidos");
        btnListarPedidos.setBounds(200, 80, 150, 30);
        btnListarPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarPedidos();  // Chama o método de listagem de pedidos diretamente na interface
            }
        });
        add(btnListarPedidos);

        btnBuscarPedido = new JButton("Buscar Pedido");
        btnBuscarPedido.setBounds(370, 80, 150, 30);
        btnBuscarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPedido();
            }
        });
        add(btnBuscarPedido);

        // Área de texto para listar os pedidos
        textAreaPedidos = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textAreaPedidos);
        scrollPane.setBounds(30, 130, 540, 200);
        add(scrollPane);
    }

    // Método para abrir a tela de adicionar produtos ao pedido
    private void abrirTelaAdicionarProdutos() {
        new AdicionarProdutosFrame(fachada); // Abre a interface de adicionar produtos
    }

    // Método para listar pedidos
    private void listarPedidos() {
        textAreaPedidos.setText(""); // Limpa o campo antes de listar novamente
        try {
            for (Pedido pedido : fachada.listarPedidos()) {
                textAreaPedidos.append(pedido.toString() + "\n");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar pedidos: " + ex.getMessage());
        }
    }

    // Método para buscar um pedido por código
    private void buscarPedido() {
        String codigoPedido = txtCodigoPedido.getText();
        if (codigoPedido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o código do pedido!");
            return;
        }
        try {
            String pedidoDetalhes = fachada.buscarPedido(Integer.parseInt(codigoPedido));
            if (pedidoDetalhes != null) {
                JOptionPane.showMessageDialog(this, pedidoDetalhes);
                abrirTelaAcoesPedido(codigoPedido); // Abre a tela com as opções de ação sobre o pedido
            } else {
                JOptionPane.showMessageDialog(this, "Pedido não encontrado.");
            }
        } catch (SQLException | PedidoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar pedido: " + ex.getMessage());
        }
    }

    // Método para abrir a tela com ações sobre o pedido (adicionar pagamento, atualizar status, etc.)
    private void abrirTelaAcoesPedido(String codigoPedido) {
        new AcoesPedidoFrame(fachada, Integer.parseInt(codigoPedido)); // Abre a interface de ações sobre o pedido
    }
}
