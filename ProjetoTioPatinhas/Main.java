package ProjetoTioPatinhas;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("Criando Conta pessoal: ");
        ContaPessoal contaPessoal = new ContaPessoal();
        System.out.println("Criando conta Empresarial: ");
        ContaEmpresarial contaEmpresarial = new ContaEmpresarial();
        ConexaoDeContas conexao = new ConexaoDeContas(contaPessoal, contaEmpresarial);
        Carteira carteira = new Carteira(contaPessoal);

}
    
}
