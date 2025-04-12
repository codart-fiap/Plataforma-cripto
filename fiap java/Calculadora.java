public class Calculadora {
    public int somar(int x, int y) {
    return x + y;
    }
    }
    class Principal {
    static void main(String[] args) {
    int a = 10;
    int b = 5;
    int resultado = Calculadora.somar(a, b);
    System.out.println("O resultado da soma é: " + resultado);
    }
    }
