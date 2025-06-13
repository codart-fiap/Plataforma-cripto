package ProjetoTioPatinhas;
import java.util.HashMap;
import java.util.regex.*;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Map;
import java.lang.String;
public class Conta {
    Carteira carteira;
    String nome;
    String email;
    BigDecimal saldo = new BigDecimal("0.00");
    String senhaHash;
    Map<String, String> contasExistentes = new HashMap<>();
    private int idConta = this.gerarNovoIdConta();
    private static final Scanner scanner = new Scanner(System.in);

    {
        this.fornecerInformacoes();
        this.depositarSaldo();
    }

    public void fornecerInformacoes() {
        String resposta = this.criarInput("Digite (1) para criar uma conta pessoal:\n" + "Digite (2) para entrar em uma conta existente: " );
        switch (resposta) {
            case "1":
                this.criarConta();
                this.logar();
                break;

            case "2":
                this.logar();
                break;


            default:
                System.out.println("Por favor escolha um opção válida!");

        }


    }



    public String criarInput(String mensagem) {
        System.out.print(mensagem + " ");
        return scanner.nextLine();
    }

    public void criarConta(){
        System.out.println("[ CRIAÇÃO DE CONTA ]");
        System.out.println("Digite 0 para voltar para o início: ");

        System.out.println("----------------");
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

        contasExistentes.put(this.email,this.senhaHash);


    }

    public void logar() {
        System.out.println("\n[ ENTRAR EM UMA CONTA ]\n");
        this.email = this.criarInput("Digite seu email:");
        if(contasExistentes.containsKey(this.email)){
            System.out.println("Conta existente!");

            String contaLog = contasExistentes.get(this.email);

            verificaSenhaExistente(contaLog);

        }else {
            System.out.println("\n[ Parece que esta conta não existe! :( ]");

            System.out.println("\nTentar novamente digite (1)! ( >_<'):\n----------------------------------------\nCriar uma nova conta digite (2) ( ^_^ )\n" );

            String reposta = scanner.nextLine();

            if(reposta.equals("1")){
                this.logar();
            }else {
                this.criarConta();
            }





        }



    }

    public void menu(){
        System.out.println("Menu de opções");
        String opcao = this.criarInput("Escolha uma opção: \n Ver meu saldo (1)");

        switch (opcao){
            case "1":
                this.consultarSaldo();
                this.menu();
                break;
            case "2":
                break;
        }
    }



    public String validarNome() {
        String LETRAS_PT = "[a-zA-ZáàâãäéêëíïóôõöúüçÁÀÂÃÄÉÊËÍÏÓÔÕÖÚÜÇ]";
        // regex que valida todo tipo de nome composto separado por hifen ou espaço. 
        String regexNomeComposto = "^(" + LETRAS_PT + "{2,})([\\s-]" + LETRAS_PT + "{2,})+$";
        boolean validacao = this.validarDado(regexNomeComposto, this.nome);
        this.nome = this.reescrever_dado(validacao,"nome",regexNomeComposto,this.nome);

        String[] primeiroNome = this.nome.split(" ");

        return primeiroNome[0];
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
        System.out.println("\n--------------------------------------\nEMAIL DEFINIDO --> " + this.email + " \n--------------------------------------\n");
    }

    public void validarSenha() {
        String regexSenha = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=])[^\\s]{8,}$";
        boolean validacao = this.validarDado(regexSenha, this.senhaHash);
        this.senhaHash = this.reescrever_dado(validacao, "senha", regexSenha, this.senhaHash);
        System.out.println("\n--------------------------------------\nSENHA DEFINIDA --> " + this.senhaHash + " \n--------------------------------------\n");
    }

    public void depositarSaldo() {
        String entrada = criarInput("Digite um valor para depositar: \n" + "Para voltar ao menu digite (0)! \n ");
        if(entrada.equals("0")){
            this.menu();
        }else {
            BigDecimal valor = new BigDecimal(entrada);
            this.saldo = this.saldo.add(valor);
            System.out.println("Saldo atualizado para: " + this.saldo);

            this.menu();

        }
    }

    public BigDecimal consultarSaldo() {
        if(this.saldo.compareTo(BigDecimal.ZERO) == 0){

            System.out.println("Ainda sem nada por aqui :( Digite um valor para começar a investir!");
            this.depositarSaldo();
        }else if (this.saldo.compareTo(BigDecimal.ZERO) > 0){
               
            System.out.println("Saldo atual: " + this.saldo);
            this.depositarSaldo();
        }
            return this.saldo;

    }

    public void verificaSenhaExistente(String contaExistente){
        this.senhaHash = this.criarInput("Digite sua senha:");
        if(this.senhaHash.equals(contaExistente)){
            System.out.println("\nLogin efetuado com sucesso!\n");

            System.out.println("Bom te ver de volta " + this.validarNome() + " \uD83D\uDE0A");

            this.menu();


        }else {
            System.out.println("Senha incorreta!");

            verificaSenhaExistente(contaExistente);

        }
    }

    private int gerarNovoIdConta() {
        return idConta++;
    }

    public int getIdConta() {
        return idConta;
    }
}
