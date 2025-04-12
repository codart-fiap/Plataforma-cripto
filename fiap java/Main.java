import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PersonagemMagico personagem = new PersonagemMagico();
        int op;
        do {
            System.out.println("""
                Escolha uma opção:  
1-Cadastrar Personagem 
2-Exibir Personagem 
3-Realizar ataque
0-Sair""");
            op = sc.nextInt();

            switch (op) {
                case 1:
                    System.out.println("Digite o nome do personagem:");
                    String nome = sc.next() + sc.nextLine();
                    System.out.println("Digite o poder do personagem:");
                    String poder =  sc.next() + sc.nextLine();
                    System.out.println("Digite o nível de energia:");
                    int energia = sc.nextInt();
                    personagem.nome = nome;
                    personagem.nivelEnergia = energia;
                    personagem.poderMagico = poder;
                    System.out.println("Digite o nome da habilidade especial:");
                    String nomeHabilidade = sc.next() + sc.nextLine();
                    System.out.println("Digite o custo de energia para usar a habilidade especial:");
                    int nivelEnergia = sc.nextInt();
                    System.out.println("A habilidade está ativada? (true/false):");
                    boolean ativada = sc.nextBoolean();
                    //Criando o objeto que representa a habilidade especial com os valores informados pelo usuário
                    HabilidadeEspecial habilidadeEspecial = new HabilidadeEspecial(nomeHabilidade, nivelEnergia, ativada);
                    //Atribuindo o objeto habilidade especial ao personagem
                    personagem.habilidade = habilidadeEspecial;
                    break;
                case 2:
                    System.out.println("Nome: " + personagem.nome + " Poder: " + personagem.poderMagico + " Nível energia: " + personagem.nivelEnergia);
                    System.out.println("Habilidade: " + personagem.habilidade.nome + " Custo energia: " + personagem.habilidade.custoEnergia + " Habilitada: " + personagem.habilidade.habilitada);
                    break;
                case 3:
                    System.out.println("Digite o nome do ataque:");
                    String ataque = sc.next() + sc.nextLine();
                    personagem.atacar(ataque);
                    break;
                case 0:
                    System.out.println("Finalizando o programa");
                default:
                    System.out.println("Opção inválida");
            }
        }while(op != 0);

        sc.close();

    }
}