package negocio;

import dados.Cliente;
import dados.ClienteDAO;
import excecoes.ClienteNaoEncontradoException;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ClienteController {

    private static ClienteController instance;
    private ClienteDAO clienteDAO;

    // Construtor privado
    private ClienteController() throws SQLException {
        this.clienteDAO = ClienteDAO.getInstance();
    }

    // Método público para acessar a instância
    public static ClienteController getInstance() throws SQLException {
        if (instance == null) {
            instance = new ClienteController();
        }
        return instance;
    }

    public void cadastrarCliente(Scanner scanner) throws SQLException {
        System.out.print("Digite o nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o CPF do cliente: ");
        String cpf = scanner.nextLine();
        System.out.print("Digite a data de nascimento (dd/MM/yyyy): ");
        String dataNascimentoStr = scanner.nextLine();

        Date dataNascimento = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dataNascimento = sdf.parse(dataNascimentoStr);
        } catch (ParseException e) {
            System.out.println("Formato de data inválido. Tente novamente.");
            return;
        }

        Cliente cliente = new Cliente(nome, cpf, dataNascimento);
        try {
            ClienteDAO.getInstance().cadastrarCliente(cliente);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    public void cadastrarCliente(String nome, String cpf, Date dataNascimento) throws SQLException {
        Cliente cliente = new Cliente(nome, cpf, dataNascimento);
        clienteDAO.cadastrarCliente(cliente);
    }

    public void listarClientes() throws SQLException {
        List<Cliente> clientes = ClienteDAO.getInstance().listarClientes();
        exibirClientes(clientes);
    }

    public Cliente buscarCliente(String cpf) throws ClienteNaoEncontradoException, SQLException {
        Cliente cliente = clienteDAO.buscarClientePorCpf(cpf);

        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente com CPF " + cpf + " não encontrado.");
        }

        // Exibindo as informações do cliente encontrado
        System.out.println("Cliente encontrado:");
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("CPF: " + cliente.getCpf());
        System.out.println("Data de Nascimento: " + cliente.getDataNascimento());

        return cliente;
    }

    public void atualizarCliente(Scanner scanner) throws SQLException, ClienteNaoEncontradoException {
        System.out.print("Digite o CPF do cliente que deseja atualizar: ");
        String cpf = scanner.nextLine();
        Cliente cliente = ClienteDAO.getInstance().buscarClientePorCpf(cpf);

        if (cliente == null) {
            throw new ClienteNaoEncontradoException(cpf);
        }

        System.out.println("Cliente encontrado: " + cliente.getNome());
        System.out.print("Digite o novo nome do cliente: ");
        cliente.setNome(scanner.nextLine());
        System.out.print("Digite a nova data de nascimento (dd/MM/yyyy): ");
        String dataNascimentoStr = scanner.nextLine();

        Date dataNascimento = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dataNascimento = sdf.parse(dataNascimentoStr);
        } catch (ParseException e) {
            System.out.println("Formato de data inválido. Tente novamente.");
            return;
        }
        cliente.setDataNascimento(dataNascimento);

        ClienteDAO.getInstance().atualizarCliente(cliente);
        System.out.println("Cliente atualizado com sucesso.");
    }

    public void exibirClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (Cliente cliente : clientes) {
                System.out.println("Nome: " + cliente.getNome());
                System.out.println("CPF: " + cliente.getCpf());
                System.out.println("Data de Nascimento: " + cliente.getDataNascimento());
                System.out.println("-------------------------");
            }
        }
    }
}
