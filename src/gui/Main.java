package gui;

import dados.Funcionario;
import excecoes.*;
import negocio.Fachada;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Fachada fachada;

    public static void main(String[] args) {
        try {
            fachada = new Fachada();
            showMainMenu();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private static void showMainMenu() {
        String[] options = {"Gerenciar Clientes", "Gerenciar Funcionários", "Gerenciar Produtos", "Gerenciar Pedidos", "Sair"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Sistema de Gerenciamento",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    manageClientes();
                    break;
                case 1:
                    manageFuncionarios();
                    break;
                case 2:
                    manageProdutos();
                    break;
                case 3:
                    managePedidos();
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Saindo...");
                    System.exit(0);
                    break;
                default:
                    break;
            }
        } while (choice != -1);
    }

    private static void manageClientes() {
        String[] options = {"Cadastrar Cliente", "Listar Clientes", "Buscar Cliente", "Atualizar Cliente", "Deletar Cliente", "Voltar"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(null, "Gerenciamento de Clientes", "Clientes",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            try {
                switch (choice) {
                    case 0:
                        cadastrarCliente();
                        break;
                    case 1:
                        listarClientes();
                        break;
                    case 2:
                        buscarCliente();
                        break;
                    case 3:
                        atualizarCliente();
                        break;
                    case 4:
                        deletarCliente();
                        break;
                    case 5:
                        return;
                    default:
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        } while (choice != 5);
    }

    private static void manageFuncionarios() {
        String[] options = {"Cadastrar Funcionário", "Listar Funcionários", "Buscar Funcionário", "Atualizar Funcionário", "Desativar Funcionário", "Voltar"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(null, "Gerenciamento de Funcionários", "Funcionários",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            try {
                switch (choice) {
                    case 0:
                        cadastrarFuncionario();
                        break;
                    case 1:
                        listarFuncionarios();
                        break;
                    case 2:
                        buscarFuncionario();
                        break;
                    case 3:
                        atualizarFuncionario();
                        break;
                    case 4:
                        desativarFuncionario();
                        break;
                    case 5:
                        return;
                    default:
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        } while (choice != 5);
    }

    private static void manageProdutos() {
        String[] options = {"Cadastrar Produto", "Listar Produtos", "Buscar Produto", "Atualizar Produto", "Deletar Produto", "Voltar"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(null, "Gerenciamento de Produtos", "Produtos",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            try {
                switch (choice) {
                    case 0:
                        cadastrarProduto();
                        break;
                    case 1:
                        listarProdutos();
                        break;
                    case 2:
                        buscarProduto();
                        break;
                    case 3:
                        atualizarProduto();
                        break;
                    case 4:
                        deletarProduto();
                        break;
                    case 5:
                        return;
                    default:
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        } while (choice != 5);
    }

    private static void managePedidos() {
        String[] options = {"Criar Pedido", "Listar Pedidos", "Buscar Pedido", "Adicionar Pagamento", "Atualizar Status", "Voltar"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(null, "Gerenciamento de Pedidos", "Pedidos",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            try {
                switch (choice) {
                    case 0:
                        criarPedido();
                        break;
                    case 1:
                        listarPedidos();
                        break;
                    case 2:
                        buscarPedido();
                        break;
                    case 3:
                        adicionarPagamento();
                        break;
                    case 4:
                        atualizarStatusPedido();
                        break;
                    case 5:
                        return;
                    default:
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        } while (choice != 5);
    }

    // Métodos relacionados a clientes
    private static void cadastrarCliente() throws SQLException, ParseException {
        String nome = JOptionPane.showInputDialog("Nome:");
        String cpf = JOptionPane.showInputDialog("CPF:");
        String dataNascimento = JOptionPane.showInputDialog("Data de Nascimento (dd/mm/yyyy):");
        fachada.cadastrarCliente(nome, cpf, dataNascimento);
    }

    private static void listarClientes() throws SQLException {
        fachada.listarClientes();
    }

    private static void buscarCliente() throws SQLException {
        String cpf = JOptionPane.showInputDialog("Digite o CPF do cliente:");
        String cliente = fachada.buscarCliente(cpf);
        JOptionPane.showMessageDialog(null, cliente != null ? cliente : "Cliente não encontrado.");
    }

    private static void atualizarCliente() throws SQLException {
        String cpf = JOptionPane.showInputDialog("Digite o CPF do cliente a ser atualizado:");
        String nome = JOptionPane.showInputDialog("Novo nome:");
        String dataNascimento = JOptionPane.showInputDialog("Nova data de nascimento:");
        fachada.atualizarCliente(cpf, nome, dataNascimento);
    }

    private static void deletarCliente() throws SQLException {
        String cpf = JOptionPane.showInputDialog("Digite o CPF do cliente a ser deletado:");
        fachada.deletarCliente(cpf);
    }

    // Métodos relacionados a funcionários
    private static void cadastrarFuncionario() throws SQLException {
        String nome = JOptionPane.showInputDialog("Nome:");
        String cpf = JOptionPane.showInputDialog("CPF:");
        String tipo = JOptionPane.showInputDialog("Tipo (assalariado/comissionado):");
        double salario = Double.parseDouble(JOptionPane.showInputDialog("Salário:"));
        fachada.cadastrarFuncionario(nome, cpf, tipo, salario);
    }

    private static void listarFuncionarios() throws SQLException {
        fachada.listarFuncionarios();
    }

    private static void buscarFuncionario() throws SQLException, FuncionarioNaoEncontradoException {
        int codigo = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do funcionário:"));
        String funcionario = String.valueOf(fachada.buscarFuncionario(codigo));
        JOptionPane.showMessageDialog(null, funcionario != null ? funcionario : "Funcionário não encontrado.");
    }

    private static void atualizarFuncionario() throws SQLException, FuncionarioNaoEncontradoException {
        int codigo = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do funcionário a ser atualizado:"));
        String nome = JOptionPane.showInputDialog("Novo nome:");
        String cpf = JOptionPane.showInputDialog("Novo CPF:");
        String tipo = JOptionPane.showInputDialog("Novo tipo (assalariado/comissionado):");
        double salario = Double.parseDouble(JOptionPane.showInputDialog("Novo salário:"));
        fachada.atualizarFuncionario(codigo, nome, cpf, tipo, salario);
    }

    private static void desativarFuncionario() throws SQLException, FuncionarioNaoEncontradoException {
        int codigo = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do funcionário a ser desativado:"));
        fachada.desativarFuncionario(codigo);
    }

    // Métodos relacionados a produtos
    private static void cadastrarProduto() throws SQLException, ProdutoJaExisteException {
        String nome = JOptionPane.showInputDialog("Nome do Produto:");
        float preco = Float.parseFloat(JOptionPane.showInputDialog("Preço:"));
        int estoque = Integer.parseInt(JOptionPane.showInputDialog("Estoque:"));
        fachada.cadastrarProduto(nome, preco, estoque);
    }

    private static void listarProdutos() throws SQLException {
        fachada.listarProdutos();
    }

    private static void buscarProduto() throws SQLException, ProdutoNaoEncontradoException {
        int codigo = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do produto:"));
        String produto = fachada.buscarProduto(codigo);
        JOptionPane.showMessageDialog(null, produto != null ? produto : "Produto não encontrado.");
    }

    private static void atualizarProduto() throws SQLException, ProdutoNaoEncontradoException {
        int codigo = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do produto a ser atualizado:"));
        String nome = JOptionPane.showInputDialog("Novo nome:");
        float preco = Float.parseFloat(JOptionPane.showInputDialog("Novo preço:"));
        int estoque = Integer.parseInt(JOptionPane.showInputDialog("Novo estoque:"));
        fachada.atualizarProduto(codigo, nome, preco, estoque);
    }

    private static void deletarProduto() throws SQLException, ProdutoNaoEncontradoException {
        int codigo = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do produto a ser deletado:"));
        fachada.deletarProduto(codigo);
    }

    // Métodos relacionados a pedidos
    private static void criarPedido() throws SQLException, ProdutoNaoEncontradoException, ClienteNaoEncontradoException, FuncionarioNaoEncontradoException {
        String cpfCliente = JOptionPane.showInputDialog("CPF do Cliente:");
        int codFuncionario = Integer.parseInt(JOptionPane.showInputDialog("Código do Funcionário:"));
        String endereco = JOptionPane.showInputDialog("Endereço:");
        String formaPagamento = JOptionPane.showInputDialog("Forma de Pagamento:");
        int numeroDeProdutos = Integer.parseInt(JOptionPane.showInputDialog("Quantos produtos você deseja adicionar ao pedido?"));

        Map<Integer, Integer> produtos = new HashMap<>();

        for (int i = 0; i < numeroDeProdutos; i++) {
            int codigoProduto = Integer.parseInt(JOptionPane.showInputDialog("Código do Produto " + (i + 1) + ":"));
            int quantidade = Integer.parseInt(JOptionPane.showInputDialog("Quantidade do Produto " + (i + 1) + ":"));
            produtos.put(codigoProduto, quantidade);
        }

        fachada.criarPedido(cpfCliente, codFuncionario, endereco, produtos, formaPagamento);
        JOptionPane.showMessageDialog(null, "Pedido criado com sucesso!");
    }

    private static void listarPedidos() throws SQLException {
        fachada.listarPedidos();
    }

    private static void buscarPedido() throws SQLException, PedidoNaoEncontradoException {
        int codigoPedido = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do pedido:"));
        String pedido = fachada.buscarPedido(codigoPedido);
        JOptionPane.showMessageDialog(null, pedido != null ? pedido : "Pedido não encontrado.");
    }

    private static void adicionarPagamento() throws SQLException, PedidoNaoEncontradoException {
        int codigoPedido = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do pedido:"));
        double valorPagamento = Double.parseDouble(JOptionPane.showInputDialog("Digite o valor do pagamento:"));

        fachada.adicionarPagamento(codigoPedido, valorPagamento);
        JOptionPane.showMessageDialog(null, "Pagamento adicionado com sucesso!");
    }

    private static void atualizarStatusPedido() throws SQLException, PedidoNaoEncontradoException {
        int codigoPedido = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do pedido:"));
        String novoStatus = JOptionPane.showInputDialog("Digite o novo status do pedido:");

        fachada.atualizarStatusPedido(codigoPedido, novoStatus);
        JOptionPane.showMessageDialog(null, "Status do pedido atualizado com sucesso!");
    }
}
