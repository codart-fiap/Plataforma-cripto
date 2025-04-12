package ProjetoTioPatinhas;
import java.util.regex.*;
import java.math.BigDecimal;
import java.util.Scanner;

public class Conta {
    String nome;
    String email;
    BigDecimal saldo = new BigDecimal("0.00");
    String senhaHash;
    private static final Scanner scanner = new Scanner(System.in);

    {
        this.fornecerInformacoes();
    }

    public void fornecerInformacoes() {
        String resposta = this.criarInput("Digite 1 para criar uma conta ou 2 para logar:"); 
        if (resposta.equals("2")) {
            this.logar();
        } else {
            this.nome = this.criarInput("Digite seu nome completo:");
            this.validarNome();
            this.email = this.criarInput("Digite seu email:");
            this.validarEmail();
            this.senhaHash = this.criarInput("""
                    Digite sua senha:
                    sua senha deve conter, no mínimo:
                    - uma letra maiúscula
                    - um letra minúscula
                    - um número
                    - um caracter especial (@#$%^&+=)
                    - 8 caracteres no total
                    """);
            this.validarSenha();
        }

    }
    
    public String criarInput(String mensagem) {
        System.out.print(mensagem + " ");
        return scanner.nextLine();
    }

    public void logar() {
        // this.email = this.criarInput("Digite seu email:");
        // this.validarEmail();
        // this.senhaHash = this.criarInput("Digite sua senha:");
        // this.validarSenha();
    }

    public void validarNome() {
        String LETRAS_PT = "[a-zA-ZáàâãäéêëíïóôõöúüçÁÀÂÃÄÉÊËÍÏÓÔÕÖÚÜÇ]";
        // regex que valida todo tipo de nome composto separado por hifen ou espaço. 
        String regexNomeComposto = "^(" + LETRAS_PT + "{2,})([\\s-]" + LETRAS_PT + "{2,})+$";
        boolean validacao = this.validarDado(regexNomeComposto, this.nome);
        this.nome = this.reescrever_dado(validacao,"nome",regexNomeComposto,this.nome);
    }
    
    public boolean validarDado(String regex, String dado) {
        Pattern padrao = Pattern.compile(regex);
        Matcher verificador = padrao.matcher(dado);
        boolean validacao = verificador.matches();
        System.out.println(validacao);
        return validacao;
    }

    public String reescrever_dado(boolean validacao, String tipoDado, String regex, String dado) {
        while (!validacao) {
            dado = criarInput("Favor digitar um " + tipoDado + " válido:");
            validacao = this.validarDado(regex, dado);
        }
        return dado;
    }

    public void validarEmail() {
        String regexEmail = "[a-z0-9._-]+@[a-z0-9]+\\.[a-z]{2,}";
        boolean validacao = this.validarDado(regexEmail,this.email);
        this.email = this.reescrever_dado(validacao, "email", regexEmail, this.email);
    }

    public void validarSenha() {
        String regexSenha = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=])[^\\s]{8,}$";
        boolean validacao = this.validarDado(regexSenha, this.senhaHash);
        this.senhaHash = this.reescrever_dado(validacao, "senha", regexSenha, this.senhaHash);
    }

    public void depositar() {
        String entrada = criarInput("Digite um valor para depositar: ");
        BigDecimal valor = new BigDecimal(entrada);
        this.saldo = this.saldo.add(valor);
        System.out.println("saldo atualizado para: " + this.saldo);
    }

    public BigDecimal consultarSaldo() {
        return this.saldo;
    }

    public void adicionarSaldo() {
        this.saldo = criarInput("Conta Criada com sucesso, adicione seu saldo")
    }




}
