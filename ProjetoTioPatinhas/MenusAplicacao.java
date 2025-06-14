package ProjetoTioPatinhas;
import java.util.Map;
import java.util.Scanner;
import java.math.BigDecimal;
import java.util.regex.Matcher; // Importar Matcher
import java.util.regex.Pattern; // Importar Pattern

public class MenusAplicacao {
    private Scanner leitor = new Scanner(System.in);

    // A conta atualmente logada/selecionada.
    public ContaPessoal contaPessoal;
    public Carteira carteiraPessoal;

    // Referência ao mapa global de contas do Main, para gerenciar a persistência.
    private Map<Integer, Conta> todasAsContas;

    private boolean sair = false; // Controla o loop do menu

    /**
     * Construtor para MenusAplicacao.
     * @param todasAsContas O mapa de todas as contas carregadas/gerenciadas pelo Main.
     */
    public MenusAplicacao(Map<Integer, Conta> todasAsContas) {
        this.todasAsContas = todasAsContas;
    }

    // Setters (mantidos para flexibilidade, embora o construtor já passe o mapa)
    public void setContaPessoal(ContaPessoal contaPessoal) {
        this.contaPessoal = contaPessoal;
        if (this.carteiraPessoal == null || this.carteiraPessoal.getConta() != contaPessoal) {
            this.carteiraPessoal = new Carteira(contaPessoal);
        }
    }

    public void setCarteiraPessoal(Carteira carteiraPessoal) {
        this.carteiraPessoal = carteiraPessoal;
    }

    /**
     * Inicia o fluxo de sessão, permitindo ao usuário criar uma nova conta ou fazer login.
     * Gerencia a seleção da conta principal para a sessão.
     */
    public void iniciarSessao() {
        String resposta = criarInput("Digite (1) para criar uma nova conta pessoal:\n" + "Digite (2) para entrar em uma conta existente: ");
        switch (resposta) {
            case "1":
                this.realizarCadastro();
                break;
            case "2":
                this.realizarLogin();
                break;
            default:
                System.out.println("Por favor, escolha uma opção válida!");
                this.iniciarSessao(); // Tenta novamente se a opção for inválida
                return; // Impede que o restante do método execute se a opção for inválida
        }

        // Se uma conta foi selecionada/criada com sucesso, procede para o menu da conta.
        if (this.contaPessoal != null) {
            // Garante que a carteira esteja associada à conta correta.
            if (this.carteiraPessoal == null || this.carteiraPessoal.getConta() != this.contaPessoal) {
                this.carteiraPessoal = new Carteira(this.contaPessoal);
            }
            contaPessoalMenu();
        } else {
            System.out.println("Sessão não iniciada. Encerrando o aplicativo.");
        }
    }

    // Métodos auxiliares para entrada e validação (movidos de Conta.java)
    /**
     * Cria uma interface para obter entrada do usuário.
     * @param mensagem A mensagem a ser exibida ao usuário.
     * @return A string lida do console.
     */
    public String criarInput(String mensagem) {
        System.out.print(mensagem + " ");
        return leitor.nextLine();
    }

    /**
     * Valida um dado genérico contra uma expressão regular.
     * @param regex A expressão regular para validação.
     * @param dado O dado a ser validado.
     * @return true se o dado corresponde à regex, false caso contrário.
     */
    public boolean validarDado(String regex, String dado) {
        Pattern padrao = Pattern.compile(regex);
        Matcher verificador = padrao.matcher(dado);
        return verificador.matches();
    }

    /**
     * Solicita ao usuário para reescrever um dado até que ele seja válido.
     * @param validacao O status inicial de validação.
     * @param tipoDado O tipo de dado (e.g., "nome", "email").
     * @param regex A expressão regular para validação.
     * @param dado O dado inicial (potencialmente inválido).
     * @return O dado validado.
     */
    public String reescrever_dado(boolean validacao, String tipoDado, String regex, String dado) {
        while (!validacao) {
            dado = criarInput("Favor digitar um " + tipoDado + " válido:");
            validacao = this.validarDado(regex, dado);
        }
        return dado;
    }

