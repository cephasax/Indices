package newdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.core.Instance;

public class DBCalculator {

	public double calcule(List<Instance> instances) {

		List<Group> groups = createGroups(instances);

		return this.getDB(groups);
	}

	private List<Group> createGroups(List<Instance> instances) {

		Map<Double, Group> groups = new HashMap<Double, Group>();

		for (Instance i : instances) {
			if (!groups.containsKey(i.classValue())) {
				groups.put(i.classValue(), new Group());
			}

			groups.get(i.classValue()).getInstances().add(i);
		}

		return new ArrayList<Group>(groups.values());
	}

	public double getDB(List<Group> groups) {

		// Calcular todos os Si
		Map<Group, Double> S = new HashMap<Group, Double>();

		for (Group i : groups) {
			double cont = 0;
			for (Instance j : i.getInstances()) {
				cont += (i.distance(i.getCentroid(), j) / i.size());
			}
			S.put(i, cont);
		}

		// Calcular todos os Mij
		Map<Group, Map<Group, Double>> Mij = new HashMap<Group, Map<Group, Double>>();

		for (Group i : groups) {
			Mij.put(i, new HashMap<Group, Double>());
			for (Group j : groups) {
				// Calculando distancia entre centróides
				Mij.get(i).put(j, j.distance(i.getCentroid(), j.getCentroid()));
			}
		}
		// Calcular todos os Rij
		Map<Group, Map<Group, Double>> Rij = new HashMap<Group, Map<Group, Double>>();

		for (Group i : groups) {
			Rij.put(i, new HashMap<Group, Double>());
			for (Group j : groups) {
				Rij.get(i).put(j, (S.get(i) + S.get(j)) / Mij.get(i).get(j));
			}
		}

		// Calcular Di
		Map<Group, Double> Di = new HashMap<Group, Double>();

		for (Group i : groups) {
			double max = Double.MIN_VALUE;
			for (Group j : groups) {
				if (!i.equals(j)) {
					double value = Rij.get(i).get(j);
					if (value > max) {
						max = value;
					}
				}
			}
			Di.put(i, max);
		}

		// Calcular Média dos valores de Di
		double cont = 0;

		for (Double d : Di.values()) {
			cont += (d / Di.size());
		}

		return cont;
	}
}
