package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Transacao {

    private Carteira carteira;
    private int idTransacao;
    private static final Scanner scanner = new Scanner(System.in);

    public int getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(int idTransacao) {
        this.idTransacao = idTransacao;
    }

    public Transacao(Carteira carteira) {
        this.carteira = carteira;
    }

   public BigDecimal negociarCripto(String acao, String nome, BigDecimal valor) {
       try {
            valor = aplicarTaxa(valor);
            this.verificarSaldoSuficiente(acao,valor, nome);
            CriptoAtivo cripto = this.criarCriptoAtivo(nome);
            BigDecimal preço = cripto.getCotacao();
            BigDecimal quantidade = valor.divide(preço, 4, RoundingMode.HALF_UP);
            this.processarTransacao(acao, nome, quantidade, valor);
            return valor;
        } catch (Exception e) {
            System.out.println("Cripto moeda inválida!");
            return null;
        }
        
    }

    public BigDecimal aplicarTaxa(BigDecimal valor) {
        return new Taxa(new BigDecimal("0.06"), valor).aplicarTaxa();
    }

   public BigDecimal criarInput(String mensagem) {
    System.out.print(mensagem + " ");
    return scanner.nextBigDecimal();
    }

    public void verificarSaldoSuficiente(String acao, BigDecimal valor, String nome) {
        if (acao.equals("comprar")) {
            while (valor.compareTo(this.carteira.getSaldo()) > 0) {
                System.out.println("Saldo insuficiente! Seu saldo é R$" + this.carteira.getSaldo() + 
                                   " e o valor da compra é R$" + valor);
                BigDecimal deposito = this.criarInput("Informe quanto deseja adicionar:");
                BigDecimal saldo = this.carteira.getSaldo();
                saldo = saldo.add(deposito);
                this.carteira.setSaldo(saldo);
            }
        } 
    }

    public CriptoAtivo criarCriptoAtivo(String nome) {
        CriptoAtivo cripto = null;
        if (nome.equalsIgnoreCase("eth")) {
            BigDecimal cotacao = new BigDecimal(90_000);
            cripto = new CriptoAtivo(2, "Ethereum", "ETH", cotacao);

        } else if (nome.equalsIgnoreCase("btc")) {
            BigDecimal cotacao = new BigDecimal(200_000);
            cripto = new CriptoAtivo(1, "Bitcoin", "BTC", cotacao);  
        } 
        return cripto;
    }

    public void processarTransacao(String acao, String nome, BigDecimal quantidade, BigDecimal valor) {
        if (acao.equals("comprar")) {
            BigDecimal saldo = this.carteira.getSaldo();
            saldo = saldo.subtract(valor);
            this.carteira.setSaldo(saldo);
            System.out.println(quantidade + " " + nome + " foi comprado com sucesso! foram investidos R$" + valor + ". o saldo foi alterado para: R$" + this.carteira.getSaldo()  + " (" + this.obterDataHoraAtualFormatada() + ")");
        } else if(acao.equals("vender")) {
            BigDecimal saldo = this.carteira.getSaldo();
            saldo = saldo.add(valor);
            this.carteira.setSaldo(saldo);
            System.out.println(quantidade + " " + nome + " foi vendido com sucesso! foram retirados R$" + valor + ". o saldo foi alterado para: R$" + this.carteira.getSaldo() + " (" + this.obterDataHoraAtualFormatada() + ")");
        }
    }

    public String obterDataHoraAtualFormatada() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(formatter);
    }
    
}




