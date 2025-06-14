package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.util.*;

public class Carteira {
    private Map<String, BigDecimal> ativos = new HashMap<>();
    private Transacao transacao;
    private Conta conta;
    private static final Scanner scanner = new Scanner(System.in);

    public Carteira(Conta conta) {
        this.conta = conta;

    }

    public Map<String, BigDecimal> getAtivos() {
        return ativos;
    }

    public void setAtivos(Map<String, BigDecimal> ativos) {
        this.ativos = ativos;
    }

    public Transacao getTransacao() {
        return transacao;
    }
    
    public Conta getConta() {
        return conta;
    }

    public BigDecimal getSaldo() {
        return this.conta.saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.conta.saldo = saldo;
    }



    public void adicionarAtivos() {
        this.transacao = new Transacao(this);

        String nome = this.criarInput("Me informe qual cripto moeda deseja investir (btc ou eth)");
        BigDecimal valorInvestido = this.criarInputBigDecial("Qual valor deseja investir?");
        transacao.negociarCripto("comprar", nome, valorInvestido);
    }

    public void removerAtivos() {
        System.out.println("o saldo é: " + this.conta.saldo);
        String nome = this.criarInput("Me informe qual cripto moeda deseja vender (btc ou eth)");
        BigDecimal valorRetirado = this.criarInputBigDecial("Quanto deseja vender?");
        transacao.negociarCripto("vender", nome, valorRetirado);
        Map<String, BigDecimal> valores = transacao.getValores(); 
        if (valores != null) {
            BigDecimal valorInvestido = valores.get("valorCompra");
            valorRetirado = valores.get("valorVenda");
            ativos.put(nome, valorInvestido);
            System.out.println("ativo adicionado: "+ ativos);
            ativos.remove(nome);
            System.out.println("ativo removido: " + ativos + ", valor retirado: "+ valorRetirado);
        }
    }

    public void consultarQuantidade() {

        if (ativos.isEmpty()){
            String opcao = this.criarInput("Você ainda não possui nenhum ativo, deseja começar a investir agora ? \n Digte (s) para sim ou (n) para não: ");
            if (opcao.equals("s")){
                this.adicionarAtivos();
            }else{
                return;
            }

        }
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
