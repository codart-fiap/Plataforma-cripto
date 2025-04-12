package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConexaoDeContas {
    private ContaPessoal contaPessoal;
    private ContaEmpresarial contaEmpresarial;
    private static final Scanner scanner = new Scanner(System.in);

    public BigDecimal saldoTotal = new BigDecimal("0.00");
    
    public ConexaoDeContas(ContaPessoal pessoal, ContaEmpresarial empresarial) {
    this.contaPessoal = pessoal;
    this.contaEmpresarial = empresarial;
    this.solicitarAcesso();
    }

    public void solicitarAcesso() {
        System.out.println("Posso conectar a conta Pessoal: " + contaPessoal.getIdPessoal() + " com a conta Empresarial: " + contaEmpresarial.getIdEmpresarial() + "?");
        String resposta = scanner.nextLine();
        if (resposta.toLowerCase().equals("sim")) {
            this.conectarConta();
        } else {
            System.out.println("Conexão Cancelada");
        }

    }

    public void conectarConta() {
        this.saldoTotal = saldoTotal.add(contaPessoal.saldo).add(contaEmpresarial.saldo);
        System.out.println("o saldo total é: " + this.saldoTotal);

    }
    }
