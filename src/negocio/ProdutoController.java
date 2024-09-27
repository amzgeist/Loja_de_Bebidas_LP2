package negocio;

import dados.Produto;
import dados.ProdutoDAO;
import excecoes.ProdutoJaExisteException;
import excecoes.ProdutoNaoEncontradoException;

import java.sql.SQLException;

public class ProdutoController {
    private static ProdutoController instance;
    private ProdutoDAO produtoDAO;

    private ProdutoController() {
        produtoDAO = ProdutoDAO.getInstance();
    }

    public static ProdutoController getInstance() {
        if (instance == null) {
            instance = new ProdutoController();
        }
        return instance;
    }

    public Produto buscarProduto(int codigo) throws SQLException, ProdutoNaoEncontradoException {
        Produto produto = produtoDAO.buscarProdutoPorCodigo(codigo);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto com código " + codigo + " não encontrado.");
        }
        return produto;
    }

    public void atualizarProduto(int codigo, String nome, float preco, int estoque) throws SQLException, ProdutoNaoEncontradoException {
        Produto produto = produtoDAO.buscarProdutoPorCodigo(codigo);

        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto com código " + codigo + " não encontrado.");
        }

        // Atualize os dados do produto
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setEstoque(estoque);

        // Atualiza no banco de dados
        produtoDAO.atualizarProduto(produto);
    }

    public void cadastrarProduto(String nome, float preco, int estoque) throws SQLException, ProdutoJaExisteException {
        if (produtoDAO.produtoExiste(nome)) {
            throw new ProdutoJaExisteException("Produto já existe.");
        }
        produtoDAO.inserirProduto(nome, preco, estoque);
    }

    public void listarProdutos() throws SQLException {
        produtoDAO.listarProdutos();
    }

    public void deletarProduto(int codigo) throws SQLException, ProdutoNaoEncontradoException {
        Produto produto = produtoDAO.buscarProdutoPorCodigo(codigo);

        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto com código " + codigo + " não encontrado.");
        }

        produtoDAO.deletarProduto(codigo);
    }
}