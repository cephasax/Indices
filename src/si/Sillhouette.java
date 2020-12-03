package si;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import file.FiltraExtensoes;

public class Sillhouette {
	
	public static File [] files;
	public static String out="";
	public static File fileAtual;

	private static void AbrirTodos() {
		
		String url = "";
		
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
		
		// Calcular silhouette p/ os arquivos selecionados
		for(int i = 0;i < files.length;i++) {
			fileAtual = files[i];
			out += CalcSilhouette.CalcularSilhouette(files[i]);
		}
		
		File saida = new File("Sillhouette_dbscan_nut.txt");
		
		FileOutputStream fos = new FileOutputStream(saida);    
        fos.write(out.toString().getBytes());  
        fos.close();        
        System.out.println("Arquivo " + saida + " salvo!");
	}
}