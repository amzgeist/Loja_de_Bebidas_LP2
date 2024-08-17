package negocio;

import dados.Funcionario;
import dados.FuncionarioDAO;
import excecoes.FuncionarioNaoEncontradoException;
import excecoes.FuncionarioInativoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FuncionarioController {
    private ArrayList<Funcionario> funcionarios;

    private static FuncionarioController instance;
    private FuncionarioDAO funcionarioDAO;

    private FuncionarioController() {
        this.funcionarioDAO = FuncionarioDAO.getInstance();
    }

    public static FuncionarioController getInstance() {
        if (instance == null) {
            instance = new FuncionarioController();
        }
        return instance;
    }

    private void inicializarFuncionarios() {
        funcionarios.add(new Funcionario("Jose Carlos", "11111122222", "Assalariado", 1600));
        funcionarios.add(new Funcionario("Francisco Souza", "22222233333", "Comissionado", 1400));
        funcionarios.add(new Funcionario("Marta da Silva", "33333344444", "Comissionado", 1450));
    }

    public void cadastrarFuncionario(Scanner scanner) throws SQLException {
        System.out.print("Digite o nome do funcionário: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o CPF do funcionário: ");
        String cpf = scanner.nextLine();
        System.out.print("Digite o tipo do funcionário (1 - Assalariado, 2 - Comissionado): ");
        String tipo = scanner.nextLine().equals("1") ? "Assalariado" : "Comissionado";
        System.out.print("Digite o salário do funcionário: ");
        double salario = Double.parseDouble(scanner.nextLine());

        Funcionario funcionario = new Funcionario(nome, cpf, tipo, salario);
        FuncionarioDAO.getInstance().inserirFuncionario(funcionario);

        System.out.println("Funcionário cadastrado com sucesso.");
    }

    public void listarFuncionarios() throws SQLException {
        List<Funcionario> funcionarios = FuncionarioDAO.getInstance().listarFuncionarios();
        exibirFuncionarios(funcionarios);
    }

    public Funcionario buscarFuncionario(int codigo) throws SQLException, FuncionarioNaoEncontradoException, FuncionarioInativoException {
        Funcionario funcionario = FuncionarioDAO.getInstance().buscarFuncionarioPorCodigo(codigo);

        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(codigo);
        }

        if (!funcionario.isAtivo()) {
            throw new FuncionarioInativoException(funcionario.getNome());
        }

        return funcionario;
    }

    public void atualizarFuncionario(Scanner scanner) throws SQLException, FuncionarioNaoEncontradoException, FuncionarioInativoException {
        System.out.print("Digite o código do funcionário que deseja atualizar: ");
        int codigo = Integer.parseInt(scanner.nextLine());
        Funcionario funcionario = buscarFuncionario(codigo);

        System.out.println("Funcionário encontrado: " + funcionario.getNome());
        System.out.print("Digite o novo nome do funcionário: ");
        funcionario.setNome(scanner.nextLine());
        System.out.print("Digite o novo salário do funcionário: ");
        funcionario.setSalario(Double.parseDouble(scanner.nextLine()));
        System.out.print("Deseja alterar o tipo de funcionário? (1 - Assalariado, 2 - Comissionado): ");
        String novoTipo = scanner.nextLine();
        if (novoTipo.equals("1") || novoTipo.equals("2")) {
            funcionario.setTipo(novoTipo.equals("1") ? "Assalariado" : "Comissionado");
        }

        FuncionarioDAO.getInstance().atualizarFuncionario(funcionario);
        System.out.println("Funcionário atualizado com sucesso.");
    }

    public void exibirFuncionarios(List<Funcionario> funcionarios) {
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
        } else {
            for (Funcionario funcionario : funcionarios) {
                System.out.println("Código: " + funcionario.getCodigoFunc());
                System.out.println("Nome: " + funcionario.getNome());
                System.out.println("CPF: " + funcionario.getCpf());
                System.out.println("Tipo: " + funcionario.getTipo());
                System.out.println("Salário: " + funcionario.getSalario());
                System.out.println("Ativo: " + (funcionario.isAtivo() ? "Sim" : "Não"));
                System.out.println("-------------------------");
            }
        }
    }

    public void reativarFuncionario(int codigo) throws SQLException, FuncionarioNaoEncontradoException {
        Funcionario funcionario = FuncionarioDAO.getInstance().buscarFuncionarioPorCodigo(codigo);
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(codigo);
        }
        if (!funcionario.isAtivo()) {
            funcionario.setAtivo(true);
            FuncionarioDAO.getInstance().atualizarFuncionario(funcionario);
            System.out.println("Funcionário reativado com sucesso.");
        } else {
            System.out.println("Funcionário já está ativo.");
        }
    }

    public void desativarFuncionario(int codigo) throws SQLException, FuncionarioNaoEncontradoException {
        Funcionario funcionario = FuncionarioDAO.getInstance().buscarFuncionarioPorCodigo(codigo);
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(codigo);
        }
        funcionario.setAtivo(false);
        FuncionarioDAO.getInstance().atualizarFuncionario(funcionario);
        System.out.println("Funcionário desativado com sucesso.");
    }
}





    /*public double calcularSalario(Funcionario funcionario, Scanner scanner) {
        double salarioCalculado = 0;
        
        switch (funcionario.getTipo()) {
            case "Assalariado":
                salarioCalculado = funcionario.getSalario();
                break;
            case "Comissionado":
                System.out.print("Digite o valor das vendas realizadas no mês: ");
                double vendas = Double.parseDouble(scanner.nextLine());
                System.out.print("Digite a porcentagem de comissão (em decimal, por exemplo, 0.10 para 10%): ");
                double percentualComissao = Double.parseDouble(scanner.nextLine());
                salarioCalculado = funcionario.getSalario() + (vendas * percentualComissao);
                break;
            case "Por Hora":
                System.out.print("Digite o número de horas trabalhadas: ");
                int horasTrabalhadas = Integer.parseInt(scanner.nextLine());
                salarioCalculado = funcionario.getSalario() * horasTrabalhadas;
                break;
            case "Salário Base e Comissão":
                System.out.print("Digite o valor das vendas realizadas no mês: ");
                double vendasBaseComissao = Double.parseDouble(scanner.nextLine());
                System.out.print("Digite a porcentagem de comissão (em decimal, por exemplo, 0.10 para 10%): ");
                double percentualComissaoBase = Double.parseDouble(scanner.nextLine());
                salarioCalculado = funcionario.getSalario() + (vendasBaseComissao * percentualComissaoBase);
                break;
            default:
                System.out.println("Tipo de funcionário desconhecido.");
        }
        
        return salarioCalculado;
    }*/

