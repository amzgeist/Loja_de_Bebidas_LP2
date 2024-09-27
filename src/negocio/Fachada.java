package negocio;

import dados.*;
import excecoes.*;
import negocio.ClienteController;
import negocio.FuncionarioController;
import negocio.PedidoController;
import negocio.ProdutoController;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Fachada {

    private ClienteController clienteController;
    public FuncionarioController funcionarioController;
    private ProdutoController produtoController;
    private PedidoController pedidoController;

    public Fachada() throws SQLException {
        this.clienteController = ClienteController.getInstance();
        this.funcionarioController = FuncionarioController.getInstance();
        this.produtoController = ProdutoController.getInstance();
        this.pedidoController = PedidoController.getInstance();
    }

    // ----------- Cliente -----------

    public void cadastrarCliente(String nome, String cpf, String dataNascimento) throws SQLException, ParseException {
        // Converter String para Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataNasc = sdf.parse(dataNascimento);  // Aqui converte a String para Date

        clienteController.cadastrarCliente(nome, cpf, dataNasc);  // Agora passamos Date em vez de String
    }

    public String listarClientes() throws SQLException {
        return clienteController.listarClientes();
    }

    public String buscarCliente(String cpf) throws SQLException {
        Cliente cliente = clienteController.buscarClientePorCpf(cpf);
        if (cliente == null) {
            return null;
        }
        return cliente.toString();
    }

    public void atualizarCliente(String cpf, String nome, String dataNascimento) throws SQLException {
        // Convertendo a string de data para Date
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dataNasc = null;
        try {
            dataNasc = dateFormat.parse(dataNascimento);
        } catch (ParseException e) {
            throw new RuntimeException("Data de nascimento inválida.");
        }

        clienteController.atualizarCliente(cpf, nome, dataNasc);
    }

    public void deletarCliente(String cpf) throws SQLException {
        clienteController.deletarCliente(cpf);
    }

    // ----------- Funcionario -----------

    public void cadastrarFuncionario(String nome, String cpf, String tipo, double salario) throws SQLException {
        funcionarioController.cadastrarFuncionario(nome, cpf, tipo, salario);
    }

    public void listarFuncionarios() throws SQLException {
        funcionarioController.listarFuncionarios();
    }

    public Funcionario buscarFuncionario(int codigo) throws SQLException, FuncionarioNaoEncontradoException {
        return funcionarioController.buscarFuncionarioPorCodigo(codigo);
    }

    public void atualizarFuncionario(int codigo, String nome, String cpf, String tipo, double salario) throws SQLException, FuncionarioNaoEncontradoException {
        funcionarioController.atualizarFuncionario(codigo, nome, cpf, tipo, salario);
    }

    public void desativarFuncionario(int codigo) throws SQLException, FuncionarioNaoEncontradoException {
        funcionarioController.desativarFuncionario(codigo);
    }

    // ----------- Produto -----------

    public void cadastrarProduto(String nome, float preco, int estoque) throws SQLException, ProdutoJaExisteException {
        produtoController.cadastrarProduto(nome, preco, estoque);
    }

    public void listarProdutos() throws SQLException {
        produtoController.listarProdutos();
    }

    public String buscarProduto(int codigo) throws SQLException, ProdutoNaoEncontradoException {
        Produto produto = produtoController.buscarProduto(codigo);
        if (produto == null) {
            return null;
        }
        return produto.toString();
    }

    public void atualizarProduto(int codigo, String nome, float preco, int estoque) throws SQLException, ProdutoNaoEncontradoException {
        produtoController.atualizarProduto(codigo, nome, preco, estoque);
    }

    public void deletarProduto(int codigo) throws SQLException, ProdutoNaoEncontradoException {
        produtoController.deletarProduto(codigo);
    }

    // ----------- Pedido -----------

    public void criarPedido(String cpfCliente, int codFuncionario, String endereco, Map<Integer, Integer> produtos, String formaPagamento) throws SQLException, ClienteNaoEncontradoException, FuncionarioNaoEncontradoException, ProdutoNaoEncontradoException {
        pedidoController.criarPedido(cpfCliente, codFuncionario, endereco, produtos, formaPagamento);
    }

    public void listarPedidos() throws SQLException {
        pedidoController.listarPedidos();
    }

    public String buscarPedido(int codigo) throws SQLException, PedidoNaoEncontradoException {
        Pedido pedido = pedidoController.buscarPedidoPorCodigo(codigo);

        if (pedido == null) {
            return null;
        }

        return pedido.toString();
    }

    public void adicionarPagamento(int codigoPedido, double valorPagamento) throws SQLException, PedidoNaoEncontradoException {
        pedidoController.adicionarPagamento(codigoPedido, valorPagamento);
    }

    public void atualizarStatusPedido(int codigoPedido, String novoStatus) throws SQLException, PedidoNaoEncontradoException {
        pedidoController.atualizarStatusPedido(codigoPedido, novoStatus);
    }
}