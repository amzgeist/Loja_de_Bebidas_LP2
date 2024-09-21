package dados;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Pedido {
    private static final double JUROS_ATRASO = 0.03; // 3% de juros por atraso
    private static final int DIAS_VENCIMENTO = 5; // Pedido vence em 5 dias
    private static final int DIAS_MULTA = 10; // Multa aplicada a cada 10 dias de atraso

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
    private Date dataVencimento;
    private double valorRecebido;

    public Pedido(Cliente cliente, Funcionario funcionario, String endereco, Map<Produto, Integer> produtos, String formaPagamento) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.endereco = endereco;
        this.produtos = produtos;
        this.formaPagamento = formaPagamento;
        this.statusPagamento = "Aguardando pagamento";
        this.status = "Pedido Confirmado. Aguardando Faturamento";
        this.dataHora = new Date(); // Armazena a data e hora atual
        this.dataVencimento = calculateDataVencimento(); // Set due date
        this.valorRecebido = 0.0;
        this.statusPagamento = "Aguardando pagamento";
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

        total += calcularJurosAtraso(); // Adiciona juros ao total, se houver

        System.out.println("Total do Pedido: R$ " + total);
        System.out.println("Valor Recebido: R$ " + valorRecebido);
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

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public double getValorRecebido() {
        return valorRecebido;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setProdutos(Map<Produto, Integer> produtos) {
        this.produtos = produtos;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }


    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public void adicionarPagamento(double valorPagamento) {
        if (valorPagamento <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        double totalPedido = calcularTotal();
        if (valorRecebido + valorPagamento > totalPedido) {
            throw new IllegalArgumentException("O valor do pagamento não pode exceder o total do pedido. Valor restante: R$ " + (totalPedido - valorRecebido));
        }
        valorRecebido += valorPagamento;
        if (valorRecebido == totalPedido) {
            status = "Pago";
            statusPagamento = "Pedido Confirmado. Aguardando Faturamento";
        }
    }

    public double calcularTotal() {
        double total = produtos.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPreco() * entry.getValue())
                .sum();
        return total + calcularJurosAtraso();
    }

    private double calcularJurosAtraso() {
        long diferencaDias = (new Date().getTime() - dataHora.getTime()) / (1000 * 60 * 60 * 24);
        if (diferencaDias > DIAS_VENCIMENTO) {
            if (diferencaDias > DIAS_VENCIMENTO + DIAS_MULTA) {
                int periodosDeMulta = (int) ((diferencaDias - DIAS_VENCIMENTO) / DIAS_MULTA);
                double valorMulta = periodosDeMulta * JUROS_ATRASO * calcularTotal();
                System.out.println("Multa por atraso aplicada: R$ " + valorMulta);
                return valorMulta;
            } else {
                statusPagamento = "Atrasado";
            }
        }
        return 0;
    }

    private Date calculateDataVencimento() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.dataHora);
        calendar.add(Calendar.DAY_OF_YEAR, 5);
        return calendar.getTime();
    }

    public void atualizarStatus(String novoStatus) {
        if (valorRecebido == calcularTotal()) {
            this.status = novoStatus;
        } else {
            throw new IllegalStateException("O pedido não pode ser atualizado. O pagamento total ainda não foi recebido.");
        }
    }

    @Override
    public String toString() {
        return "Pedido{Código: " + codigo + ", Cliente: " + cliente.getNome() +
                ", Funcionário: " + funcionario.getNome() + ", Endereço: " + endereco +
                ", Forma de Pagamento: " + formaPagamento + ", Total: R$" + calcularTotal() + "}";
    }
}
