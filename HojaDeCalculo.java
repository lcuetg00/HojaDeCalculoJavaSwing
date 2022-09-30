
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
 * Anyadidos dos constructores extra (uno para una hoja de calculo vacia y otro para cargar
 * la hoja desde un archivo).
 * Anyadidos metodos toString a las clases de celda. 
 * Anyadido metodo que devuelve una celda
 * Puesta a interfaz celda a public
 * Anyadido un metodo para insertar celdas en la hoja de calculo
 * Anyadido metodo crear celda
 */
public class HojaDeCalculo {

	Celda[][] celdas;
	public final int filas;
	public final int columnas;
	
	
	public HojaDeCalculo(int filas, int columnas, String[] datos) {
		celdas= new Celda[filas][columnas];
		asignarFilas(datos);
		this.filas=filas;
		this.columnas=columnas;
	}
	
	public HojaDeCalculo(int filas, int columnas) {
		celdas= new Celda[filas][columnas];
		for(int i=0;i<filas;i++) {
			for(int j=0;j<columnas;j++) {
				celdas[i][j]= new CeldaValor(0);
			}
		}
		this.filas=filas;
		this.columnas=columnas;
	}
	
	public HojaDeCalculo(File archivo) throws FileNotFoundException {
		Scanner scan = new Scanner(archivo);
		
		
		String linea= scan.nextLine();
		this.columnas=Integer.parseInt(linea.substring(0,linea.indexOf(' ')));
		this.filas=Integer.parseInt(linea.substring(linea.indexOf(' ')+1));
		if(columnas<=0 || filas<=0) {
			throw new IllegalArgumentException("El número de filas y columnas no puede ser cero o menor que 0");
		}
		String[] lineaFilas= new String[filas];
		for(int j=0;j<filas;j++) { //Recopilo las filas de la hoja de calculo
			lineaFilas[j]=scan.nextLine()+' ';
		}

		celdas= new Celda[filas][columnas];
		asignarFilas(lineaFilas);
	}
	
	
	public void guardarHojaCalculo(File ubicacion) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(ubicacion));
		
	    writer.write(columnas+" "+filas+"\n");

		String hoja="";
		for(int i=0;i<celdas.length;i++) {
			for(int j=0;j<celdas[i].length;j++) {
				hoja=hoja.concat(celdas[i][j].toString()+" ");
			}
			hoja=hoja.substring(0,hoja.length()-1);
			hoja=hoja.concat("\n");
				
		}
		 writer.write(hoja);
	    writer.close();
	}
	
	public int getValue(int x, int y) {
		return celdas[x][y].getValue();
		
	}
	
	public void insertarCelda(Celda celda, int fila, int columnas) {
		celdas[fila][columnas]=celda;
		return;
	}
	
	public Celda getCelda(int filas, int columnas) {
		return celdas[filas][columnas];
	}
	
	public void asignarCelda(String s, int x, int y) {
		if(s.charAt(0)=='=') {
			CeldaFormula celdaF= new CeldaFormula(s.substring(1),this);
			celdas[x][y]=celdaF;
		} else {
			CeldaValor celdaV= new CeldaValor(Integer.parseInt(s));
			celdas[x][y]=celdaV;
		}
	
	}
	
	public Celda crearCelda(String s) {
		Celda celda;
		if(s.charAt(0)=='=') {
			celda= new CeldaFormula(s.substring(1),this);
		} else {
			celda= new CeldaValor(Integer.parseInt(s));
		}
		return celda;
	}

	private void asignarFilas(String[] filas) {
		int numElementosRegistrados=0;
		for(int i=0;i<celdas.length;i++) {
			for(int j=0;j<celdas[i].length;j++) {
				if(filas[i].length()==0) {
					throw new IllegalArgumentException("Hay espacios en la entrada");
				}
				asignarCelda(filas[i].substring(0,filas[i].indexOf(' ')),i,j);
				filas[i]=filas[i].substring(filas[i].indexOf(' ')+1);
				numElementosRegistrados++;
				
			}
			if(filas[i].length()>0) {
				throw new IllegalArgumentException("Hay valores de más");
			}
			
				
		}
		
	}
	@Override
	public String toString() {
		String hoja="";
		for(int i=0;i<celdas.length;i++) {
			for(int j=0;j<celdas[i].length;j++) {
				hoja=hoja.concat(celdas[i][j].getValue()+" ");
			}
			hoja=hoja.substring(0,hoja.length()-1);
			hoja=hoja.concat("\n");
				
		}
		
		return hoja;
	}
	
	
	
}