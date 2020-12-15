package db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;

import file.FiltraExtensoes;

public class DaviesBouldin {
	

	private static List<File> AbrirTodos() {		
		
		String url = "C:";
		List<File> files = new ArrayList<File>();
		JFileChooser chooser = null;
		chooser = new JFileChooser( url );
		chooser.addChoosableFileFilter(new FiltraExtensoes());
		chooser.setMultiSelectionEnabled( true );
		if( chooser.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			files = Arrays.asList(chooser.getSelectedFiles());
		}
		return files;
	}
	
	public static void main (String [] args) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		List<File> files = new ArrayList<File>();
		files = AbrirTodos();
		
		//CalcDB cdb = new CalcDB();
		//Calcular DB p/ os arquivos selecionados
		for(File file: files) {
			System.out.println(file.getName());
			String result = CalcDB.calcularDB(file);
			sb.append(file.getName() + " \t " + result + "\n");
		}
		
		File saida = new File("db_jcx_teste.txt");
		FileOutputStream fos = new FileOutputStream(saida);    
        fos.write(sb.toString().getBytes());  
        fos.close();        
        System.out.println("Arquivo " + saida + " salvo!");
	}	
	
}