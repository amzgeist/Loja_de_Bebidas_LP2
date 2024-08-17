package excecoes;

public class FuncionarioNaoEncontradoException extends RuntimeException {
    public FuncionarioNaoEncontradoException(int codigo) {
        super("Funcionário com código " + codigo + " não encontrado.");
    }
}
