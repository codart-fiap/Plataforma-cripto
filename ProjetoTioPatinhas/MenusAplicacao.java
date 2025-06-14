package ProjetoTioPatinhas;
import java.util.Scanner;
public class MenusAplicacao {
    Scanner leitor = new Scanner(System.in);
    ContaPessoal contaPessoal = new ContaPessoal();
    Carteira carteiraPessoal = new Carteira(contaPessoal);
    boolean sair = false;

    public void menuLoginCadastro() {


    }

    public void contaPessoalMenu() {
        int opcao;

        while (!sair) {
            System.out.println("Menu de opções\n Digite (1) para consultar o saldo ou depositar \n Digite (2) para consultar sua carteira de investimentos ");

            try {
                opcao = leitor.nextInt(); //Tenta ler o input do usuario
                leitor.nextLine();
                switch (opcao) {
                    case (1):
                        contaPessoal.consultarSaldo();
                        this.contaPessoalMenu();
                        break;

                    case (2):
                        carteiraPessoal.consultarQuantidade();
                        break;
                    case (0):
                        System.out.println("SAindo");
                        sair = true;
                        break;

                    default:
                        System.out.println("Opção inválida");
                        break;
                }

            } catch (java.util.InputMismatchException e) { //Se o input for diferente de um numero
                System.err.println("Entra da inválida. Por favor, digite um número.");
                leitor.next();


            }


        }
    }
}