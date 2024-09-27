package dados;

public class Produto {
    private int codigo;
    private String nome;
    private float preco;
    private int estoque;

    public Produto(int codigo, String nome, float preco, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Produto(String nome, float preco, int estoque) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public String toString() {
        return "Produto: " + this.nome +
                "\nPreço: R$ " + this.preco +
                "\nEstoque: " + this.estoque +
                "\nCódigo: " + this.codigo;
    }

    // Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
}
