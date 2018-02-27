package cr;

import weka.core.Instances;

public class AdjustedRandIndex {

	//X base original
	//y base nova
	public static double ARI(Instances X, Instances Y) {
		// Tabela de contigencia
		int[][] table = new int[X.numClasses() + 1][Y.numClasses() + 1];

		// Numero de objetos
		int numInstances = X.numInstances();

		// Popula a tabela
		int xClass, yClass;
		for (int i = 0; i < numInstances; i++) {
			xClass = (int) X.instance(i).classValue();
			yClass = (int) Y.instance(i).classValue();
			table[xClass][yClass]++;
		}

		for (int i = 0; i < table.length - 1; i++) {
			for (int j = 0; j < table[i].length - 1; j++) {
				table[table.length - 1][j] += table[i][j]; // Computa a Ãºltima
															// linha
				table[i][table[i].length - 1] += table[i][j]; // Computa a
																// Ãºltima coluna
			}
		}

		double TERMO_A = 0;
		for (int i = 0; i < table.length - 1; i++) {
			for (int j = 0; j < table[i].length - 1; j++) {
				TERMO_A += Mathematics.combinationOf(table[i][j], 2);
			}
		}

		double TERMO_B = 0;
		double TERMO_C = 0;
		for (int i = 0; i < table.length - 1; i++) {
			TERMO_B += Mathematics.combinationOf(
					table[i][table[i].length - 1], 2); // Ultima coluna
			// System.out.printf("B ~> %d-%d\n", i, table[i].length - 1);
		}
		
		for (int i = 0; i < table[table.length-1].length - 1; i++) {
			TERMO_C += Mathematics.combinationOf(table[table.length - 1][i],
					2); // Ultima linha
			// System.out.printf("C ~> %d-%d\n", table.length - 1, i);
		}
				
		double TERMO_D = Mathematics.combinationOf(numInstances, 2);

		double INDEX = TERMO_A;
		double EXP_INDEX = (TERMO_B * TERMO_C) / TERMO_D;
		double MAX_INDEX = 0.5 * (TERMO_B + TERMO_C);

//		print(table);

		return ((INDEX - EXP_INDEX) / (MAX_INDEX - EXP_INDEX));
	}
	
	public static void print(int[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				System.out.printf("%2d ", table[i][j]);
			}
			System.out.println();
		}
	}

	
}
