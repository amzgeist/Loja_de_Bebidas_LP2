package dados;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Pedido {
    private static final double JUROS_ATRASO = 0.03; // 3% de juros por atraso
    private static final int DIAS_VENCIMENTO = 5; // Pedido vence em 5 dias
    private static final int DIAS_MULTA = 10; // Multa aplicada a cada 10 dias de atraso

    private int codigo;
    private Cliente cliente;
    private Funcionario funcionario;
    private String endereco;
    private Map<Produto, Integer> produtos; // Mapeamento Produto -> Quantidade
    private String formaPagamento;
    private String statusPagamento;
    private String status;
    private Date dataCriacao;
    private Date dataVencimento;
    private double valorRecebido;

    // Construtor para o Pedido
    public Pedido(Cliente cliente, Funcionario funcionario, String endereco, Map<Produto, Integer> produtos, String formaPagamento) {
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.endereco = endereco;
        this.produtos = produtos != null ? produtos : new HashMap<>();
        this.formaPagamento = formaPagamento;
        this.statusPagamento = "Aguardando pagamento";
        this.status = "Pedido Confirmado. Aguardando Faturamento";
        this.dataCriacao = new Date(); // Data atual
        this.dataVencimento = calcularDataVencimento();
        this.valorRecebido = 0.0;
    }

    // Construtor vazio para quando for buscar o pedido a partir do DAO
    public Pedido() {
        this.produtos = new HashMap<>();
        this.dataCriacao = new Date(); // Data atual
        this.dataVencimento = calcularDataVencimento();
        this.valorRecebido = 0.0;
        this.statusPagamento = "Aguardando pagamento";
        this.status = "Pedido Confirmado. Aguardando Faturamento";
    }

    // Cálculo da data de vencimento (5 dias após a data de criação)
    private Date calcularDataVencimento() {
        long currentTime = System.currentTimeMillis();
        return new Date(currentTime + (DIAS_VENCIMENTO * 24L * 60L * 60L * 1000L)); // 5 dias
    }

    // Adiciona um produto ao pedido
    public void adicionarProduto(Produto produto, int quantidade) {
        produtos.put(produto, quantidade);
    }

    // Calcula o valor total do pedido
    public double calcularTotal() {
        double total = 0.0;
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            Produto produto = entry.getKey();
            int quantidade = entry.getValue();
            total += produto.getPreco() * quantidade;
        }
        return total;
    }

    // Getters e Setters

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Map<Produto, Integer> getProdutos() {
        return produtos;
    }

    public void setProdutos(Map<Produto, Integer> produtos) {
        this.produtos = produtos;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    // Sobrescreve toString para exibir o resumo do pedido
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido Código: ").append(codigo).append("\n");
        sb.append("Cliente: ").append(cliente.getNome()).append(" (CPF: ").append(cliente.getCpf()).append(")\n");
        sb.append("Funcionário: ").append(funcionario.getNome()).append(" (Código: ").append(funcionario.getCodigoFunc()).append(")\n");
        sb.append("Endereço: ").append(endereco).append("\n");
        sb.append("Forma de Pagamento: ").append(formaPagamento).append("\n");
        sb.append("Status Pagamento: ").append(statusPagamento).append("\n");
        sb.append("Status Pedido: ").append(status).append("\n");
        sb.append("Data de Criação: ").append(dataCriacao).append("\n");
        sb.append("Data de Vencimento: ").append(dataVencimento).append("\n");
        sb.append("Produtos:\n");

        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            Produto produto = entry.getKey();
            int quantidade = entry.getValue();
            sb.append(" - ").append(produto.getNome()).append(" (x").append(quantidade).append("): R$ ")
                    .append(produto.getPreco() * quantidade).append("\n");
        }

        sb.append("Total: R$ ").append(calcularTotal()).append("\n");
        sb.append("Valor Recebido: R$ ").append(valorRecebido).append("\n");
        return sb.toString();
    }
}