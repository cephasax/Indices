package cr;

public class CalcCrIndex {

	public static void main(String[] args) {
		
		CrIndex cr = new CrIndex();
		
		System.out.println("Selecione a base original");
		cr.AbrirBaseOriginal();
		cr.criarInstancesOriginal();
		
		System.out.println("Selecione as bases resultantes dos agrupamentos");
		cr.AbrirTodos();
		cr.criarInstancesNovas();
		
		for(int i = 0; i < cr.getBasesNovas().size(); i++){
			double d = AdjustedRandIndex.ARI(cr.getBaseOriginal(), cr.getBasesNovas().get(i));
			System.out.println("Base: " + cr.getFiles()[i].getName() + " - CR Index: " + d);
			System.out.println("-------------------------------------");
			System.out.println();
		}		

	}

}
