package ProjetoTioPatinhas;


public class Main {
    public static void main(String[] args) {

        try {
            System.out.println("Criando Conta pessoal: ");
            ContaPessoal contaPessoal = new ContaPessoal();
            System.out.println("Criando conta Empresarial: ");
            ContaEmpresarial contaEmpresarial = new ContaEmpresarial();
            ConexaoDeContas conexao = new ConexaoDeContas(contaPessoal, contaEmpresarial);
            Carteira carteira = new Carteira(contaPessoal);
        }

        catch (NumberFormatException e) {
            System.err.println("Formato numérico inválido: " + e.getMessage());
        }

        catch (IllegalArgumentException e) {
            System.err.println("Argumento inválido ao popular objeto: " + e.getMessage());
        }

        catch (Exception e) {
            System.err.println("Erro inesperado durante criação/população: " + e.getMessage());
            e.printStackTrace();
        }

}
    
}
