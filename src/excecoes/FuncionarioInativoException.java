package excecoes;

import dados.Funcionario;

public class FuncionarioInativoException extends Exception {
    private Funcionario funcionario;


    public FuncionarioInativoException(String nomeFuncionario) {
        super("Funcionário " + nomeFuncionario + " está inativo.");
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }
}
