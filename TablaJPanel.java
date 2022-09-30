import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



class TablaJPanel extends JScrollPane {
	public final Color COLOR_POR_DEFECTO;
	public final Color COLOR_EN_SELECCION;
	public final int TAMANYO_LISTA_CAMBIOS=999;
	
	HojaDeCalculo hoja;
	JPanel tablaPanel;
	FocusListener escuchaTextField;
	Actualizacion[] listaCambios;
	int punteroListaCambios;
	TextFieldCelda[][] listaTextFields;
	
	public TablaJPanel(FocusListener escuchaTextField) { //le paso el listener de los texfield
		//como parametro para luego anyadirselo a cada textfield
		super();
		this.escuchaTextField = escuchaTextField;
		COLOR_POR_DEFECTO = new JTextField().getBackground();
		COLOR_EN_SELECCION = COLOR_POR_DEFECTO.darker();
		listaCambios=new Actualizacion[TAMANYO_LISTA_CAMBIOS]; 
		punteroListaCambios = 0;
		
		/*
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		String[] columnNames = new String[5];
		columnNames[0] = "";
		for(int i=1; i<5; i++) {
			columnNames[i] = numALetras(i);
		}
		String[][] tableContent = new String[5][5];
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				tableContent[i][j] = Integer.toString(i*j);
			}
		}
		hojaTable= new JTable(tableContent,columnNames); //le pasamos como argumento el contenido
		this.add(hojaTable);
		*/
	}
	
	private void generar() {
		listaTextFields = new TextFieldCelda[hoja.filas][hoja.columnas];
		if(tablaPanel != null) {
			this.remove(tablaPanel);
		}
		
		JPanel tablaPanelInterna = new JPanel();
		
		tablaPanel= new JPanel(new GridLayout(hoja.filas+1,hoja.columnas+1));
		
		for(int i=0;i < hoja.columnas+1;i++) {
			JLabel etiqueta = new JLabel(MainUI.numALetras(i));
			etiqueta.setPreferredSize(new Dimension(80,20));
			tablaPanel.add(etiqueta);
		}
		for(int i=0;i < hoja.filas;i++) {
			JLabel etiqueta = new JLabel(Integer.toString(i+1));
			etiqueta.setPreferredSize(new Dimension(80,20));
			tablaPanel.add(etiqueta);
			for(int j=0;j<hoja.columnas;j++) {
				TextFieldCelda textField= new TextFieldCelda(hoja.getCelda(i,j).toString(),i,j);
				textField.setPreferredSize(new Dimension(80,20));
				textField.setEditable(false);
				textField.addFocusListener(escuchaTextField);
				textField.setBackground(COLOR_POR_DEFECTO);
				listaTextFields[i][j]=textField;
				tablaPanel.add(textField);
			}
		}
		
		tablaPanelInterna.add(tablaPanel);
		this.setViewportView(tablaPanelInterna);
		
		// Recolocar el nuevo panel en el viewport para que sea visible
		
		//this.setViewportView(tablaPanel);
	}
	
	public void establecerHoja(int filas, int columnas) {
		hoja=new HojaDeCalculo(filas,columnas);
		this.generar();
		
	}
	
	
	
	public void establecerHojaFichero(File archivo) throws FileNotFoundException {
		hoja= new HojaDeCalculo(archivo);
		this.generar();
	}
	
	public void deshacerCambios() {
		punteroListaCambios--;
		Actualizacion cambios = listaCambios[punteroListaCambios];
		cambios.deshacer();
	}
	
	public void rehacerCambios() {
		Actualizacion rehacer = listaCambios[punteroListaCambios];
		rehacer.rehacer();
		punteroListaCambios++;
		
	}
	
	public boolean poderDeshacer() {
		boolean poderDeshacer = true;
		if(punteroListaCambios==0) {
			poderDeshacer = false;
		}
		return poderDeshacer;
	}
	
	public boolean poderRehacer() {
		boolean poderRehacer=true;
		if(listaCambios[punteroListaCambios]==null) {
			poderRehacer=false;
		}
		return poderRehacer;
	}
	
	class TextFieldCelda extends JTextField { //Creo una nueva clase para los textfields, que almacena
		//la posicion en la celda para que el listener sepa que celda es
		public final int fila;
		public final int columna;
		public TextFieldCelda(String texto, int fila, int columna) {
			super(texto);
			this.fila = fila;
			this.columna = columna;
		}
		
