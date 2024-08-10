package negocio;

import dados.Cliente;
import dados.Funcionario;
import excecoes.*;

import java.util.Scanner;

public class Fachada {
    private ClienteController clienteController;
    private FuncionarioController funcionarioController;
    private ProdutoController produtoController;
    private PedidoController pedidoController;

    public Fachada() {
        this.clienteController = ClienteController.getInstance();
        this.funcionarioController = FuncionarioController.getInstance();
        this.produtoController = ProdutoController.getInstance();
        this.pedidoController = PedidoController.getInstance();
    }

    public void cadastrarCliente(Scanner scanner) {
        clienteController.cadastrarCliente(scanner);
    }

    public void listarClientes() {
        clienteController.listarClientes();
    }

    public Cliente buscarCliente(String cpf) throws ClienteNaoEncontradoException {
        return clienteController.buscarCliente(cpf);
    }

    public void cadastrarFuncionario(Scanner scanner) {
        funcionarioController.cadastrarFuncionario(scanner);
    }

    public void listarFuncionarios() {
        funcionarioController.listarFuncionarios();
    }

    public Funcionario buscarFuncionario(int codigo) throws FuncionarioNaoEncontradoException, FuncionarioInativoException {
        return funcionarioController.buscarFuncionario(codigo);
    }

    public void atualizarFuncionario(Scanner scanner) {
        funcionarioController.atualizarFuncionario(scanner);
    }

    public void reativarFuncionario(int codigo) {
        funcionarioController.reativarFuncionario(codigo);
    }

    public void cadastrarProduto(Scanner scanner) throws ProdutoJaExisteException {
        produtoController.cadastrarProduto(scanner);
    }

    public void listarProdutos() {
        produtoController.listarProdutos();
    }

    public void buscarProduto(int codigoProduto) throws ProdutoNaoEncontradoException {
        produtoController.buscarProduto(codigoProduto);
    }

    public void atualizarProduto(Scanner scanner) {
        produtoController.atualizarProduto(scanner);
    }

    public void criarPedido(Scanner scanner) throws ProdutoNaoEncontradoException, FuncionarioInativoException, ClienteNaoEncontradoException {
        pedidoController.criarPedido(scanner, produtoController, clienteController, funcionarioController);
    }

    public void listarPedidos() {
        pedidoController.listarPedidos();
    }

    public void buscarPedido(int numeroPedido, Scanner scanner) throws PedidoNaoEncontradoException {
        pedidoController.buscarPedido(numeroPedido, scanner);
    }
}
