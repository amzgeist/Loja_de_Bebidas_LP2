package Entities;

public class Funcionario extends Pessoa {
    private static int proximoCodigo = 1;
    final int codFunc;
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

    public String getTipo() {
        return tipo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
