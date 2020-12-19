package si;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFileChooser;
import file.FiltraExtensoes;

public class Sillhouette {

	public static File[] files;

	private static void AbrirTodos() {

		String url = "C:";

		JFileChooser chooser = null;
		chooser = new JFileChooser(url);
		chooser.addChoosableFileFilter(new FiltraExtensoes());
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			files = chooser.getSelectedFiles();
		}
	}

	public static void main(String[] args) throws IOException {
		AbrirTodos();
		StringBuilder sb = new StringBuilder();

		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(4);

		sb.append("o cabeçalho csv abaixo segue a ordem: nome do arquivo; silhouette geral; silhouette do grupo 1; do grupo 2; ..."
						+ "\n\n");
		sb.append("file;geral;g1;g2;g3;g4;g5;g6;g7;g8;g9;g10;g11;g12;g13;g14;g15;g16;g17;g18;g19;g20,..." + "\n");

		// System.out.print(sb.toString());

		// Calcular silhouette p/ os arquivos selecionados
		System.out.println("Iniciando cálculos de índices...\n");
		for (int i = 0; i < files.length; i++) {
			long d = System.currentTimeMillis();
			String result = new String(CalcSilhouette.CalcularSilhouette(files[i]) + "\n");
			sb.append(result);
			long d2 = System.currentTimeMillis();
			long r = d2 - d;
			System.out.print(files[i].getName() + " - " + milisToTime(r));
		}

		File saida = new File("S_index_teste.txt");

		FileOutputStream fos = new FileOutputStream(saida);
		fos.write(sb.toString().getBytes());
		fos.close();
		System.out.println();
		System.out.println("Arquivo " + saida + " salvo!");
	}
	
	public static String milisToTime(long milis) {
		
		long minutes = (milis / 1000) / 60;
        long seconds = (milis / 1000) % 60;
		
		return String.format("%d minutes and %d seconds.", minutes, seconds);
    }

}
	