package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Transacao {

    private Carteira carteira;
    private static final Scanner scanner = new Scanner(System.in);

    public Transacao(Carteira carteira) {
        this.carteira = carteira;
    }

   public BigDecimal negociarCripto(String acao, String nome, BigDecimal valor) {
    valor = aplicarTaxa(valor);
    this.verificarSaldoSuficiente(acao,valor, nome);
    DadosCripto cripto = new DadosCripto();
    BigDecimal preço = cripto.dados.get(nome).get("preço");
    BigDecimal quantidade = valor.divide(preço, 4, RoundingMode.HALF_UP);

    if (acao.equals("comprar")) {
        this.carteira.saldo = this.carteira.saldo.subtract(valor);
        System.out.println(quantidade + " " + nome + " foi comprado com sucesso! foram investidos R$" + valor + "o saldo foi alterado para: " + this.carteira.saldo);
    } else if(acao.equals("vender")) {
        this.carteira.saldo = this.carteira.saldo.add(valor);
        System.out.println(quantidade + " " + nome + " foi vendido com sucesso! foram retirados R$" + valor + "o saldo foi alterado para: " + this.carteira.saldo);
    }
    return valor;
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
            while (valor.compareTo(this.carteira.saldo) > 0) {
                System.out.println("Saldo insuficiente! Seu saldo é R$" + this.carteira.saldo + 
                                   " e o valor da compra é R$" + valor);
                BigDecimal deposito = this.criarInput("Informe quanto deseja adicionar:");
                this.carteira.saldo = this.carteira.saldo.add(deposito);
            }
        } 
    }
    
}




