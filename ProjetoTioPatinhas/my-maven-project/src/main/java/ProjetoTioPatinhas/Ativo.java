package ProjetoTioPatinhas;

import java.math.BigDecimal;

public class Ativo {
    private Integer id;           // use Integer para permitir null antes do insert
    private String nome;
    private String simbolo;
    private BigDecimal cotacao;

    public Ativo(Integer id, String nome, String simbolo, BigDecimal cotacao) {
        this.id = id;
        this.nome = nome;
        this.simbolo = simbolo;
        this.cotacao = cotacao;
    }

    public Ativo(String nome, String simbolo, BigDecimal cotacao) {
        this(null, nome, simbolo, cotacao);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }

    public BigDecimal getCotacao() { return cotacao; }
    public void setCotacao(BigDecimal cotacao) { this.cotacao = cotacao; }

    @Override
    public String toString() {
        return "Ativo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", simbolo='" + simbolo + '\'' +
                ", cotacao=" + cotacao +
                '}';
    }
}