package newdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;

import file.FiltraExtensoes;
import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * @author usa a implementação de Netinho
 *
 */
public class DaviesBouldinNewDB {
	

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
		
		FileInputStream inFile;
		InputStreamReader in;
		Instances base;
		List<Instance> listInstances;
		DBCalculator dbc = new DBCalculator();
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(4);
		
		//CalcDB cdb = new CalcDB();
		//Calcular DB p/ os arquivos selecionados
		for(File file: files) {
			inFile = new FileInputStream(file);
			in = new InputStreamReader(inFile);
			base = new Instances(in);
			base.setClassIndex(base.numAttributes() - 1);
			listInstances = new ArrayList<Instance>(base);
			String result = df.format(dbc.calcule(listInstances));
			sb.append(file.getName() + " \t " + result + "\n");
			System.out.print(file.getName() + " \t " + result + "\n");
		}
		
		File saida = new File("newDb_teste.txt");
		FileOutputStream fos = new FileOutputStream(saida);    
        fos.write(sb.toString().getBytes());  
        fos.close();        
        System.out.println("\nArquivo " + saida + " salvo!");
	}	
	
}