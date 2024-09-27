package negocio;

import dados.ConexaoDB;
import dados.Funcionario;
import dados.FuncionarioDAO;
import excecoes.FuncionarioNaoEncontradoException;
import excecoes.FuncionarioInativoException;

import java.sql.*;
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

    public void cadastrarFuncionario(Scanner scanner) {
        try {
            System.out.println("Digite o nome do funcionário:");
            String nome = scanner.nextLine();

            System.out.println("Digite o CPF do funcionário:");
            String cpf = scanner.nextLine();

            System.out.println("Digite o tipo do funcionário (Assalariado/Comissionado):");
            String tipo = scanner.nextLine();

            System.out.println("Digite o salário do funcionário:");
            double salario = Double.parseDouble(scanner.nextLine());

            int codigofunc = funcionarioDAO.gerarCodigoUnico();

            boolean statusativo = true;

            Funcionario funcionario = new Funcionario(codigofunc,nome, cpf, tipo, salario, statusativo);

            funcionarioDAO.inserirFuncionario(funcionario);
            System.out.println("Funcionário cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    public void cadastrarFuncionario(String nome, String cpf, String tipo, double salario) {
        try {
            // Buscar o último código gerado
            String ultimoCodigo = funcionarioDAO.buscarUltimoCodigo();

            // Gerar o novo código incrementando o último código
            int novoCodigoFunc = gerarCodigoCrescente(ultimoCodigo);

            boolean ativo = true; // O funcionário é cadastrado como ativo por padrão

            // Criando o objeto Funcionario com os 6 argumentos
            Funcionario funcionario = new Funcionario(novoCodigoFunc, nome, cpf, tipo, salario, ativo);

            // Inserindo no banco de dados
            funcionarioDAO.inserirFuncionario(funcionario);
            System.out.println("Funcionário cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    private int gerarCodigoCrescente(String ultimoCodigo) {
        // Converter o último código em número e incrementar
        int proximoCodigo = Integer.parseInt(ultimoCodigo) + 1;
        return proximoCodigo; // Retorna como String
    }

    public List<Funcionario> listarFuncionarios() throws SQLException {
        return funcionarioDAO.listarFuncionarios();
    }

    public Funcionario buscarFuncionarioPorCodigo(int codigo) throws SQLException, FuncionarioNaoEncontradoException {
        Funcionario funcionario = funcionarioDAO.buscarFuncionarioPorCodigo(codigo);
        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(codigo);
        }
        return funcionario;
    }

    public void atualizarFuncionario(int codigo, String nome, String cpf, String tipo, double salario) throws SQLException, FuncionarioNaoEncontradoException {
        Funcionario funcionario = funcionarioDAO.buscarFuncionarioPorCodigo(codigo);

        if (funcionario == null) {
            throw new FuncionarioNaoEncontradoException(codigo);
        }

        funcionario.setNome(nome);
        funcionario.setCpf(cpf);
        funcionario.setTipo(tipo);
        funcionario.setSalario(salario);

        funcionarioDAO.atualizarFuncionario(funcionario);
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

