package negocio;

import dados.Funcionario;
import excecoes.FuncionarioNaoEncontradoException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FuncionarioController {
    private final List<Funcionario> funcionarios;
    private static FuncionarioController instance;

    private FuncionarioController() {
        funcionarios = new ArrayList<>();
    }

    public static FuncionarioController getInstance() {
        if (instance == null) {
            instance = new FuncionarioController();
        }
        return instance;
    }

    public void cadastrarFuncionario(Scanner scanner) {
        System.out.print("Digite o nome do funcionário: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o CPF do funcionário: ");
        String cpf = scanner.nextLine();

        System.out.print("Digite o tipo do funcionário (1 para Assalariado, 2 para Comissionado): ");
        String tipo = scanner.nextLine();
        tipo = (tipo.equals("1") ? "Assalariado" : "Comissionado");

        System.out.print("Digite o salário do funcionário: ");
        double salario = Double.parseDouble(scanner.nextLine());

        Funcionario funcionario = new Funcionario(nome, cpf, tipo, salario);
        funcionarios.add(funcionario);
        System.out.println("Funcionário cadastrado com sucesso. Código: " + funcionario.getCodigoFunc());
    }

    public void listarFuncionarios() {
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }
        for (Funcionario funcionario : funcionarios) {
            System.out.println(funcionario);
        }
    }

    public Funcionario buscarFuncionario(int codFunc) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getCodigoFunc() == codFunc) {
                if (!funcionario.isAtivo()) {
                    System.out.println("Funcionário está inativo. Deseja reativá-lo? (Sim/Não)");
                    Scanner scanner = new Scanner(System.in);
                    String resposta = scanner.nextLine();
                    if (resposta.equalsIgnoreCase("Sim")) {
                        funcionario.setAtivo(true);
                        System.out.println("Funcionário reativado com sucesso.");
                    }
                    return funcionario;
                }
                // Exibindo informações do funcionário
                System.out.println("Funcionário encontrado:");
                System.out.println("Código: " + funcionario.getCodigoFunc());
                System.out.println("Nome: " + funcionario.getNome());
                System.out.println("CPF: " + funcionario.getCpf());
                System.out.println("Tipo: " + funcionario.getTipo());
                System.out.println("Salário: R$ " + funcionario.getSalario());
                return funcionario; // Retorna o funcionário encontrado
            }
        }
        System.out.println("Funcionário com código " + codFunc + " não encontrado.");
        return null; // Retorna null se não encontrar funcionário
    }

    public void atualizarFuncionario(Scanner scanner) {
        System.out.print("Digite o código do funcionário que deseja atualizar: ");
        int codFunc = Integer.parseInt(scanner.nextLine());
        Funcionario funcionario = null;
        for (Funcionario f : funcionarios) {
            if (f.getCodigoFunc() == codFunc) {
                funcionario = f;
                break;
            }
        }

        if (funcionario == null) {
            System.out.println("Funcionário com código " + codFunc + " não encontrado.");
            return;
        }

        System.out.println("Funcionário encontrado: " + funcionario.getNome());
        System.out.println("Escolha o dado para atualizar:");
        System.out.println("1. Nome");
        System.out.println("2. CPF");
        System.out.println("3. Salário");
        System.out.println("4. Tipo");
        System.out.print("Digite sua escolha: ");
        int escolha = Integer.parseInt(scanner.nextLine());

        switch (escolha) {
            case 1:
                System.out.print("Digite o novo nome: ");
                funcionario.setNome(scanner.nextLine());
                break;
            case 2:
                System.out.print("Digite o novo CPF: ");
                funcionario.setCpf(scanner.nextLine());
                break;
            case 3:
                System.out.print("Digite o novo salário: ");
                funcionario.setSalario(Double.parseDouble(scanner.nextLine()));
                break;
            case 4:
                System.out.print("Digite o novo tipo (Assalariado/Comissionado): ");
                funcionario.setTipo(scanner.nextLine());
                break;
            default:
                System.out.println("Opção inválida.");
                return;
        }

        System.out.println("Atualização realizada com sucesso!");
    }

    public void deletarFuncionario(int codFunc) throws FuncionarioNaoEncontradoException {
        Funcionario funcionario = buscarFuncionario(codFunc);
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException("Funcionário com código " + codFunc + " não encontrado.");
        }
        if (!funcionario.isAtivo()) {
            System.out.println("Funcionário já está inativo.");
            return;
        }

        funcionario.setAtivo(false);
        System.out.println("Funcionário desativado com sucesso.");
    }

}
