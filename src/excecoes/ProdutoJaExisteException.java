package excecoes;

public class ProdutoJaExisteException extends Exception {
    public ProdutoJaExisteException(String nome) {
        super("Erro: Produto com o nome '" + nome + "' já existe. Cadastro não realizado.");
    }
}
