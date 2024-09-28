package negocio;

import dados.Produto;
import dados.ProdutoDAO;
import excecoes.ProdutoJaExisteException;
import excecoes.ProdutoNaoEncontradoException;

import java.sql.SQLException;
import java.util.List;

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
            throw new ProdutoNaoEncontradoException("Produto não encontrado para o código: " + codigo);
        }
        produtoDAO.atualizarProduto(codigo, nome, preco, estoque);
    }

    public void cadastrarProduto(String nome, float preco, int estoque) throws SQLException, ProdutoJaExisteException {
        if (produtoDAO.produtoExiste(nome)) {
            throw new ProdutoJaExisteException("Produto já existe.");
        }
        produtoDAO.inserirProduto(nome, preco, estoque);
    }

    public List<Produto> listarProdutos() throws SQLException {
        return produtoDAO.listarProdutos();
    }

    public void deletarProduto(int codigo) throws SQLException, ProdutoNaoEncontradoException {
        Produto produto = produtoDAO.buscarProdutoPorCodigo(codigo);

        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto com código " + codigo + " não encontrado.");
        }

        produtoDAO.deletarProduto(codigo);
    }
}