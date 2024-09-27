package excecoes;

public class FuncionarioNaoEncontradoException extends Exception {
    public FuncionarioNaoEncontradoException(int codigo) {
        super("Funcionário com o código " + codigo + " não encontrado.");
    }
}

