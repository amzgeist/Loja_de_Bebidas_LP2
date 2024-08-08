package excecoes;

import dados.Funcionario;

public class FuncionarioInativoException extends Exception {
    private Funcionario funcionario;

    public FuncionarioInativoException(String message, Funcionario funcionario) {
        super(message);
        this.funcionario = funcionario;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }
}
