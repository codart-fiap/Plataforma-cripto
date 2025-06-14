package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.util.*;

public class Carteira {
    // O mapa de ativos agora REFERENCIA diretamente o mapa na classe Conta
    private Conta conta; // Mantém a referência à conta à qual esta carteira pertence
    private Transacao transacao; // Referência para a classe Transacao
    private static final Scanner scanner = new Scanner(System.in);

    public Carteira(Conta conta) {
        this.conta = conta;
        // A carteira de ativos agora é armazenada diretamente na conta
        // this.ativos = conta.carteiraDeAtivos; // Não é mais necessário ter um 'ativos' próprio aqui
    }

    // Getters e Setters ajustados para operar no mapa da conta
    // Note que `getAtivos()` agora retorna o mapa da conta
    public Map<String, BigDecimal> getAtivos() {
        return this.conta.carteiraDeAtivos;
    }

    // `setAtivos` pode não ser mais necessário, pois as modificações devem ser feitas diretamente no mapa da conta
    public void setAtivos(Map<String, BigDecimal> ativos) {
        this.conta.carteiraDeAtivos = ativos;
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

    /**
     * Adiciona ativos à carteira.
     * Inclui a exibição de opções e validação de entrada para criptomoedas,
     * e um atraso para simular o processamento da transação.
     */
    public void adicionarAtivos() {
        this.transacao = new Transacao(this); // Cria uma nova transação para a operação

        String nomeCripto;
        // Loop para garantir que o usuário digite uma criptomoeda válida
        while (true) {
            System.out.println("\nCriptomoedas disponíveis para investimento: BTC, ETH");
            nomeCripto = this.criarInput("Me informe qual criptomoeda deseja investir (btc ou eth):").toLowerCase();
            if (nomeCripto.equals("btc") || nomeCripto.equals("eth")) {
                break; // Sai do loop se a entrada for válida
            } else {
                System.out.println("Opção inválida. Por favor, digite 'btc' ou 'eth'.");
            }
        }

        BigDecimal valorInvestido = this.criarInputBigDecial("Qual valor deseja investir?");

        System.out.println("Processando sua ordem de compra para " + nomeCripto.toUpperCase() + "...");
        transacao.negociarCripto("comprar", nomeCripto, valorInvestido);

        try {
            // Simula o tempo de processamento da transação
            Thread.sleep(2000); // 2 segundos de atraso
            System.out.println("\nTransação de compra finalizada com sucesso!");
        } catch (InterruptedException e) {
            System.err.println("O processamento da transação foi interrompido.");
            Thread.currentThread().interrupt(); // Restaura o estado de interrupção
        }

        // Recupera a quantidade comprada da transação
        Map<String, BigDecimal> valoresTransacao = transacao.getValores();
        if (valoresTransacao != null && valoresTransacao.containsKey("quantidadeComprada")) {
            BigDecimal quantidadeComprada = valoresTransacao.get("quantidadeComprada");
            // Atualiza a quantidade do ativo na carteira da CONTA
            // Se o ativo já existe, soma a nova quantidade; caso contrário, adiciona.
            this.conta.carteiraDeAtivos.merge(nomeCripto, quantidadeComprada, BigDecimal::add);
            System.out.println("Sua carteira de ativos foi atualizada. Ativos atuais: " + formatarAtivos());
        } else {
            System.out.println("Não foi possível atualizar a carteira de ativos. A transação pode não ter gerado uma quantidade comprada.");
        }
    }

    /**
     * Remove ativos da carteira.
     * Inclui a exibição de opções e validação de entrada para criptomoedas.
     */
    public void removerAtivos() {
        this.transacao = new Transacao(this); // Adicionado: Garante que 'transacao' não seja nulo

        System.out.println("Seu saldo atual é: R$" + this.conta.saldo.toPlainString());

        if (this.conta.carteiraDeAtivos.isEmpty()) {
            System.out.println("Você não possui nenhum ativo para vender em sua carteira.");
            return;
        }

        String nomeCripto;
        // Loop para garantir que o usuário digite uma criptomoeda válida para venda
        while (true) {
            System.out.println("\nAtivos disponíveis para venda na sua carteira: " + formatarAtivos());
            nomeCripto = this.criarInput("Me informe qual criptomoeda deseja vender (btc ou eth):").toLowerCase();
            if (nomeCripto.equals("btc") || nomeCripto.equals("eth")) {
                // Verificar se o ativo realmente existe na carteira e se há quantidade suficiente
                if (!this.conta.carteiraDeAtivos.containsKey(nomeCripto) || this.conta.carteiraDeAtivos.get(nomeCripto).compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Você não possui " + nomeCripto.toUpperCase() + " ou a quantidade é zero/negativa em sua carteira.");
                    continue; // Pede novamente
                }
                break; // Sai do loop se a entrada for válida
            } else {
                System.out.println("Opção inválida. Por favor, digite 'btc' ou 'eth'.");
            }
        }

        BigDecimal valorRetirado = this.criarInputBigDecial("Qual o valor monetário que deseja vender (baseado na cotação atual)?");

        System.out.println("Processando sua ordem de venda para " + nomeCripto.toUpperCase() + "...");
        transacao.negociarCripto("vender", nomeCripto, valorRetirado);

        try {
            Thread.sleep(2000); // Simula o tempo de processamento da transação
            System.out.println("\nTransação de venda finalizada com sucesso!");
        } catch (InterruptedException e) {
            System.err.println("O processamento da transação de venda foi interrompido.");
            Thread.currentThread().interrupt();
        }

        // Recupera a quantidade vendida da transação
        Map<String, BigDecimal> valoresTransacao = transacao.getValores();
        if (valoresTransacao != null && valoresTransacao.containsKey("quantidadeVendida")) {
            BigDecimal quantidadeVendida = valoresTransacao.get("quantidadeVendida");
            // Diminui a quantidade do ativo na carteira da CONTA
            this.conta.carteiraDeAtivos.merge(nomeCripto, quantidadeVendida, (oldVal, newVal) -> oldVal.subtract(newVal));

            // Remove o ativo do mapa se a quantidade se tornar zero ou negativa
            if (this.conta.carteiraDeAtivos.get(nomeCripto) != null && this.conta.carteiraDeAtivos.get(nomeCripto).compareTo(BigDecimal.ZERO) <= 0) {
                this.conta.carteiraDeAtivos.remove(nomeCripto);
                System.out.println(nomeCripto.toUpperCase() + " foi removido da sua carteira, pois a quantidade é zero ou negativa.");
            }
            System.out.println("Sua carteira de ativos foi atualizada. Ativos atuais: " + formatarAtivos());
        } else {
            System.out.println("Não foi possível atualizar a carteira de ativos. A transação pode não ter gerado uma quantidade vendida.");
        }
    }

    /**
     * Consulta a quantidade de ativos na carteira.
     * Se a carteira estiver vazia, sugere investir.
     */
    public void consultarQuantidade() {
        if (this.conta.carteiraDeAtivos.isEmpty()){
            System.out.println("Você ainda não possui nenhum ativo em sua carteira.");
            String opcao = this.criarInput("Deseja começar a investir agora? Digite (s) para sim ou (n) para não: ");
            if (opcao.equalsIgnoreCase("s")){
                this.adicionarAtivos();
            }else{
                return;
            }
        } else {
            System.out.println("\n--- Seus Ativos na Carteira ---");
            System.out.println(formatarAtivos());
            // Oferece opções após consultar a carteira
            String escolha = this.criarInput("Deseja (1) Adicionar mais ativos, (2) Vender ativos ou (0) Voltar ao menu? ");
            switch (escolha) {
                case "1":
                    this.adicionarAtivos();
                    break;
                case "2":
                    this.removerAtivos();
                    break;
                case "0":
                    return; // Volta ao menu anterior
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    /**
     * Formata o mapa de ativos para uma string legível.
     * @return Uma string representando os ativos na carteira.
     */
    private String formatarAtivos() {
        if (this.conta.carteiraDeAtivos.isEmpty()) {
            return "Nenhum ativo.";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, BigDecimal> entrada : this.conta.carteiraDeAtivos.entrySet()) {
            sb.append("- ").append(entrada.getKey().toUpperCase()).append(": ").append(entrada.getValue().toPlainString()).append("\n");
        }
        return sb.toString().trim(); // Remove a última quebra de linha se houver
    }


    public String criarInput(String mensagem) {
        System.out.print(mensagem + " ");
        return scanner.nextLine();
    }

    public BigDecimal criarInputBigDecial(String mensagem) {
        System.out.print(mensagem + " ");
        String entrada = scanner.nextLine().replace(",", ".");
        try {
            BigDecimal valor = new BigDecimal(entrada);
            if (valor.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("O valor não pode ser negativo. Digite um valor positivo.");
                return criarInputBigDecial(mensagem); // Solicita novamente
            }
            return valor;
        } catch (NumberFormatException e) {
            System.err.println("Erro: Valor numérico inválido. Por favor, digite um número válido.");
            return criarInputBigDecial(mensagem); // Solicita novamente
        }
    }
}
