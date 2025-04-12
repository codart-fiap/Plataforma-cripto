package ProjetoTioPatinhas;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("Criando Conta pessoal: ");
        ContaPessoal contaPessoal = new ContaPessoal();
        // contaPessoal.depositar();
        // System.out.println("Criando conta Empresarial: ");
        // ContaEmpresarial contaEmpresarial = new ContaEmpresarial();
        // contaEmpresarial.depositar();
        // ConexaoDeContas conexao = new ConexaoDeContas(contaPessoal, contaEmpresarial);
        
        Carteira carteira = new Carteira(new BigDecimal("100"));
        carteira.adicionarAtivos();
        carteira.removerAtivos();

}
    
}
