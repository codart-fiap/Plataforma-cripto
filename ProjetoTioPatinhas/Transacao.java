package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Transacao {

    private Carteira carteira;
    private int idTransacao;
    private static final Scanner scanner = new Scanner(System.in);
    private static int proximoIdOrdem = 1;
    private Map<String, BigDecimal> valores = new HashMap<>();

    public int getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(int idTransacao) {
        this.idTransacao = idTransacao;
    }

    private int gerarNovoIdTransacao() {
        return this.idTransacao++;
    }

    public Transacao(Carteira carteira) {
        this.carteira = carteira;
    }

   public void negociarCripto(String acao, String nome, BigDecimal valor) {
        this.verificarSaldoSuficiente(acao,valor, nome);
        CriptoAtivo cripto = this.criarCriptoAtivo(nome);
        BigDecimal preço = cripto.getCotacao();
        BigDecimal quantidade = valor.divide(preço, 4, RoundingMode.HALF_UP);
        this.gerenciarOrdem(acao, nome, quantidade, valor);
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
                BigDecimal deposito = this.criarInput("Adicione saldo para começar a investir !:");
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

    public void gerenciarOrdem(String acao, String nome, BigDecimal quantidade, BigDecimal valor) {
        int idOrdem = gerarNovoIdOrdem();
        this.setIdTransacao(this.gerarNovoIdTransacao());
        Ordem ordem = new Ordem(
                idOrdem,
                this.carteira.getConta().getIdConta(),
                this.idTransacao,
                nome,
                acao,
                this.obterDataHoraAtualFormatada(),
                quantidade,
                valor
            );
        ordem.setStatus("pendente");
        Ordem correspondente = Ordem.buscarOrdemCorrespondente(acao, nome, quantidade, valor);
        if (correspondente != null) {
            ordem.setStatus("executada");
            correspondente.setStatus("executada");
            this.processarTransacao(acao, nome, quantidade, valor);
        } else {
            ordem.armazenarOrdem(acao, ordem);
            System.out.println("Nenhuma ordem correspondente. Ordem pendente.");
            // Adiciona ao mapa de ordens
        }
        
    }

    private int gerarNovoIdOrdem() {
        return proximoIdOrdem++;
    }

    public void processarTransacao(String acao, String nome, BigDecimal quantidade, BigDecimal valor) {
        BigDecimal valorCompra = processarCompra(nome, quantidade,valor);
        BigDecimal valorVenda = processarVenda(nome, quantidade,valor);
        this.valores.put("valorCompra",valorCompra);
        this.valores.put("valorVenda",valorVenda);
    }

    public Map<String, BigDecimal> getValores() {
        return valores;
    }

    private BigDecimal processarCompra(String nome, BigDecimal quantidade, BigDecimal valor) {
        valor = this.aplicarTaxa(valor);
        BigDecimal saldo = this.carteira.getSaldo();
        saldo = saldo.subtract(valor);
        this.carteira.setSaldo(saldo);
        System.out.println(quantidade + " " + nome + " foi comprado com sucesso! foram investidos R$" + valor + ". o saldo foi alterado para: R$" + this.carteira.getSaldo()  + " (" + this.obterDataHoraAtualFormatada() + ")");
        return valor;
    }

    private BigDecimal processarVenda(String nome, BigDecimal quantidade, BigDecimal valor) {
        valor = this.aplicarTaxa(valor);
        BigDecimal saldo = this.carteira.getSaldo();
        saldo = saldo.add(valor);
        this.carteira.setSaldo(saldo);
        System.out.println(quantidade + " " + nome + " foi vendido com sucesso! foram retirados R$" + valor + ". o saldo foi alterado para: R$" + this.carteira.getSaldo() + " (" + this.obterDataHoraAtualFormatada() + ")");
        return valor;
    }


    public String obterDataHoraAtualFormatada() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(formatter);
    }
    
}




