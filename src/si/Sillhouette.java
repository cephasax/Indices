package si;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import file.FiltraExtensoes;

public class Sillhouette {
	
	public static File [] files;
	public static File fileAtual;

	private static void AbrirTodos() {
		
		String url = "C:";
		
		JFileChooser chooser = null;
		chooser = new JFileChooser( url );
		chooser.addChoosableFileFilter(new FiltraExtensoes());
		chooser.setMultiSelectionEnabled( true );
		if( chooser.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			files = chooser.getSelectedFiles();
		}
	}
	
	public static void main(String[] args) throws IOException {
		AbrirTodos();
		StringBuilder sb = new StringBuilder();
	    
	    sb.append("o cabeçalho csv abaixo segue a ordem: nome do arquivo; silhouette geral; silhouette do grupo 1; do grupo 2; ..." +"\n\n");
	    sb.append("file;geral;g1;g2;g3;g4;g5;g6;g7;g8;g9;g10;g11;g12;g13;g14;g15;g16;g17;g18;g19;g20,..." + "\n");
		
		// Calcular silhouette p/ os arquivos selecionados
		for(int i = 0;i < files.length;i++) {
			fileAtual = files[i];
			sb.append(CalcSilhouette.CalcularSilhouette(files[i]) + "\n");
		}
		
		File saida = new File("S_index_teste.txt");
		
		FileOutputStream fos = new FileOutputStream(saida);    
        fos.write(sb.toString().getBytes());  
        fos.close();        
        System.out.println("Arquivo " + saida + " salvo!");
	}
}