package ProjetoTioPatinhas;

public class Ordem {
    private int idOrdem;
    private int idConta;
    private int idParAtivo;
    private String tipo; // "compra" ou "venda"
    private String dataLancamento;
    private double quantidade;
    private double preco;
    private String status;

    // Construtor padrão
    public Ordem() {}

    // Construtor com overload
    public Ordem(int idOrdem, int idConta, int idParAtivo, String tipo) {
        this.idOrdem = idOrdem;
        this.idConta = idConta;
        this.idParAtivo = idParAtivo;
        this.tipo = tipo;
    }

    // Construtor completo
    public Ordem(int idOrdem, int idConta, int idParAtivo, String tipo, String dataLancamento,
                 double quantidade, double preco, String status) {
        this.idOrdem = idOrdem;
        this.idConta = idConta;
        this.idParAtivo = idParAtivo;
        this.tipo = tipo;
        this.dataLancamento = dataLancamento;
        this.quantidade = quantidade;
        this.preco = preco;
        this.status = status;
    }

    // Getters e Setters
    public int getIdOrdem() {
        return idOrdem;
    }

    public void setIdOrdem(int idOrdem) {
        this.idOrdem = idOrdem;
    }

    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public int getIdParAtivo() {
        return idParAtivo;
    }

    public void setIdParAtivo(int idParAtivo) {
        this.idParAtivo = idParAtivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Polimorfismo dinâmico
    @Override
    public String toString() {
        return "Ordem [idOrdem=" + idOrdem + ", tipo=" + tipo + ", status=" + status + "]";
    }
}
