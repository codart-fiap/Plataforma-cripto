package ProjetoTioPatinhas;
import java.math.BigDecimal;
import java.util.*;

public class DadosCripto {
    Map<String, Map<String, BigDecimal>> dados = new HashMap<>();

    public DadosCripto() {
        BigDecimal preçoBTC = new BigDecimal(500_000); 
        BigDecimal circulacaoBTC = new BigDecimal(21_000_000);
        this.criarDadosCripto("btc",preçoBTC,circulacaoBTC);

        BigDecimal preçoETH = new BigDecimal(9_100); 
        BigDecimal circulacaoETH = new BigDecimal(120_000_000);
        this.criarDadosCripto("eth", preçoETH, circulacaoETH);
    }

    public void criarDadosCripto(String nome, BigDecimal preço, BigDecimal circulacao) {
        Map<String, BigDecimal> cripto = new HashMap<>();
        BigDecimal valorDeMercado = preço.multiply(circulacao); 
        cripto.put("preço", preço);
        cripto.put("circulacao", circulacao);
        cripto.put("valorDeMercado", valorDeMercado);
        dados.put(nome, cripto);
    }

    public void exibirDados() {
        for (String nomeMoeda : dados.keySet()) {
            System.out.println("Moeda: " + nomeMoeda);
            System.out.println("Dados: " + dados.get(nomeMoeda));
        }
    }
    
}
