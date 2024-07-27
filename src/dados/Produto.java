package dados;

public class Produto {
    private static int proximoCodigo = 1;
    private int codigo;
    private String nome;
    private float preco;
    private int estoque;

    public Produto(String nome, float preco, int estoque) {
        this.codigo = proximoCodigo++;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public float getPreco() {
        return preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
}