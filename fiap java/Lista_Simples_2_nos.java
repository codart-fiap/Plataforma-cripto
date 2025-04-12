public class Lista_Simples_2_nos {
	
	private  static class NO {
		  public int dado;
		  public NO prox;
		  }

	public static void main(String[] args) {
		
		NO lista = null;
		System.out.println("Valor ponteiro lista= " + lista);
		for(int i =1; i<=2;i++) {
			NO novo = new NO();
			novo.dado= i+4;
			novo.prox = lista;
			System.out.println("a lista era:"+lista);				
			lista = novo;	
			System.out.println("dado:"+novo.dado);				
			System.out.println("a nova lista é:"+lista.prox);				
		}

		// System.out.println("Dado do NO apontado por lista= "+lista.dado);
		// System.out.println("Dado do NO apontado por prox " +lista.prox.dado);
	}
}