    /**
     * Gerencia o processo de cadastro de uma nova conta pessoal.
     * Adiciona a conta criada ao mapa global de contas.
     */
    private void realizarCadastro() {
        System.out.println("[ CRIAÇÃO DE CONTA PESSOAL ]");
        System.out.println("----------------");

        // Cria uma nova instância de ContaPessoal
        ContaPessoal novaConta = new ContaPessoal();

        // Coleta e valida o nome
        String nome = criarInput("Digite seu nome completo:");
        String LETRAS_PT = "[a-zA-ZáàâãäéêëíïóôõöúüçÁÀÂÃÄÉÊËÍÏÓÔÕÖÚÜÇ]";
        String regexNomeComposto = "^(" + LETRAS_PT + "{2,})([\\s-]" + LETRAS_PT + "{2,})+$";
        novaConta.nome = reescrever_dado(validarDado(regexNomeComposto, nome), "nome", regexNomeComposto, nome);

        // Coleta e valida o email
        String email = criarInput("Digite seu email:");
        String regexEmail = "[a-z0-9._-]+@[a-z0-9]+\\.[a-z]{2,}";
        // Garante que o email seja validado e reescrito se necessário
        novaConta.email = reescrever_dado(validarDado(regexEmail, email), "email", regexEmail, email);
        System.out.println("\n--------------------------------------\nEMAIL DEFINIDO --> " + novaConta.email + " \n--------------------------------------\n");

        // Coleta e valida a senha
        String senha = criarInput("""
                       Digite sua senha:
                       sua senha deve conter, no mínimo:
                       - uma letra maiúscula
                       - um letra minúscula
                       - um número
                       - um caracter especial (@#$%^&+=)
                       - 8 caracteres no total
                       """);
        String regexSenha = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=])[^\\s]{8,}$";
        novaConta.senhaHash = reescrever_dado(validarDado(regexSenha, senha), "senha", regexSenha, senha);
        System.out.println("\n--------------------------------------\nSENHA DEFINIDA --> " + novaConta.senhaHash + " \n--------------------------------------\n");

        // Verifica se já existe uma conta com este email no mapa global de contas.
        boolean emailJaExiste = todasAsContas.values().stream()
                .anyMatch(c -> c.email.equalsIgnoreCase(novaConta.email));

        if (emailJaExiste) {
            System.out.println("Já existe uma conta com este email. Por favor, tente fazer login.");
            this.realizarLogin(); // Redireciona para o login
        } else {
            // Adiciona a nova conta ao mapa global de contas
            todasAsContas.put(novaConta.getIdConta(), novaConta);
            this.contaPessoal = novaConta; // Define a conta recém-criada como a conta ativa da sessão
            System.out.println("Conta criada com sucesso! ID: " + novaConta.getIdConta());
            System.out.println("Login efetuado automaticamente para a nova conta.");
        }
    }

    /**
     * Gerencia o processo de login em uma conta existente.
     * Busca a conta no mapa global e define a conta ativa da sessão.
     */
    private void realizarLogin() {
        System.out.println("\n[ ENTRAR EM UMA CONTA EXISTENTE ]\n");
        String inputEmail = criarInput("Digite seu email:");
        String inputSenha = criarInput("Digite sua senha:");

        Conta contaEncontrada = null;
        // Percorre o mapa de todas as contas para encontrar a conta pelo email.
        for (Conta c : todasAsContas.values()) {
            if (c.email.equalsIgnoreCase(inputEmail)) {
                contaEncontrada = c;
                break;
            }
        }

        if (contaEncontrada != null) {
            // Se a conta for encontrada, verifica a senha.
            if (contaEncontrada.senhaHash.equals(inputSenha)) {
                // Verifica se é uma ContaPessoal para ser a conta ativa da sessão.
                if (contaEncontrada instanceof ContaPessoal) {
                    this.contaPessoal = (ContaPessoal) contaEncontrada;
                    System.out.println("\nLogin efetuado com sucesso para " + this.contaPessoal.nome + "!");
                } else {
                    System.out.println("Esta é uma conta empresarial. Por favor, faça login com uma conta pessoal para usar este menu.");
                    this.contaPessoal = null; // Garante que nenhuma conta é selecionada
                }
            } else {
                System.out.println("Senha incorreta. Tente novamente.");
                this.realizarLogin(); // Tenta novamente
            }
        } else {
            System.out.println("\n[ Parece que esta conta não existe! :( ]");
            String resposta = criarInput("Tentar novamente digite (1) ( >_<'):\n----------------------------------------\nCriar uma nova conta digite (2) ( ^_^ )\n");
            if(resposta.equals("1")){
                this.realizarLogin();
            } else if (resposta.equals("2")){
                this.realizarCadastro();
            } else {
                System.out.println("Opção inválida. Retornando ao menu principal.");
            }
        }
    }

    /**
     * Exibe o menu de opções para a conta pessoal e gerencia as interações do usuário.
     * Este método é chamado somente após uma conta pessoal ser criada ou logada com sucesso.
     */
    public void contaPessoalMenu() {
        // Esta verificação é redundante se `iniciarSessao` for chamado primeiro,
        // mas é uma boa prática de segurança.
        if (this.contaPessoal == null) {
            System.out.println("Nenhuma conta pessoal selecionada. Retornando ao menu inicial.");
            return;
        }

        System.out.println("\nBem-vindo(a) de volta, " + this.contaPessoal.nome + "!");

        int opcao;
        while (!sair) {
            System.out.println("\n--- Menu de Opções da Conta Pessoal ---");
            System.out.println("ID da Conta: " + this.contaPessoal.getIdConta());
            System.out.println("Digite (1) para consultar o saldo ou depositar");
            System.out.println("Digite (2) para consultar sua carteira de investimentos");
            System.out.println("Digite (0) para sair");
            System.out.print("Sua opção: ");

            try {
                opcao = leitor.nextInt(); // Tenta ler a opção do usuário
                leitor.nextLine(); // Consome a quebra de linha pendente após nextInt()

                switch (opcao) {
                    case (1):
                        this.contaPessoal.consultarSaldo(); // Exibe o saldo atual
                        String entradaDeposito = criarInput("Deseja depositar? Digite (s) para sim ou (n) para não: ");
                        if (entradaDeposito.equalsIgnoreCase("s")) {
                            String valorStr = criarInput("Digite o valor para depositar: ").replace(",", ".");
                            try {
                                BigDecimal valorDeposito = new BigDecimal(valorStr);
                                this.contaPessoal.depositarSaldo(valorDeposito);
                            } catch (NumberFormatException e) {
                                System.out.println("Valor inválido. Por favor, digite um número.");
                            }
                        }
                        break;
                    case (2):
                        carteiraPessoal.consultarQuantidade();
                        break;
                    case (0):
                        System.out.println("Saindo do Menu da Conta Pessoal...");
                        sair = true; // Define a flag para sair do loop
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, digite um número válido (0, 1 ou 2).");
                        break;
                }

            } catch (java.util.InputMismatchException e) {
                // Captura exceção se o usuário digitar algo que não seja um número
                System.err.println("Entrada inválida. Por favor, digite um número.");
                leitor.next(); // Limpa o input inválido para evitar loop infinito
            }
        }
    }
}
