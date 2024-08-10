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
    private String status;
    private Date dataHora;

    public Pedido(Cliente cliente, Funcionario funcionario, String endereco, Map<Produto, Integer> produtos, String formaPagamento) {
        this.codigo = proximoCodigo++;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.endereco = endereco;
        this.produtos = produtos;
        this.formaPagamento = formaPagamento;
        this.status = "Pedido Confirmado. Aguardando Faturamento";
        this.dataHora = new Date(); // Armazena a data e hora atual
    }

    public void exibirResumo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("Resumo do Pedido:");
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

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setStatus(String status) {
        this.status = status;
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
