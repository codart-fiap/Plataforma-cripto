package ProjetoTioPatinhas;
import java.math.BigDecimal;

public class Taxa {
    BigDecimal taxa;
    BigDecimal valor;

    public Taxa(BigDecimal taxa, BigDecimal valor) {
        this.taxa = taxa;
        this.valor = valor;
    }

    public BigDecimal aplicarTaxa() {
        BigDecimal porcentagem = this.taxa.multiply(new BigDecimal(100));
        System.out.println("Taxa de " + porcentagem + "% aplicada com sucesso!");
        return this.valor.subtract(this.valor.multiply(this.taxa));
   }
}
