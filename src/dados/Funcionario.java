package dados;

public class Funcionario extends Pessoa {
    private static int proximoCodigo = 1;
    private final int codFunc;
    private boolean ativo;
    private String tipo;
    private double salario;

    public Funcionario(String nome, String CPF, String tipo, double salario) {
        super(nome, CPF);
        this.codFunc = proximoCodigo++;
        this.ativo = true;
        this.tipo = tipo;
        this.salario = salario;
    }

    public int getCodFunc() {
        return codFunc;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getSalario() {
        return salario;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getCPF() {
        return CPF;
    }

    public void setNomeFuncionario(String nome) {
    }

    public void setCPFFuncionario(String cpf) {
    }
}
