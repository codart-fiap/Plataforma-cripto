package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Ordem {
    private int idOrdem;
    private int idConta;
    private int idTransacao;
    private String nome;
    private String tipo; // "compra" ou "venda"
    private String dataLancamento;
    private BigDecimal quantidade;
    private BigDecimal valor;
    private String status;

    private static Map<Integer, Ordem> ordensCompra = new HashMap<>();
    private static Map<Integer, Ordem> ordensVenda = new HashMap<>();
    private static int proximoIdOrdem = 1;

    // Construtor padrão
    public Ordem() {}

    public Ordem(int idOrdem, int idConta, String nome, String tipo ,BigDecimal quantidade, BigDecimal valor) {
        this.idOrdem = idOrdem;
        this.idConta = idConta;
        this.nome = nome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    // Construtor com overload
    public Ordem(int idOrdem, int idConta, int idTransacao, String tipo) {
        this.idOrdem = idOrdem;
        this.idConta = idConta;
        this.idTransacao = idTransacao;
        this.tipo = tipo;
    }

    // Construtor completo
    public Ordem(int idOrdem, int idConta, int idTransacao,  String nome, String tipo, String dataLancamento,
                 BigDecimal quantidade, BigDecimal valor) {
        this.idOrdem = idOrdem;
        this.idConta = idConta;
        this.idTransacao = idTransacao;
        this.nome = nome;
        this.tipo = tipo;
        this.dataLancamento = dataLancamento;
        this.quantidade = quantidade;
        this.valor = valor;
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

    public int getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(int idTransacao) {
        this.idTransacao = idTransacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int gerarNovoIdOrdem() {
        return proximoIdOrdem++;
    }

    public void armazenarOrdem(String acao,Ordem ordem) {
        if (acao.equals("comprar")) {
            ordensCompra.put(ordem.idOrdem, ordem);
            System.out.println("ordem de compra adicionada com sucesso!" + ordensCompra);
        } else if (acao.equals("vender")) {
            ordensVenda.put(ordem.idOrdem, ordem);
            System.out.println("ordem de venda adicionada com sucesso!" + ordensVenda);
        }
    }

    public static Ordem buscarOrdemCorrespondente(String acao, String nome, BigDecimal quantidade, BigDecimal valor) {
        Map<Integer, Ordem> mapaOposto = acao.equals("comprar") ? ordensVenda : ordensCompra;
    
        for (Ordem o : mapaOposto.values()) {
            if (o.getStatus().equals("pendente") &&
                o.getNome().equals(nome) &&
                o.getQuantidade().compareTo(quantidade) >= 0 &&
                (
                    (acao.equals("comprar") && o.getValor().compareTo(valor) <= 0) ||
                    (acao.equals("vender") && o.getValor().compareTo(valor) >= 0)
                )
            ) {
                return o; // encontrou
            }
        }
    
        return null; // nenhuma correspondente
    }
    
    

    // // Polimorfismo dinâmico
    // @Override
    // public String toString() {
    //     return "Ordem [idOrdem=" + idOrdem + ", tipo=" + tipo + ", status=" + status + "]";
    // }
}
