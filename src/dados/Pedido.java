package dados;

import java.util.HashMap;
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
    private String statusPagamento;

    public Pedido(Cliente cliente, Funcionario funcionario, String endereco, Map<Produto, Integer> produtos, int formaPagamento) {
        this.codigo = proximoCodigo++;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.endereco = endereco;
        this.produtos = new HashMap<>(produtos);
        setFormaPagamento(formaPagamento);
        this.status = "Aguardando confirmação";
        this.statusPagamento = "Aguardando pagamento";
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
