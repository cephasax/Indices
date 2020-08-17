package newdb;

import java.util.ArrayList;
import java.util.List;

import weka.core.Instance;

public class Group {

	private List<Instance> instances;

	public Group() {
		this.instances = new ArrayList<Instance>();
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public int size() {
		return this.instances.size();
	}

	public Instance getCentroid() throws RuntimeException {

		switch (this.size()) {
		case 0:
			throw new RuntimeException("GRUPO VAZIO NÃO TEM CENTRÓIDE");
		case 1:
			return this.getInstances().get(0);
		default:
			// Calcular ponto médio e depois escolher a instância mais próxima do ponto
			// médio
			Instance copy = (Instance) this.getInstances().get(0).copy();

			for (int idAtt = 0; idAtt < copy.numAttributes(); idAtt++) {
				double cont = 0;
				for (Instance i : this.getInstances()) {
					cont += i.value(idAtt);
				}
				cont = cont / this.size();
				copy.setValue(idAtt, cont);
			}

			Instance min = this.getInstances().get(0);

			double minValue = this.distance(copy, min);
			for (Instance i : this.getInstances()) {
				double distance = this.distance(copy, i);
				if (distance < minValue) {
					min = i;
					minValue = distance;
				}
			}

			return min;
		}

	}

	public double distance(Instance x, Instance y) {

		if (x == null || y == null) {
			return Double.POSITIVE_INFINITY;
		}

		if (x.numAttributes() != y.numAttributes())
			throw new IllegalArgumentException(
					String.format("Arrays have different length: x[%d], y[%d]", x.numAttributes(), y.numAttributes()));

		int n = x.numAttributes();
		double dist = 0.0;
		for (int i = 0; i < n; i++) {
			if (!Double.isNaN(x.value(i)) && !Double.isNaN(y.value(i))) {
				double d = x.value(i) - y.value(i);
				dist += d * d;
			}
		}
		return Math.sqrt(dist);
	}

}
