package ProjetoTioPatinhas;

import java.math.BigDecimal;

public class Ativo {
    private int id;
    private String nome;
    private String simbolo;
    private BigDecimal cotacao;

    public Ativo(int id, String nome, String simbolo, BigDecimal cotacao) {
        this.id = id;
        this.nome = nome;
        this.simbolo = simbolo;
        this.cotacao = cotacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public BigDecimal getCotacao() {
        return cotacao;
    }

    public void setCotacao(BigDecimal cotacao) {
        this.cotacao = cotacao;
    }
}
