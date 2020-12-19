package si;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import file.FiltraExtensoes;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class SilhouetteJcx {
	
	public static File [] files;
	public static String out="";
	public static String typeOfSilh = ""; // se arquivo de resultado começar com Cluster1 ou 0 caso contrário.
	public static int [] qtd;
	public static int indice; 
	public static double [][] matDist;
	public static Instances base;
	public static int colunas;
	public static double [] menor;
	public static double [] silhouette;
	

	private static void AbrirTodos() {
		
		String url = ".";
		
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
		
		// Perguntar pelo typeOfSilh
		typeOfSilh = JOptionPane.showInputDialog ("Digite 1 se arquivo de resultado começar com:" +
													"Cluster1 ou 0 caso contrário");
		
		// inicio do processamento ...
        Date d = GregorianCalendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat();
		System.out.println("Início do processamento - " + format.format(d));
		
		// Calcular silhouette p/ os arquivos selecionados
		for(int i = 0;i < files.length;i++) {
			out += CalcularSilhouette(files[i]);
		}
		
		File saida = new File("Si_TF-IDF_9-11.txt");
		
		FileOutputStream fos = new FileOutputStream(saida);    
        fos.write(out.toString().getBytes());  
        fos.close();        
        System.out.println("Arquivo " + saida + " salvo!");
	}

	private static String CalcularSilhouette(File file) throws IOException {
		String output = "";
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(4);
		
		FileInputStream inFile = new FileInputStream( file );
		InputStreamReader in = new InputStreamReader( inFile );
		Instances base = new Instances( in );
		base.setClassIndex( base.numAttributes() - 1 );
		
		AttributeStats as = base.attributeStats(base.classIndex());
		indice = base.numAttributes() - 1;
		
		qtd = new int [as.nominalCounts.length];
        for (int i = 0; i < as.nominalCounts.length; i++) {
        	qtd[i]= new Integer(as.nominalCounts[i]);
        	
        	// verificando quantidades por cluster ....
        	System.out.print("[qtd" + i + ": " + qtd[i] + "] ");
        }
        System.out.println("");
        colunas = 2;
        colunas += base.numClasses();
        matDist = new double [base.numInstances()][colunas];
        
        for (int i=0; i < base.numInstances(); i++){
        	for (int j=0; j < base.numInstances(); j++){
        		
        		if (i == j) continue;
        		
        		// implementação antiga com cluster começando em 1
        		if (typeOfSilh.equals("1")) {
        			
	        		if (base.get(i).stringValue(indice).equalsIgnoreCase(base.get(j).stringValue(indice))){
	        			matDist[i][0] += 
	        			Distancia (base.instance(i), base.instance(j)) / 
	        		    (qtd[Integer.parseInt(base.get(i).stringValue(indice).substring(7)) -1 ] - 1); 
	        		}
	        		else {
	        			matDist[i][Integer.parseInt(base.get(j).stringValue(indice).substring(7))] += 
	        			Distancia (base.instance(i), base.instance(j)) /
	        		    qtd[Integer.parseInt(base.get(j).stringValue(indice).substring(7)) -1 ];
	        		}
        		}
        		
        		// implementação nova com cluster começando em 0
        		if (typeOfSilh.equals("0")) {
        			
        			if (base.get(i).stringValue(indice).equalsIgnoreCase(base.get(j).stringValue(indice))){
            			matDist[i][0] += 
        					Distancia (base.instance(i), base.instance(j)) / 
        			        (qtd[Integer.parseInt(base.get(i).stringValue(indice).substring(7))] - 1); 
            		}
            		else {
            			matDist[i][Integer.parseInt(base.get(j).stringValue(indice).substring(7))+1] += 
        					Distancia (base.instance(i), base.instance(j)) /
        			        qtd[Integer.parseInt(base.get(j).stringValue(indice).substring(7))];
            		}
        		}
        	}
        }
        
        for (int i=0; i < base.numInstances(); i++){
        	double bi=0;
        	bi = Calcula_bi(i);
        	//System.out.println("a(i): " + matDist[i][0]);
        	matDist[i][colunas-1] = ( bi - matDist[i][0]) / Math.max(matDist[i][0], bi);
        }
        
        silhouette = new double [base.numClasses() + 1];
        for (int i=0; i < base.numInstances(); i++){
        	for (int j=1; j < colunas; j++){
        		if (matDist[i][j] == 0) {
        			silhouette[j-1] += matDist[i][colunas - 1]; 
        		}
        	}
        }
        
        for (int i=0; i < base.numClasses(); i++){
        	silhouette[i] = silhouette[i] / qtd[i]; 
        	silhouette[base.numClasses()] += silhouette[i]; 
        }
        silhouette[base.numClasses()] =  silhouette[base.numClasses()] / base.numClasses();
        
        // escrevendo arquivo e tempo de processamento ...
        Date d = GregorianCalendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat();
        
        System.out.println(file.getName() + " - " + format.format(d));
        output += file.getName() + "\n";
        for (int i=0; i < silhouette.length - 1; i++){
        	
        	if (typeOfSilh.equals("1")) {
        		// out put cluster 1
            	output +="Silhouette cluster" + (i+1) +  ": " + df.format(silhouette[i]) + "\n";
        	}
        	else {
        		// output cluster 0
            	output +="Silhouette cluster" + (i) +  ": " + df.format(silhouette[i]) + "\n";
        	}
        }
        output += "Silhouette Geral...: " + df.format(silhouette[base.numClasses()]) + "\n";
        output += "-----------------------------------------------------------------------------------" + "\n";
       
		return output;
	}

	private static double Calcula_bi(int i) {
		menor = new double[colunas - 3];
		int k=0;
		for (int j=1; j <= colunas - 2; j++){
			if (matDist[i][j] > 0) {
				menor[k] = matDist[i][j];
				k++;
			}
		}
		Bubblesort(menor); 
		//System.out.println(menor[0]);
		return menor[0];
	}

	private static void Bubblesort(double[] menor) {
		double aux;
		for (int i=1; i < menor.length; i++){
			 for (int j=0; j < menor.length-1; j++){
			      if (menor[j] > menor[j+1]){
			         aux = menor[j+1];
			         menor[j+1] = menor[j];
			         menor[j] = aux;
			      }
			 }
		}	   
	}

	private static double Distancia(Instance a, Instance b) {
		double diff=0;
		
		for ( int i = 1; i < a.numAttributes() -1; i++ ) {
			if( a.attribute( i ).isNominal() ){
				// testa se atributo é hieráquico
				if (a.stringValue(i).startsWith("R.")){
					//diff += Math.pow(computeDistance(a.stringValue(i), b.stringValue(i)), 2);
					diff += computeDistance(a.stringValue(i), b.stringValue(i));
				}
				// atributo nao hieraquico
				else {
					if (a.value( i ) == b.value( i )){
						diff += Math.pow(0,2);
						//System.out.println(a.attribute(i).name() + " = " + diff);
					}	
					else {	
						diff += Math.pow(1,2);
						//System.out.println(a.attribute(i).name() + " # " + diff);
					}	
				}
			}
			else {
				diff += Math.pow( a.value( i ) - b.value( i ), 2 );
				//diff += Math.abs( a.value( i ) - b.value( i ) );
				//System.out.println(a.attribute(i).name() + " # " + diff);
			}
		}
		diff = Math.sqrt(diff);
		return diff;
	}

	private static double computeDistance(String s1, String s2) {
		String[] t1 = s1.split("[.]");
		String[] t2 = s2.split("[.]");
		if (s1.compareTo(s2) == 0){
			return 0;
		}
		else {
			if (t1[1].compareToIgnoreCase(t2[1]) == 0){
				if (t1[2].compareToIgnoreCase(t2[2]) == 0){
					if (t1[3].compareToIgnoreCase(t2[3]) == 0){
						if (t1[4].compareToIgnoreCase(t2[4]) == 0) {
							if (t1[5].compareToIgnoreCase(t2[5]) == 0) {
								if (t1[6].compareToIgnoreCase(t2[6]) == 0) {
									return 0.015625;
								}
								else {
									return 0.03125;
								}
							}
							else {
								return 0.0625;
							}
						}
						else {
							return 0.125;
						}		
					}
					else {
						return 0.25;
					}						
				}
				else {
					return 0.5;
				}	
			}
			else {
				return 1;
			}
		}
	}
}
