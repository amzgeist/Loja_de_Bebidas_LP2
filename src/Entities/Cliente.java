package Entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cliente {
    public String nome;
    public Date dataNascimento;
    private String CPF;

    public Cliente(String nome, Date dataNascimento, String CPF) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.CPF = CPF;
    }

    public String getNome() {
        return nome;
    }

    public String getDataNascimentoFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dataNascimento);
    }

    public String getCPF() {
        return CPF;
    }
}
