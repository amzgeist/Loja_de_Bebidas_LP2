package Controllers;

import Entities.Funcionario;

import java.util.ArrayList;

public class FuncionarioController {
    final ArrayList<Funcionario> funcionarios = new ArrayList<>();

    public void cadastrarFuncionario(String nome, String CPF, String tipo, double salario) {

        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getCPF().equals(CPF)) {
                System.out.println("CPF já cadastrado.");
                return;
            }
        }

        funcionarios.add(new Funcionario(nome, CPF, tipo, salario));
        System.out.println("Funcionário cadastrado com sucesso.");
    }

    public void deletarFuncionario(int codFunc) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getCodFunc() == codFunc) {
                funcionario.setAtivo(false);
                System.out.println("Funcionário removido com sucesso.");
                return;
            }
        }
        System.out.println("Funcionário não encontrado.");
    }

    public void buscarFuncionario(int codFunc) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getCodFunc() == codFunc) {
                if (!funcionario.isAtivo()) {
                    throw new IllegalArgumentException("Funcionário inativo");
                }
                System.out.println("Nome: " + funcionario.getNome());
                System.out.println("CPF: " + funcionario.getCPF());
                System.out.println("Tipo: " + funcionario.getTipo());
                System.out.println("Salário: R$ " + funcionario.getSalario());
                return;
            }
        }
        throw new IllegalArgumentException("Funcionário não existe.");
    }

    public void alterarFuncionario(int codFunc, String nome, String CPF, double salario) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getCodFunc() == codFunc) {
                for (Funcionario outroFuncionario : funcionarios) {
                    if (outroFuncionario.getCPF().equals(CPF) && outroFuncionario.getCodFunc() != codFunc) {
                        System.out.println("CPF já cadastrado.");
                        return;
                    }
                }
                funcionario.nome = nome;
                funcionario.CPF = CPF;
                funcionario.setSalario(salario);
                System.out.println("Funcionário atualizado com sucesso.");
                return;
            }
        }
        System.out.println("Funcionário não encontrado.");
    }

    public void listarFuncionarios() {
        boolean algumAtivo = false;
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.isAtivo()) {
                algumAtivo = true;
                System.out.println("Código do Funcionário: " + funcionario.getCodFunc());
                System.out.println("Nome: " + funcionario.getNome());
                System.out.println("CPF: " + funcionario.getCPF());
                System.out.println("Tipo: " + funcionario.getTipo());
                System.out.println("Salário: R$ " + funcionario.getSalario());
                System.out.println("---------------------------------------");
            }
        }
        if (!algumAtivo) {
            System.out.println("Nenhum funcionário cadastrado ou todos estão inativos.");
        }
    }

    public void reativaFuncionario(int codFunc) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getCodFunc() == codFunc) {
                funcionario.setAtivo(true);
                System.out.println("Funcionário reativado com sucesso.");
                return;
            }
        }
        System.out.println("Funcionário não encontrado.");
    }
}