package negocio;

import dados.*;
import excecoes.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Scanner;

public class Fachada {
    private ClienteController clienteController;
    private FuncionarioController funcionarioController;
    private ProdutoController produtoController;
    private static PedidoController pedidoController;

    public Fachada() throws SQLException {
        this.clienteController = ClienteController.getInstance();
        this.funcionarioController = FuncionarioController.getInstance();
        this.produtoController = ProdutoController.getInstance();
        this.pedidoController = PedidoController.getInstance();
    }

    // Métodos relacionados a Clientes
    public void cadastrarCliente(Scanner scanner) throws SQLException {
        clienteController.cadastrarCliente(scanner);
    }

    public void cadastrarCliente(String nome, String cpf, String dataNascimento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            clienteController.cadastrarCliente(nome, cpf, sdf.parse(dataNascimento));
        } catch (ParseException | SQLException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    public void listarClientes() throws SQLException {
        clienteController.listarClientes();
    }

    public Cliente buscarCliente(String cpf) throws ClienteNaoEncontradoException, SQLException {
        return clienteController.buscarCliente(cpf);
    }

    public void atualizarCliente(Scanner scanner) throws SQLException {
        clienteController.atualizarCliente(scanner);
    }

    // Métodos relacionados a Funcionários
    public void cadastrarFuncionario(Scanner scanner) throws SQLException {
        funcionarioController.cadastrarFuncionario(scanner);
    }

    public void cadastrarFuncionario(String nome, String cpf, String tipo, double salario) throws SQLException {
        funcionarioController.cadastrarFuncionario(nome, cpf, tipo, salario);
    }

    public void listarFuncionarios() throws SQLException {
        funcionarioController.listarFuncionarios();
    }

    public Funcionario buscarFuncionario(int codigo) throws SQLException, FuncionarioNaoEncontradoException, FuncionarioInativoException {
        return funcionarioController.buscarFuncionario(codigo);
    }

    public void atualizarFuncionario(Scanner scanner) throws SQLException, FuncionarioInativoException {
        funcionarioController.atualizarFuncionario(scanner);
    }

    public void reativarFuncionario(int codigo) throws SQLException {
        funcionarioController.reativarFuncionario(codigo);
    }

    public void desativarFuncionario(int codigo) throws SQLException {
        funcionarioController.desativarFuncionario(codigo);
    }

    // Métodos relacionados a Produtos
    public void cadastrarProduto(Scanner scanner) throws SQLException, ProdutoJaExisteException {
        produtoController.cadastrarProduto(scanner);
    }

    public void cadastrarProduto(String nome, float preco, int estoque) {
        try {
            produtoController.cadastrarProduto(nome, preco, estoque);
        } catch (SQLException | ProdutoJaExisteException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    public void listarProdutos() throws SQLException {
        produtoController.listarProdutos();
    }

    public Produto buscarProduto(int codigoProduto) throws ProdutoNaoEncontradoException, SQLException {
        return produtoController.buscarProduto(codigoProduto); // Retorno do produto
    }

    public void atualizarProduto(Scanner scanner) throws SQLException {
        produtoController.atualizarProduto(scanner);
    }

    // Métodos relacionados a Pedidos
    public void criarPedido(Scanner scanner) throws SQLException, ProdutoNaoEncontradoException, FuncionarioInativoException, ClienteNaoEncontradoException {
        pedidoController.criarPedido(scanner, produtoController, clienteController, funcionarioController);
    }

    public void criarPedido(String cpfCliente, int codFuncionario, String endereco, Map<Integer, Integer> produtos, String formaPagamento)
            throws SQLException, ClienteNaoEncontradoException, FuncionarioNaoEncontradoException, ProdutoNaoEncontradoException {
        pedidoController.criarPedido(cpfCliente, codFuncionario, endereco, produtos, formaPagamento);
    }

    public void listarPedidos() throws SQLException {
        pedidoController.listarPedidos();
    }

    public void exibirDetalhesPedido(int codigoPedido, Scanner scanner) throws PedidoNaoEncontradoException, SQLException {
        Pedido pedido = pedidoController.buscarPedido(codigoPedido, scanner);
        pedidoController.exibirDetalhesPedido(pedido, scanner);
    }

    public void buscarPedido(int numeroPedido, Scanner scanner) throws SQLException, PedidoNaoEncontradoException {
        pedidoController.buscarPedido(numeroPedido, scanner);
    }

    public void atualizarStatusPedido(int codigoPedido, Scanner scanner) throws SQLException, PedidoNaoEncontradoException {
        pedidoController.atualizarStatusPedido(codigoPedido, scanner);
    }

    public static void adicionarPagamento(int codigoPedido, Scanner scanner) throws SQLException, PedidoNaoEncontradoException {
        pedidoController.adicionarPagamento(codigoPedido, scanner);
    }
}
