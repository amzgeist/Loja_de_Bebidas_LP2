package negocio;

import dados.Produto;
import excecoes.ProdutoJaExisteException;
import excecoes.ProdutoNaoEncontradoException;

import java.util.ArrayList;
import java.util.Scanner;

public class ProdutoController {
    private ArrayList<Produto> produtos;
    private static ProdutoController instance;

    private ProdutoController() {
        this.produtos = new ArrayList<>();
        inicializarProdutos();
    }

    public static ProdutoController getInstance() {
        if (instance == null) {
            instance = new ProdutoController();
        }
        return instance;
    }

    public void cadastrarProduto(Scanner scanner) throws ProdutoJaExisteException {
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o preço do produto: ");
        float preco = Float.parseFloat(scanner.nextLine());
        System.out.print("Digite o estoque do produto: ");
        int estoque = Integer.parseInt(scanner.nextLine());

        for (Produto produto : produtos) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                throw new ProdutoJaExisteException("Produto com este nome já existe.");
            }
        }

        Produto produto = new Produto(nome, preco, estoque);
        produtos.add(produto);
        System.out.println("Produto cadastrado com sucesso. Código do Produto: " + produto.getCodigo());
    }

    public void listarProdutos() {
        System.out.println("----- Lista de Produtos -----");
        for (Produto produto : produtos) {
            System.out.println("Código: " + produto.getCodigo());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: R$ " + produto.getPreco());
            System.out.println("Estoque: " + produto.getEstoque());
            System.out.println();
        }
    }

    public Produto buscarProduto(int codigo) throws ProdutoNaoEncontradoException {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == codigo) {
                return produto;
            }
        }
        throw new ProdutoNaoEncontradoException("Produto com código " + codigo + " não encontrado.");
    }

    public void atualizarProduto(Scanner scanner) {
        System.out.print("Digite o código do produto que deseja atualizar: ");
        int codigo = Integer.parseInt(scanner.nextLine());
        try {
            Produto produto = buscarProduto(codigo);
            System.out.println("Produto encontrado:");
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: R$ " + produto.getPreco());
            System.out.println("Estoque: " + produto.getEstoque());
            System.out.println("1. Atualizar Nome");
            System.out.println("2. Atualizar Preço");
            System.out.println("3. Atualizar Estoque");
            System.out.println("4. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    System.out.print("Digite o novo nome: ");
                    String novoNome = scanner.nextLine();
                    produto.setNome(novoNome);
                    System.out.println("Nome do produto atualizado com sucesso.");
                    break;
                case 2:
                    System.out.print("Digite o novo preço: ");
                    float novoPreco = Float.parseFloat(scanner.nextLine());
                    produto.setPreco(novoPreco);
                    System.out.println("Preço do produto atualizado com sucesso.");
                    break;
                case 3:
                    System.out.print("Digite o novo estoque: ");
                    int novoEstoque = Integer.parseInt(scanner.nextLine());
                    produto.setEstoque(novoEstoque);
                    System.out.println("Estoque do produto atualizado com sucesso.");
                    break;
                case 4:
                    System.out.println("Voltando ao menu anterior...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } catch (ProdutoNaoEncontradoException e) {
            System.out.println(e.getMessage());
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
