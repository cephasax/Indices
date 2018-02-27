package cr;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import file.FiltraExtensoes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class CrIndex {

	public  File [] files;
	public File fileBaseOriginal;
	
	public ArrayList<Instances> basesNovas = new ArrayList<Instances>();
	public Instances baseOriginal; 	
	
	public Instances loadDataset(String base) {
		Instances dataset = null;

		try {
			DataSource source = new DataSource(base);
			dataset = source.getDataSet();
			dataset.setClassIndex(dataset.numAttributes() - 1);
		} catch (Exception e) {
			System.out.println("Não foi possível carregar a base!!\n\nbase");
			e.printStackTrace();
			System.exit(1);
		}

		return dataset;
	}
	
	public void AbrirBaseOriginal() {		
		
		String url = "C:\\Program Files\\Wekas\\Weka-scr\\Weka-scr\\results\\classWithout-cluster";
		
		JFileChooser chooser = null;
		chooser = new JFileChooser( url );
		chooser.addChoosableFileFilter(new FiltraExtensoes());
		chooser.setMultiSelectionEnabled(false);
		if( chooser.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			fileBaseOriginal = chooser.getSelectedFile();
		}
	}
	
	public void AbrirTodos() {		
		
		String url = "C:\\Program Files\\Wekas\\Weka-scr\\Weka-scr\\results\\classWithout-cluster";
		//String url = "D:/JCXavier/Doutorado/Sandwich/BaseDados/Viagem";
		
		JFileChooser chooser = null;
		chooser = new JFileChooser( url );
		chooser.addChoosableFileFilter(new FiltraExtensoes());
		chooser.setMultiSelectionEnabled( true );
		if( chooser.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			files = chooser.getSelectedFiles();
		}
	}

	public void criarInstancesNovas(){
		for(File file: this.getFiles()){
			basesNovas.add(loadDataset(file.getPath()));
		}
	}
	
	public void criarInstancesOriginal(){
		baseOriginal = loadDataset(fileBaseOriginal.getPath());
	}
	
	//GETTERS and SETTERS
	public Instances getBaseOriginal() {
		return baseOriginal;
	}

	
	public void setBaseOriginal(Instances baseOriginal) {
		this.baseOriginal = baseOriginal;
	}

	
	public File[] getFiles() {
		return files;
	}

	
	public void setFiles(File[] files) {
		this.files = files;
	}

	
	public File getFileBaseOriginal() {
		return fileBaseOriginal;
	}

	
	public void setFileBaseOriginal(File fileBaseOriginal) {
		this.fileBaseOriginal = fileBaseOriginal;
	}

	
	public ArrayList<Instances> getBasesNovas() {
		return basesNovas;
	}

	
	public void setBasesNovas(ArrayList<Instances> basesNovas) {
		this.basesNovas = basesNovas;
	}
	
	
	
	
}
