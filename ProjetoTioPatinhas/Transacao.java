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
        // A validação do 'nome' agora é feita em Carteira.java antes de chamar este método.
        this.verificarSaldoSuficiente(acao, valor, nome);
        CriptoAtivo cripto = this.criarCriptoAtivo(nome); // 'cripto' não será nulo se 'nome' for válido

        // Verifica se 'cripto' é nulo, embora a validação em Carteira.java já deva prevenir isso.
        // É uma camada extra de segurança.
        if (cripto == null) {
            System.err.println("Erro: Criptoativo não encontrado para o nome '" + nome + "'.");
            return;
        }

        BigDecimal preço = cripto.getCotacao();
        // Evita divisão por zero se o preço for zero, embora seja improvável para criptos reais.
        if (preço.compareTo(BigDecimal.ZERO) == 0) {
            System.err.println("Erro: Preço da criptomoeda é zero. Não é possível negociar.");
            return;
        }
        BigDecimal quantidade = valor.divide(preço, 4, RoundingMode.HALF_UP);
        this.gerenciarOrdem(acao, nome, quantidade, valor);
    }

    public BigDecimal aplicarTaxa(BigDecimal valor) {
        // Considerando uma taxa fixa, ajustei para garantir que não haja erros de arredondamento.
        // A taxa de 6% significa que o valor final será 94% do original.
        BigDecimal taxaAplicada = valor.multiply(new BigDecimal("0.06"));
        BigDecimal valorComTaxa = valor.subtract(taxaAplicada);
        System.out.println("Taxa de 6% (R$" + taxaAplicada.toPlainString() + ") aplicada. Valor líquido: R$" + valorComTaxa.toPlainString());
        return valorComTaxa;
    }

    public BigDecimal criarInput(String mensagem) {
        System.out.print(mensagem + " ");
        return scanner.nextBigDecimal();
    }

    public void verificarSaldoSuficiente(String acao, BigDecimal valor, String nome) {
        if (acao.equals("comprar")) {
            while (valor.compareTo(this.carteira.getSaldo()) > 0) {
                System.out.println("Saldo insuficiente! Seu saldo é R$" + this.carteira.getSaldo().toPlainString() +
                        " e o valor da compra é R$" + valor.toPlainString());
                BigDecimal deposito = this.criarInput("Adicione saldo para começar a investir (apenas números):");
                BigDecimal saldoAtual = this.carteira.getSaldo();
                saldoAtual = saldoAtual.add(deposito);
                this.carteira.setSaldo(saldoAtual);
                System.out.println("Saldo atualizado para: R$" + saldoAtual.toPlainString());
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
        // Não precisamos de um else aqui, pois a validação já acontece em Carteira.java
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
        ordem.setStatus("pendente"); // Inicialmente define a ordem como pendente

        Ordem correspondente = Ordem.buscarOrdemCorrespondente(acao, nome, quantidade, valor);
        if (correspondente != null) {
            ordem.setStatus("executada");
            correspondente.setStatus("executada");
            System.out.println("Ordem " + idOrdem + " (" + acao + " " + nome + ") executada com ordem correspondente " + correspondente.getIdOrdem() + ".");
        } else {
            // Mesmo que não haja um correspondente, a ordem é armazenada como pendente.
            // Para o usuário que está comprando/vendendo, a transação afetará sua carteira imediatamente.
            ordem.armazenarOrdem(acao, ordem);
            System.out.println("Nenhuma ordem correspondente encontrada. Ordem " + idOrdem + " (" + acao + " " + nome + ") adicionada como pendente.");
        }

        // Processa a transação para o usuário iniciador, independentemente de ter encontrado um correspondente.
        // Isso garante que o saldo e a carteira do usuário sejam atualizados.
        this.processarTransacao(acao, nome, quantidade, valor);
    }

    private int gerarNovoIdOrdem() {
        return proximoIdOrdem++;
    }

    public void processarTransacao(String acao, String nome, BigDecimal quantidade, BigDecimal valor) {
        BigDecimal valorFinal;
        if (acao.equals("comprar")) {
            valorFinal = processarCompra(nome, quantidade, valor);
            this.valores.put("valorCompra", valorFinal); // Valor monetário gasto
            this.valores.put("quantidadeComprada", quantidade); // Quantidade de cripto comprada
        } else if (acao.equals("vender")) {
            valorFinal = processarVenda(nome, quantidade, valor);
            this.valores.put("valorVenda", valorFinal); // Valor monetário recebido
            this.valores.put("quantidadeVendida", quantidade); // Quantidade de cripto vendida
        }
    }

    public Map<String, BigDecimal> getValores() {
        return valores;
    }

    private BigDecimal processarCompra(String nome, BigDecimal quantidade, BigDecimal valor) {
        valor = this.aplicarTaxa(valor); // Aplica a taxa antes de subtrair do saldo
        BigDecimal saldo = this.carteira.getSaldo();
        saldo = saldo.subtract(valor);
        this.carteira.setSaldo(saldo);
        System.out.println(quantidade.toPlainString() + " " + nome.toUpperCase() + " comprado com sucesso! Foram investidos R$" + valor.toPlainString() + ". Saldo atual: R$" + this.carteira.getSaldo().toPlainString()  + " (" + this.obterDataHoraAtualFormatada() + ")");
        return valor;
    }

    private BigDecimal processarVenda(String nome, BigDecimal quantidade, BigDecimal valor) {
        valor = this.aplicarTaxa(valor); // Aplica a taxa antes de adicionar ao saldo
        BigDecimal saldo = this.carteira.getSaldo();
        saldo = saldo.add(valor);
        this.carteira.setSaldo(saldo);
        System.out.println(quantidade.toPlainString() + " " + nome.toUpperCase() + " vendido com sucesso! Foram adicionados R$" + valor.toPlainString() + ". Saldo atual: R$" + this.carteira.getSaldo().toPlainString() + " (" + this.obterDataHoraAtualFormatada() + ")");
        return valor;
    }


    public String obterDataHoraAtualFormatada() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(formatter);
    }
}
