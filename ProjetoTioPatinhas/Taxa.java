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
        BigDecimal taxa = new BigDecimal("0.06");
        return this.valor.subtract(this.valor.multiply(taxa));
   }
}
