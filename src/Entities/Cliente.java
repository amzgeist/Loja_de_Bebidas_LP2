package Entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cliente extends Pessoa {
    public Date dataNascimento;

    public Cliente(String nome, String CPF, Date dataNascimento) {
        super(nome, CPF);
        this.dataNascimento = dataNascimento;
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
