package ProjetoTioPatinhas;

public class ContaEmpresarial extends Conta {
    public String cnpj; // Tornando público para acesso direto (se necessário)

    /**
     * Construtor padrão para ContaEmpresarial.
     * Chama o construtor da superclasse `Conta` para gerenciar o ID.
     */
    public ContaEmpresarial() {
        super(); // Garante que o ID da conta seja gerado pela classe base
    }

    // O método `getIdEmpresarial()` não é mais necessário, pois o ID da conta
    // é acessado via `getIdConta()` da superclasse `Conta`.
    // public int getIdEmpresarial() {
    //     return idEmpresarial;
    // }
}
