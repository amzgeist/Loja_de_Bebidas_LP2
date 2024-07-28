package negocio;

import dados.Produto;
import excecoes.ProdutoJaExisteException;
import java.util.ArrayList;

public class ProdutoController {
    private ArrayList<Produto> produtos;
    private static ProdutoController instance;

    private ProdutoController() {
        produtos = new ArrayList<>();
        inicializarProdutos();
    }

    public static ProdutoController getInstance() {
        if (instance == null) {
            instance = new ProdutoController();
        }
        return instance;
    }

    public void cadastrarProduto(String nome, float preco, int estoque) throws ProdutoJaExisteException {
        if (preco <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser um valor positivo.");
        }
        if (estoque < 0) {
            throw new IllegalArgumentException("O estoque do produto deve ser um valor não negativo.");
        }

        // Verifica se um produto com o mesmo nome já existe
        if (produtoJaExiste(nome)) {
            throw new ProdutoJaExisteException(nome);
        }

        Produto produto = new Produto(nome, preco, estoque);
        produtos.add(produto);
        System.out.println("Produto cadastrado com sucesso. Código do Produto: " + produto.getCodigo());
    }

    private boolean produtoJaExiste(String nome) {
        for (Produto produto : produtos) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                return true;
            }
        }
        return false;
    }

    public void listarProdutos() {
        System.out.println("----- Lista de Produtos -----");
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        }
        for (Produto produto : produtos) {
            System.out.println("Código: " + produto.getCodigo());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: R$ " + produto.getPreco());
            System.out.println("Estoque: " + produto.getEstoque());
            System.out.println();
        }
    }

    public Produto buscarProdutoPorCodigo(int codigo) {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == codigo) {
                return produto;
            }
        }
        return null;
    }

    public void atualizarProduto(int codigo, String novoNome, float novoPreco, int novoEstoque) {
        Produto produto = buscarProdutoPorCodigo(codigo);
        if (produto != null) {
            produto.setNome(novoNome);
            produto.setPreco(novoPreco);
            produto.setEstoque(novoEstoque);
            System.out.println("Produto atualizado com sucesso para: " + produto.getNome());
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public void apagarProduto(int codigoProduto) {
        Produto produto = buscarProdutoPorCodigo(codigoProduto);
        if (produto != null) {
            produtos.remove(produto);
            System.out.println("Produto removido com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private void inicializarProdutos() {
        produtos.add(new Produto("Coca cola lata", 3.50f, 30));
        produtos.add(new Produto("Fanta lata", 3.00f, 20));
        produtos.add(new Produto("Brahma 600Ml", 6.00f, 30));
        produtos.add(new Produto("Monster 473Ml", 9.00f, 25));
        produtos.add(new Produto("Água sem gás 500Ml", 1.50f, 50));
    }
}
