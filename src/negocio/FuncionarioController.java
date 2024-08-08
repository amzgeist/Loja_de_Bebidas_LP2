package negocio;

import dados.Funcionario;
import excecoes.FuncionarioNaoEncontradoException;
import excecoes.FuncionarioInativoException;

import java.util.ArrayList;
import java.util.Scanner;

public class FuncionarioController {
    private ArrayList<Funcionario> funcionarios;
    private static FuncionarioController instance;

    private FuncionarioController() {
        this.funcionarios = new ArrayList<>();
        inicializarFuncionarios();
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
        int tipo = Integer.parseInt(scanner.nextLine());
        String tipoStr = tipo == 1 ? "Assalariado" : "Comissionado";
        System.out.print("Digite o salário do funcionário: ");
        double salario = Double.parseDouble(scanner.nextLine());

        Funcionario funcionario = new Funcionario(nome, cpf, tipoStr, salario);
        funcionarios.add(funcionario);
        System.out.println("Funcionário cadastrado com sucesso. Código do Funcionário: " + funcionario.getCodigoFunc());
    }

    public void listarFuncionarios() {
        System.out.println("----- Lista de Funcionários -----");
        for (Funcionario funcionario : funcionarios) {
            System.out.println("Código: " + funcionario.getCodigoFunc());
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("CPF: " + funcionario.getCpf());
            System.out.println("Tipo: " + funcionario.getTipo());
            System.out.println("Salário: R$ " + funcionario.getSalario());
            System.out.println("Ativo: " + (funcionario.isAtivo() ? "Sim" : "Não"));
            System.out.println();
        }
    }

    public Funcionario buscarFuncionario(int codigo) throws FuncionarioNaoEncontradoException, FuncionarioInativoException {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getCodigoFunc() == codigo) {
                if (!funcionario.isAtivo()) {
                    throw new FuncionarioInativoException("Funcionário " + funcionario.getNome() + " está inativo.", funcionario);
                }
                return funcionario;
            }
        }
        throw new FuncionarioNaoEncontradoException("Funcionário com código " + codigo + " não encontrado.");
    }

    public void atualizarFuncionario(Scanner scanner) {
        System.out.print("Digite o código do funcionário que deseja atualizar: ");
        int codigo = Integer.parseInt(scanner.nextLine());
        try {
            Funcionario funcionario = buscarFuncionario(codigo);
            System.out.println("Funcionário encontrado:");
            System.out.println("Nome: " + funcionario.getNome());
            System.out.println("CPF: " + funcionario.getCpf());
            System.out.println("Tipo: " + funcionario.getTipo());
            System.out.println("Salário: R$ " + funcionario.getSalario());
            System.out.println("1. Atualizar Nome");
            System.out.println("2. Atualizar CPF");
            System.out.println("3. Atualizar Tipo");
            System.out.println("4. Atualizar Salário");
            System.out.println("5. Desativar Funcionário");
            System.out.println("6. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    System.out.print("Digite o novo nome: ");
                    String novoNome = scanner.nextLine();
                    funcionario.setNome(novoNome);
                    System.out.println("Nome do funcionário atualizado com sucesso.");
                    break;
                case 2:
                    System.out.print("Digite o novo CPF: ");
                    String novoCpf = scanner.nextLine();
                    funcionario.setCpf(novoCpf);
                    System.out.println("CPF do funcionário atualizado com sucesso.");
                    break;
                case 3:
                    System.out.print("Digite o novo tipo do funcionário (1 para Assalariado, 2 para Comissionado): ");
                    int novoTipo = Integer.parseInt(scanner.nextLine());
                    String novoTipoStr = novoTipo == 1 ? "Assalariado" : "Comissionado";
                    funcionario.setTipo(novoTipoStr);
                    System.out.println("Tipo do funcionário atualizado com sucesso.");
                    break;
                case 4:
                    System.out.print("Digite o novo salário: ");
                    double novoSalario = Double.parseDouble(scanner.nextLine());
                    funcionario.setSalario(novoSalario);
                    System.out.println("Salário do funcionário atualizado com sucesso.");
                    break;
                case 5:
                    desativarFuncionario(funcionario);
                    break;
                case 6:
                    System.out.println("Voltando ao menu anterior...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } catch (FuncionarioNaoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (FuncionarioInativoException e) {
            System.out.println(e.getMessage());
            System.out.print("Deseja reativar o funcionário " + e.getFuncionario().getNome() + "? (Sim/Não): ");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("Sim")) {
                reativarFuncionario(e.getFuncionario().getCodigoFunc());
            }
        }
    }

    public void desativarFuncionario(Funcionario funcionario) {
        if (funcionario.isAtivo()) {
            funcionario.setAtivo(false);
            System.out.println("Funcionário desativado com sucesso.");
        } else {
            System.out.println("Funcionário já está inativo.");
        }
    }

    public void reativarFuncionario(int codigo) {
        try {
            Funcionario funcionario = buscarFuncionario(codigo);
            if (!funcionario.isAtivo()) {
                funcionario.setAtivo(true);
                System.out.println("Funcionário reativado com sucesso.");
            } else {
                System.out.println("Funcionário já está ativo.");
            }
        } catch (FuncionarioNaoEncontradoException | FuncionarioInativoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void inicializarFuncionarios() {
        funcionarios.add(new Funcionario("Jose Carlos", "11111122222", "Assalariado", 1600));
        funcionarios.add(new Funcionario("Francisco Souza", "22222233333", "Comissionado", 1400));
        funcionarios.add(new Funcionario("Marta da Silva", "33333344444", "Comissionado", 1450));
    }
}
