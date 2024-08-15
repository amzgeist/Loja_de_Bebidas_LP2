package negocio;

import dados.Cliente;
import dados.Funcionario;
import excecoes.*;
import dados.Pedido;

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

    public void cancelarPedido(int codigoPedido) throws PedidoNaoEncontradoException {
        pedidoController.cancelarPedido(codigoPedido);
    }


    public Pedido buscarPedido(int codigoPedido) throws PedidoNaoEncontradoException {
        return pedidoController.buscarPedido(codigoPedido);
    }

    public void exibirDetalhesPedido(int numeroPedido, Scanner scanner) throws PedidoNaoEncontradoException {
        Pedido pedido = pedidoController.buscarPedido(numeroPedido);  // Busca o pedido usando o número
        pedidoController.exibirDetalhesPedido(pedido, scanner);  // Exibe os detalhes do pedido
    }

    public void adicionarPagamento(Pedido pedido, Scanner scanner) {
        pedidoController.adicionarPagamento(pedido, scanner);
    }

    public void calcularSalarioFuncionario(int codigo, Scanner scanner) {
        try {
            Funcionario funcionario = funcionarioController.buscarFuncionario(codigo);
            double salarioCalculado = 0;
            
            switch (funcionario.getTipo()) {
                case "Assalariado":
                    salarioCalculado = funcionario.getSalario();
                    break;
                case "Comissionado":
                    System.out.print("Digite o valor das vendas realizadas no mês: ");
                    double vendas = Double.parseDouble(scanner.nextLine());
                    System.out.print("Digite a porcentagem de comissão (em decimal, por exemplo, 0.10 para 10%): ");
                    double percentualComissao = Double.parseDouble(scanner.nextLine());
                    salarioCalculado = funcionario.getSalario() + (vendas * percentualComissao);
                    break;
                case "Por Hora":
                    System.out.print("Digite o número de horas trabalhadas: ");
                    int horasTrabalhadas = Integer.parseInt(scanner.nextLine());
                    salarioCalculado = funcionario.getSalario() * horasTrabalhadas;
                    break;
                case "Salário Base e Comissão":
                    System.out.print("Digite o valor das vendas realizadas no mês: ");
                    double vendasBaseComissao = Double.parseDouble(scanner.nextLine());
                    System.out.print("Digite a porcentagem de comissão (em decimal, por exemplo, 0.10 para 10%): ");
                    double percentualComissaoBase = Double.parseDouble(scanner.nextLine());
                    salarioCalculado = funcionario.getSalario() + (vendasBaseComissao * percentualComissaoBase);
                    break;
                default:
                    System.out.println("Tipo de funcionário desconhecido.");
            }
            
            System.out.println("Salário Calculado: R$ " + salarioCalculado);
        } catch (FuncionarioNaoEncontradoException | FuncionarioInativoException e) {
            System.out.println(e.getMessage());
        }
    }
}
