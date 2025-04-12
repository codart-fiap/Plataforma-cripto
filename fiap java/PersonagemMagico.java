public class PersonagemMagico {

    //Atributos
    String nome;
    String poderMagico;
    int nivelEnergia;

    HabilidadeEspecial habilidade;

    PersonagemMagico() {

    }

    public void atacar(String ataque) {
        if (nivelEnergia >= 10) {
            System.out.println(nome + " realizou um ataque: " + ataque + "!"  + "e gastou 10 de energia");
            nivelEnergia -= 10;
        } else {
            System.out.println(nome + " está sem energia para atacar.");
        }
    }

    public PersonagemMagico(String nome) {
        this.nome = nome;
    }
}
