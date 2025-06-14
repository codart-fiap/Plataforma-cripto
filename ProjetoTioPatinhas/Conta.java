package ProjetoTioPatinhas;
import java.util.regex.*; // Importar Pattern e Matcher
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Map; // Manter import caso seja usado em futuras implementações

public class Conta {
    public String nome;
    public String email;
    public BigDecimal saldo = new BigDecimal("0.00");
    public String senhaHash;

    private int idConta;
    private static int proximoIdConta = 1;

    // O Scanner é agora gerenciado por MenusAplicacao, não pela Conta.
    // private static final Scanner scanner = new Scanner(System.in);

    /**
     * Construtor padrão para a classe Conta.
     * Atribui um novo ID único à conta ao ser instanciada.
     */
    public Conta() {
        this.idConta = gerarNovoIdConta();
    }

    /**
     * Setter para o ID da conta. Usado principalmente ao carregar contas de um arquivo,
     * onde o ID já é conhecido.
     * @param idConta O ID a ser atribuído à conta.
     */
    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    /**
     * Getter para o ID da conta.
     * @return O ID único da conta.
     */
    public int getIdConta() {
        return idConta;
    }

    /**
     * Método estático para gerar um novo ID único para uma conta.
     * Garante que cada nova conta receba um ID sequencial e exclusivo.
     * @return O próximo ID disponível.
     */
    private static int gerarNovoIdConta() {
        return proximoIdConta++;
    }

    /**
     * Método estático para definir o próximo ID disponível.
     * Usado por `Main.java` após carregar contas de arquivos para garantir
     * que novos IDs não entrem em conflito com IDs existentes.
     * @param newId O novo valor para o próximo ID.
     */
    public static void setProximoIdConta(int newId) {
        if (newId >= proximoIdConta) { // Usar >= para o caso de o arquivo ter o mesmo ID
            proximoIdConta = newId + 1; // Próximo ID deve ser maior que o maior carregado
        }
    }

    /**
     * Permite que o usuário deposite saldo na conta.
     * @param valor O valor BigDecimal a ser depositado.
     */
    public void depositarSaldo(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) > 0) { // Garante que o depósito seja positivo
            this.saldo = this.saldo.add(valor);
            System.out.println("Saldo atualizado para: R$" + this.saldo.toPlainString());
        } else {
            System.out.println("O valor do depósito deve ser positivo.");
        }
    }

    /**
     * Consulta o saldo atual da conta.
     * @return O saldo atual da conta.
     */
    public BigDecimal consultarSaldo() {
        System.out.println("Seu saldo atual é: R$" + this.saldo.toPlainString());
        return this.saldo;
    }
}
