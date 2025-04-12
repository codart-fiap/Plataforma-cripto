package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.util.*;

public class Carteira {
    Map<String, BigDecimal> ativos = new HashMap<>();
    Transacao transacao;
    Conta conta;
    private static final Scanner scanner = new Scanner(System.in);

    public Carteira(Conta conta) {
        this.conta = conta;
        this.transacao = new Transacao(this);
        this.adicionarAtivos();
        this.removerAtivos();
    }

    public void adicionarAtivos() {
        String nome = this.criarInput("Me informe qual cripto moeda deseja investir (btc ou eth)");
        BigDecimal valorInvestido = this.criarInputBigDecial("Qual valor deseja investir?");

        valorInvestido = transacao.negociarCripto("comprar", nome, valorInvestido);
        ativos.put(nome, valorInvestido);
        System.out.println("ativo adicionado: " + ativos);
    }

    public void removerAtivos() {
        System.out.println("o saldo é: " + this.conta.saldo);
        String nome = this.criarInput("Me informe qual cripto moeda deseja vender (btc ou eth)");
        BigDecimal valorRetirado = this.criarInputBigDecial("Quanto deseja vender?");

        transacao.negociarCripto("vender", nome, valorRetirado);
        ativos.remove(nome);
        System.out.println("ativo removido: " + ativos);
    }

    public void consultarQuantidade() {
        for (Map.Entry<String, BigDecimal> entrada : ativos.entrySet()) {
            System.out.println(entrada.getKey() + " -> " + entrada.getValue());
        }
    }
    
    public String criarInput(String mensagem) {
        System.out.print(mensagem + " ");
        return scanner.nextLine();
    }

    public BigDecimal criarInputBigDecial(String mensagem) {
        System.out.print(mensagem + " ");
        String entrada = scanner.nextLine().replace(",", ".");
        return new BigDecimal(entrada);
    }



}