		public void actualizarValor(String texto) {
			try {
				if(!texto.equals(hoja.getCelda(fila, columna).toString())) {
					Celda celdaNueva = hoja.crearCelda(texto);
					ActualizacionCelda actualizacion= new ActualizacionCelda(
							hoja.getCelda(fila,columna),celdaNueva, fila, columna);
					listaCambios[punteroListaCambios]=actualizacion;
					punteroListaCambios++;
					this.setText(texto);
					hoja.asignarCelda(texto, fila, columna);
					purgarListaCambios();
				}

			} catch(Exception e) {
				JOptionPane.showMessageDialog(tablaPanel,
					    "No se puede registrar el valor "+texto+" en la hoja de calculo.",
					    "Error en la inserccion del valor",
					    JOptionPane.WARNING_MESSAGE);

			}
		}
		

	}
	
	/*
	 * Recorre las celdas de tablaPanel. Si alguna de estas es una CeldaFormula, calcua el valor de su fórmula
	 */
    public void calcularFormulas() {
    	Component component = null;
    	int punteroPanel = 0;
    	ActualizacionCelda[] lista = new ActualizacionCelda[hoja.filas*hoja.columnas];
    	int punteroLista = 0;
    	
	    for(int i=0;i < hoja.filas;i++) {	
	    	for(int j=0;j<hoja.columnas;j++) {
	    		do {
	    			component=tablaPanel.getComponent(punteroPanel++);
	    			
	    		}while(!(component instanceof JTextField));
	        	if(hoja.getCelda(i,j) instanceof CeldaFormula) {
	        		int valor = hoja.getCelda(i,j).getValue();
	        		CeldaValor nuevaCelda = new CeldaValor(valor);
	        		lista[punteroLista++] = new ActualizacionCelda(hoja.getCelda(i,j),nuevaCelda,i,j);
	        		hoja.insertarCelda(nuevaCelda,i,j);
	        		((JTextField)component).setText(Integer.toString(valor));
	        	}
	        }
	    }
	    if(lista[0]!=null) {
	    	listaCambios[punteroListaCambios++] = new ActualizacionTabla(lista);
			purgarListaCambios();
	    }
    }
    
    public void borrarRegistroCambios() {
    	for(int i=0;i<listaCambios.length && listaCambios[i]!=null;i++) {
    		listaCambios[i] = null;
    	}
    	punteroListaCambios=0;
    }
    
    public void purgarListaCambios() {
    	for(int i=punteroListaCambios;i<listaCambios.length && listaCambios[i]!=null;i++) {
    		listaCambios[i] = null;
    	}
    }
    
    public interface Actualizacion{
    	
    	public void deshacer();
    	public void rehacer();
    	
    }
    
    class ActualizacionTabla implements Actualizacion {
    	ActualizacionCelda[] lista;
    	
    	public ActualizacionTabla(ActualizacionCelda[] lista) {
    		this.lista = lista;
    		
    	}

		@Override
		public void deshacer() {
			for(int i=0;(i < lista.length) && (lista[i] != null);i++) {
				lista[i].deshacer();
			}
		}

		@Override
		public void rehacer() {
			for(int i=0;(i < lista.length) && (lista[i] != null);i++) {
				lista[i].rehacer();
			}
		}
    	
    }
	
    class ActualizacionCelda implements Actualizacion {
    	private Celda celdaAntigua;
    	private Celda celdaNueva;
    	private int fila;
    	private int columna;
    	public ActualizacionCelda(Celda celdaAntigua, Celda celdaNueva, int fila, int columna) {
    		this.celdaAntigua = celdaAntigua;
    		this.celdaNueva = celdaNueva;
    		this.fila = fila;
    		this.columna = columna;
    	}
		@Override
		public void deshacer() {
			hoja.insertarCelda(celdaAntigua,fila,columna);
			listaTextFields[fila][columna].setText(celdaAntigua.toString());
		}
		@Override
		public void rehacer() {
			hoja.insertarCelda(celdaNueva, fila, columna);
			listaTextFields[fila][columna].setText(celdaNueva.toString());
		}
    	
    	
    }
    
    
}