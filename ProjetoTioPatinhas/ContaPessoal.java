package ProjetoTioPatinhas;

public class ContaPessoal extends Conta {
    public String cpf; // Tornando público para acesso direto (se necessário)

    /**
     * Construtor padrão para ContaPessoal.
     * Chama o construtor da superclasse `Conta` para gerenciar o ID.
     */
    public ContaPessoal() {
        super(); // Garante que o ID da conta seja gerado pela classe base
    }

    // O método `getIdPessoal()` não é mais necessário, pois o ID da conta
    // é acessado via `getIdConta()` da superclasse `Conta`.
    // public int getIdPessoal() {
    //     return idPessoal;
    // }

    /**
     * Método para conectar a conta pessoal a uma conta empresarial.
     * (Implementação pendente conforme o projeto original).
     * @param idEmpresarial O ID da conta empresarial a ser conectada.
     * @return Um valor inteiro (0 neste caso, pois a funcionalidade não está completa).
     */
    public int conectarConta(int idEmpresarial) {
        System.out.println("Funcionalidade de conexão com conta empresarial ainda não implementada.");
        return 0;
    }
}
