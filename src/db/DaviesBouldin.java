package db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;

import file.FiltraExtensoes;

public class DaviesBouldin {
	
	public static File [] files;
	public static String out=""; 
	public static File fileAtual;

	private static void AbrirTodos() {		
		
		String url = "C:\\Users\\Cephas\\Google Drive\\Pessoal\\Mestrado\\Dissertacao\\dados\\experimento_perfil\\bases\\modificadas\\final_exp\\controle";
		
		JFileChooser chooser = null;
		chooser = new JFileChooser( url );
		chooser.addChoosableFileFilter(new FiltraExtensoes());
		chooser.setMultiSelectionEnabled( true );
		if( chooser.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			files = chooser.getSelectedFiles();
		}
	}
	
	public static void main ( String [] args ) throws IOException {
		AbrirTodos();
		CalcDB cdb = new CalcDB();
		// Calcular DB p/ os arquivos selecionados
		for(int i = 0;i < files.length;i++) {
			cdb = new CalcDB();
			fileAtual = files[i];
			out += cdb.calcularDB(fileAtual) + "\n";
		}		
		
		File saida = new File("DB_controle.txt");
		
		FileOutputStream fos = new FileOutputStream(saida);    
        fos.write(out.toString().getBytes());  
        fos.close();        
        System.out.println("Arquivo " + saida + " salvo!");
        
	}	
	
}