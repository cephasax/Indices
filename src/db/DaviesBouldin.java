package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;

import file.FiltraExtensoes;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

public class DaviesBouldin {
	public static File [] files;
	public static String out=""; 

	public static File fileAtual;
	
	public interface Mensurably<T> {
		public double distance ( T a, T b );
	}

	private static void AbrirTodos() {		
		
		String url = "C:\\Users\\Cephas\\Google Drive\\Pessoal\\Mestrado\\Dissertacao\\dados\\experimento_perfil\\bases\\modificadas\\final_exp\\controle";
		//String url = "D:/JCXavier/Doutorado/Sandwich/BaseDados/Viagem";
		
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
		Mensurably <Instance> difference = new Mensurably <Instance>() {
			public double distance ( Instance a, Instance b ) {
				double diff = 0;
				for (int i = 1; i < a.numAttributes() - 1; i++) {
					if ( a.attribute( i ).isNominal() ) {
						// testa se atributo é hieráquico
						if (a.stringValue(i).startsWith("R.")){
							diff += computeDistance(a.stringValue(i), b.stringValue(i));
						}
						// atributo nao hieraquico
						else {
							diff += a.value( i ) == b.value( i ) ? 0 : 1;
						}
					}					
					// atributo numerico	
					else{
					   diff += Math.abs( a.value( i ) - b.value( i ) );
					   //diff += Math.sqrt(Math.pow(a.value( i ), 2) - Math.pow(b.value( i ), 2));
					}				
				}
				return diff;
			}

			private double computeDistance(String s1, 	String s2) {
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
		};	

		// Calcular DB p/ os arquivos selecionados
		for(int i = 0;i < files.length;i++) {
			fileAtual = files[i];
			FileInputStream inFile = new FileInputStream( files[i] );
			InputStreamReader in = new InputStreamReader( inFile );
			Instances base = new Instances( in );
			base.setClassIndex( base.numAttributes() - 1 );

			DaviesBouldin bdb = new DaviesBouldin();

			out += files[i] + "\n";
			out += "File: " + fileAtual.getName() + " - DB index = " + bdb.daviesBouldin( base, difference ) + "\n";
			out += "------------------------------------------------------------------------------\n";
		}		
		
		
		//File saida = new File("Trip_kMeans_RC-ER_DB.txt");
		File saida = new File("DB_perfil.txt");
		
		FileOutputStream fos = new FileOutputStream(saida);    
        fos.write(out.toString().getBytes());  
        fos.close();        
        System.out.println("Arquivo " + saida + " salvo!");
	}	
		
	public double daviesBouldin ( Instances base, Mensurably <Instance> difference ) {
		Instances [] group = new Instances [base.numClasses()];
		for( int i = 0; i < group.length; i++ ){
			group[ i ] = new Instances( base, 0 );
		}
		
		for( int i = 0; i < base.numInstances(); i++ ) {
			group[ (int) base.instance( i ).classValue() ].add( base.instance( i ) );
		}

		double [] E = new double [group.length];
		Instance [] center = new Instance [group.length];
		for( int i = 0; i < group.length; i++ ) {
			center[ i ] = this.center( group[ i ] );
			center[ i ].setDataset( group[ i ] );
			E[ i ] = medianSquaredDistance( group[ i ], center[ i ], difference );
		}

		double db = 0;
		for( int i = 0; i < group.length; i++ ) {
			db += mrs( i, E, center, difference );
		}
		return db / (double) group.length;
	}

	protected double mrs ( int index, double [] E, Instance [] center, Mensurably <Instance> groupDistance ) {
		double max = Double.NEGATIVE_INFINITY;
		for( int i = 0; i < center.length; i++ ) {
			if( i != index ) {
				double aux = rs( E[ index ], E[ i ], center[ index ], center[ i ], groupDistance );
				if( aux > max ) {
					max = aux;
				}
			}
		}
		return max;
	}

	protected double rs ( double E1, double E2, Instance center1, Instance center2, Mensurably <Instance> groupDistance ) {
		return ( E1 + E2 ) / ( groupDistance.distance( center1, center2 ) );
	}

	protected double medianSquaredDistance ( Instances base, Instance center, Mensurably <Instance> difference ) {
		double median = 0;
		for( int i = 0; i < base.numInstances(); i++ )
			median += Math.pow( difference.distance( base.instance( i ), center ), 2 );
		return median / (double) base.numInstances();
	}

	protected Instance center ( Instances base ) {
		Instance center = new SparseInstance( base.firstInstance() );
		for( int i = 0; i < base.numAttributes(); i++ ) {
			if( base.attribute( i ).isNominal() )
				center.setValue( i, moda( base, base.attribute( i ) ) );
			else if( base.attribute( i ).isNumeric() )
				center.setValue( i, median( base, base.attribute( i ) ) );
			else throw new IllegalArgumentException( "Attribute " + base.attribute( i ).name() + " not is numeric or nominal" );
		}
		return center;
	}

	protected double moda ( Instances base, Attribute att ) {
		double [] count = new double [att.numValues()];
		for( int i = 0; i < base.numInstances(); i++ )
			count[ (int) base.instance( i ).value( att ) ]++;
		return maxIndex( count );
	}

	protected double median ( Instances base, Attribute att ) {
		double median = 0;
		for( int i = 0; i < base.numInstances(); i++ )
			median += base.instance( i ).value( att );
		return median / (double) base.numInstances();
	}

	private int maxIndex ( double ... values ) {
		int max = 0;
		for( int i = 1; i < values.length; i++ )
			if( values[ i ] > values[ max ] )
				max = i;
		return max;
	}
}