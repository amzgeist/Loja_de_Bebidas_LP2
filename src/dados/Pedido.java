package dados;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Pedido {
    private static int proximoCodigo = 1;
    private int codigo;
    private Cliente cliente;
    private Funcionario funcionario;
    private String endereco;
    private Map<Produto, Integer> produtos;
    private String formaPagamento;
    private String statusPagamento;
    private String status;
    private Date dataHora;

    public Pedido(Cliente cliente, Funcionario funcionario, String endereco, Map<Produto, Integer> produtos, int formaPagamento) {
        this.codigo = proximoCodigo++;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.endereco = endereco;
        this.produtos = produtos;
        setFormaPagamento(formaPagamento);
        this.statusPagamento = "Aguardando pagamento";
        this.status = "Aguardando confirmação";
        this.dataHora = new Date(); // Armazena a data e hora atual
    }

    private void setFormaPagamento(int formaPagamento) {
        switch (formaPagamento) {
            case 1:
                this.formaPagamento = "Cartão de Crédito";
                break;
            case 2:
                this.formaPagamento = "Cartão de Débito";
                break;
            case 3:
                this.formaPagamento = "Pix";
                break;
            case 4:
                this.formaPagamento = "Dinheiro";
                break;
            default:
                this.formaPagamento = "Não especificado";
                break;
        }
    }

    public void exibirResumo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("-----------------------------");
        System.out.println("Código do Pedido: " + codigo);
        System.out.println("Data e Hora: " + sdf.format(dataHora));
        System.out.println("Cliente: " + cliente.getNome() + " (CPF: " + cliente.getCpf() + ")");
        System.out.println("Funcionário: " + funcionario.getNome() + " (Código: " + funcionario.getCodigoFunc() + ")");
        System.out.println("Endereço: " + endereco);
        System.out.println("Itens do Pedido:");
        double total = 0;
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            Produto produto = entry.getKey();
            int quantidade = entry.getValue();
            double subtotal = produto.getPreco() * quantidade;
            System.out.println("  Produto: " + produto.getNome() + " (Código: " + produto.getCodigo() + ")");
            System.out.println("  Quantidade: " + quantidade);
            System.out.println("  Subtotal: R$ " + subtotal);
            total += subtotal;
        }
        System.out.println("Total do Pedido: R$ " + total);
        System.out.println("Forma de Pagamento: " + formaPagamento);
        System.out.println("Status do Pagamento: " + statusPagamento);
        System.out.println("Status: " + status);
    }

    public int getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public String getEndereco() {
        return endereco;
    }

    public Map<Produto, Integer> getProdutos() {
        return produtos;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public double calcularTotal() {
        return produtos.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPreco() * entry.getValue())
                .sum();
    }

    @Override
    public String toString() {
        return "Pedido{Código: " + codigo + ", Cliente: " + cliente.getNome() +
                ", Funcionário: " + funcionario.getNome() + ", Endereço: " + endereco +
                ", Forma de Pagamento: " + formaPagamento + ", Total: R$" + calcularTotal() + "}";
    }
}
