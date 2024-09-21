package dados;

public class Funcionario {
    private static int proximoCodigo = 1;
    private int codigoFunc;
    private String nome;
    private String tipo;
    private String cpf;
    private double salario;
    private boolean ativo;

    public Funcionario(int codigo, String nome, String cpf, String tipo, double salario, boolean ativo) {
        this.codigoFunc = codigo;
        this.nome = nome;
        this.cpf = cpf;
        this.tipo = tipo;
        this.salario = salario;
        this.ativo = ativo;
    }

    public int getCodigoFunc() {
        return codigoFunc;
    }
    private int gerarCodigoUnico() {
        return proximoCodigo++;
    }

    public void setCodigoFunc(int codigoFunc) {
        this.codigoFunc = codigoFunc;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Funcionário{Código: " + codigoFunc + ", Nome: " + nome + ", CPF: " + cpf +
                ", Tipo: " + tipo + ", Salário: R$" + salario + ", Ativo: " + ativo + "}";
    }
}
