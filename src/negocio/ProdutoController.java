package negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dados.Produto;
import excecoes.ProdutoJaExisteException;

public class ProdutoController {
    private final List<Produto> produtos;
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

    // Método para cadastrar um novo produto, aceitando Scanner como argumento
    public void cadastrarProduto(Scanner scanner) throws ProdutoJaExisteException {
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o preço do produto: ");
        float preco = Float.parseFloat(scanner.nextLine());

        System.out.print("Digite o estoque inicial do produto: ");
        int estoque = Integer.parseInt(scanner.nextLine());

        // Verificação se o produto já existe
        if (produtoExiste(nome)) {
            throw new ProdutoJaExisteException("Produto com o nome " + nome + " já está cadastrado.");
        }

        Produto produto = new Produto(nome, preco, estoque);
        produtos.add(produto);
        System.out.println("Produto cadastrado com sucesso. Código do Produto: " + produto.getCodigo());
    }

    // Método para verificar se um produto com o mesmo nome já existe
    private boolean produtoExiste(String nome) {
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
        } else {
            for (Produto produto : produtos) {
                System.out.println("Código: " + produto.getCodigo());
                System.out.println("Nome: " + produto.getNome());
                System.out.println("Preço: R$ " + produto.getPreco());
                System.out.println("Estoque: " + produto.getEstoque());
                System.out.println();
            }
        }
    }

    public Produto buscarProduto(int codigo) {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == codigo) {
                return produto;
            }
        }
        return null;
    }

    public void atualizarEstoque(int codigo, int novoEstoque) {
        Produto produto = buscarProduto(codigo);
        if (produto != null) {
            produto.setEstoque(novoEstoque);
            System.out.println("Estoque atualizado com sucesso para o Produto: " + produto.getNome());
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private void inicializarProdutos() {
        produtos.add(new Produto("Coca cola lata", 3.50f, 30));
        produtos.add(new Produto("Fanta lata", 3.00f, 20));
        produtos.add(new Produto("Brahma 600Ml", 6.00f, 30));
        produtos.add(new Produto("Monster 473Ml", 9.00f, 25));
        produtos.add(new Produto("Água sem gás 500Ml", 1.5f, 50));
    }
}
