package negocio;

import dados.Produto;
import dados.ProdutoDAO;
import excecoes.ProdutoJaExisteException;
import excecoes.ProdutoNaoEncontradoException;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProdutoController {
    private static ProdutoController instance;
    private ProdutoDAO produtoDAO;

    private ProdutoController() {
        this.produtoDAO = ProdutoDAO.getInstance();
    }

    public static ProdutoController getInstance() {
        if (instance == null) {
            instance = new ProdutoController();
        }
        return instance;
    }


    public void cadastrarProduto(Scanner scanner) throws SQLException, ProdutoJaExisteException {
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o preço do produto: ");
        float preco = Float.parseFloat(scanner.nextLine());
        System.out.print("Digite o estoque do produto: ");
        int estoque = Integer.parseInt(scanner.nextLine());

        Produto produto = new Produto(nome, preco, estoque);
        ProdutoDAO.getInstance().inserirProduto(produto);

        System.out.println("Produto cadastrado com sucesso.");
    }

    public void cadastrarProduto(String nome, float preco, int estoque) throws SQLException, ProdutoJaExisteException {
        Produto produto = new Produto(nome, preco, estoque);
        produtoDAO.inserirProduto(produto);
    }

    public void listarProdutos() throws SQLException {
        List<Produto> produtos = ProdutoDAO.getInstance().listarProdutos();
        exibirProdutos(produtos);
    }

    public Produto buscarProduto(int codigoProduto) throws ProdutoNaoEncontradoException, SQLException {
        Produto produto = produtoDAO.buscarProduto(codigoProduto);

        if (produto != null) {
            System.out.println("Produto encontrado:");
            System.out.println("Código: " + produto.getCodigo());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: R$ " + produto.getPreco());
            System.out.println("Estoque: " + produto.getEstoque());
        } else {
            throw new ProdutoNaoEncontradoException("Produto com código " + codigoProduto + " não encontrado.");
        }
        return produto;
    }

    public void atualizarProduto(Scanner scanner) throws SQLException {
        System.out.print("Digite o código do produto que deseja atualizar: ");
        int codigo = Integer.parseInt(scanner.nextLine());

        Produto produto;
        try {
            produto = produtoDAO.buscarProduto(codigo);
            if (produto == null) {
                throw new ProdutoNaoEncontradoException("Produto não encontrado.");
            }
        } catch (ProdutoNaoEncontradoException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Produto encontrado: ");
        System.out.println("Nome: " + produto.getNome());
        System.out.println("Preço: " + produto.getPreco());
        System.out.println("Estoque: " + produto.getEstoque());

        System.out.println("O que deseja atualizar?");
        System.out.println("1. Nome");
        System.out.println("2. Preço");
        System.out.println("3. Estoque");
        System.out.println("4. Voltar");

        int opcao = Integer.parseInt(scanner.nextLine());

        switch (opcao) {
            case 1:
                System.out.print("Digite o novo nome do produto: ");
                String novoNome = scanner.nextLine();
                produto.setNome(novoNome);
                produtoDAO.atualizarProduto(produto);
                System.out.println("Nome atualizado com sucesso.");
                break;

            case 2:
                System.out.print("Digite o novo preço do produto: ");
                float novoPreco = Float.parseFloat(scanner.nextLine());
                produto.setPreco(novoPreco);
                produtoDAO.atualizarProduto(produto);
                System.out.println("Preço atualizado com sucesso.");
                break;

            case 3:
                System.out.print("Digite o novo estoque do produto: ");
                int novoEstoque = Integer.parseInt(scanner.nextLine());
                produto.setEstoque(novoEstoque);
                produtoDAO.atualizarProduto(produto);
                System.out.println("Estoque atualizado com sucesso.");
                break;

            case 4:
                System.out.println("Voltando ao menu anterior...");
                return;

            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    public void exibirProdutos(List<Produto> produtos) {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (Produto produto : produtos) {
                System.out.println("Código: " + produto.getCodigo());
                System.out.println("Nome: " + produto.getNome());
                System.out.println("Preço: " + produto.getPreco());
                System.out.println("Estoque: " + produto.getEstoque());
                System.out.println("-------------------------");
            }
        }
    }

    public void atualizarProdutoNoBanco(Produto produto) throws SQLException {
        produtoDAO.atualizarProduto(produto);
    }

}
