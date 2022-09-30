import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;


public class MainUI {
	public final static String EVENTO_INVALIDO_0= "cause=UNKNOWN";
	public final static String EVENTO_INVALIDO_1= "cause=ROLLBACK";
	public final static String EVENTO_INVALIDO_2="cause=TRAVERSAL_FORWARD";
	JFrame mainFrame;
		FormulaJPanel formulaPanel;
		TablaJPanel tablaPanel;
		BotonesJPanel botonesPanel;
	
	JMenuBar menuBar;
		JMenu fileMenu;
			JMenuItem newMenuItem;
			JMenuItem saveMenuItem;
			JMenuItem loadMenuItem;
		JMenu editMenu;
			JMenuItem deshacerMenuItem;
			JMenuItem rehacerMenuItem;
			
	TablaJPanel.TextFieldCelda celdaActual;
	

	public MainUI() {
		mainFrame=new JFrame();

		mainFrame.setMinimumSize(new Dimension(300,300));
		mainFrame.setPreferredSize(new Dimension(300,300));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		//Paneles
		
		
		formulaPanel=new FormulaJPanel(
				new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						celdaActual.actualizarValor(formulaPanel.recuperarTexto());
						celdaActual.setBackground(tablaPanel.COLOR_POR_DEFECTO);
						celdaActual=null;
						if(tablaPanel.punteroListaCambios!=0) {
							deshacerMenuItem.setEnabled(true);
							rehacerMenuItem.setEnabled(false);
						}
						//System.out.println("ButtonPress1");
						//System.out.println(arg0.toString());

						//System.out.println();
					}
				}, 
				new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						formulaPanel.desabilitarEntrada();
						celdaActual.setBackground(tablaPanel.COLOR_POR_DEFECTO);
						celdaActual=null;
						//System.out.println("ButtonPress2");
						//System.out.println(arg0.toString());
						
						//System.out.println();
					}
		});
		
		tablaPanel=new TablaJPanel(new FocusListener() { //le paso el listener de los textfields

			@Override
			public void focusGained(FocusEvent arg0) {
				//System.out.println(arg0.toString());
				
				//System.out.println((arg0.getOppositeComponent()==null) ? "NULL" : arg0.getOppositeComponent().toString());
				//System.out.println(arg0.toString());
				if(arg0.toString().contains(EVENTO_INVALIDO_0)) {
					if(celdaActual!=null) {
						celdaActual.setBackground(tablaPanel.COLOR_POR_DEFECTO);
					}
					TablaJPanel.TextFieldCelda tfCelda=  (TablaJPanel.TextFieldCelda)arg0.getSource();
					/*System.out.println("Foco ganado "+tfCelda.fila+
							" "+tfCelda.columna
							);*/
					tfCelda.setBackground(tablaPanel.COLOR_EN_SELECCION);
					
					celdaActual=tfCelda;//Establecemos la celda que se esta editando
					formulaPanel.prepararEntradaTexto(celdaActual.getText());
				}
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		}); 
		botonesPanel=new BotonesJPanel(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tablaPanel.calcularFormulas(); //Cambio la hoja del panel con en valor de todas las formulas y los jtextfields
				formulaPanel.desabilitarEntrada();
				if(celdaActual!=null) {
					celdaActual.setBackground(tablaPanel.COLOR_POR_DEFECTO);
				}
				if(tablaPanel.punteroListaCambios!=0) {
					deshacerMenuItem.setEnabled(true);
					rehacerMenuItem.setEnabled(false);
				}
				celdaActual=null;
			}
			
		});
		
		mainPanel.add(formulaPanel);
		mainPanel.add(tablaPanel);
		mainPanel.add(botonesPanel);
		
		mainFrame.setContentPane(mainPanel);
		
		//MenuBar
		menuBar= new JMenuBar();
		fileMenu=new JMenu("Archivo");
		editMenu=new JMenu("Editar");
		
		newMenuItem= new JMenuItem("Nuevo");
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NuevaHojaJDialog nuevaJDialog= new NuevaHojaJDialog(mainFrame);
				if(nuevaJDialog.exito) {
					int filasHoja=nuevaJDialog.filas;
					int columnasHoja=nuevaJDialog.columnas;
					tablaPanel.establecerHoja(filasHoja, columnasHoja);
					saveMenuItem.setEnabled(true);
					deshacerMenuItem.setEnabled(false);
					rehacerMenuItem.setEnabled(false);
					tablaPanel.borrarRegistroCambios();
					mainFrame.pack(); //El listener debe de hacer el pack ya que el jpanel es independiente de otro componente
				}
				
			}
		});
		saveMenuItem= new JMenuItem("Guardar");
		saveMenuItem.setEnabled(false);
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser("/home/luisuni/Escritorio/Hojas de calculo");
				int returnVal = fc.showSaveDialog(mainFrame);
				if(returnVal==JFileChooser.APPROVE_OPTION) {
					try {
						File fichero=fc.getSelectedFile();
						if(fichero.exists()) {
							fichero.delete();
						}
						fichero.createNewFile();
						tablaPanel.hoja.guardarHojaCalculo(fichero);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(mainFrame,
							    "Error al guardar",
							    "Guardar hoja de calculo",
							    JOptionPane.WARNING_MESSAGE);
					}
					
					
				} else if(returnVal==JFileChooser.CANCEL_OPTION) {
					
				} else if(returnVal==JFileChooser.ERROR_OPTION) {
					
				}
				
			}
		});
		loadMenuItem= new JMenuItem("Cargar");
		loadMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser("/home/luisuni/Escritorio/Hojas de calculo");
				
				int returnVal = fc.showOpenDialog(mainFrame);
				if(returnVal==JFileChooser.APPROVE_OPTION) {
					try {
						celdaActual=null;
						formulaPanel.desabilitarEntrada();
						tablaPanel.establecerHojaFichero(fc.getSelectedFile());
						saveMenuItem.setEnabled(true);
						deshacerMenuItem.setEnabled(false);
						rehacerMenuItem.setEnabled(false);
						tablaPanel.borrarRegistroCambios();
						mainFrame.pack();

					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(mainFrame,
							    "El archivo seleccionado no existe",
							    "Cargar hoja de calculo",
							    JOptionPane.WARNING_MESSAGE);
					}
				} else if(returnVal==JFileChooser.CANCEL_OPTION) {
					
				} else if(returnVal==JFileChooser.ERROR_OPTION) {
					
				}
			}
		});
		
			
		fileMenu.add(newMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(loadMenuItem);
		
		deshacerMenuItem= new JMenuItem("Deshacer");
		deshacerMenuItem.setEnabled(false);
		deshacerMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tablaPanel.deshacerCambios();
				if(tablaPanel.poderDeshacer()==false) {
					deshacerMenuItem.setEnabled(false);
				} 
				rehacerMenuItem.setEnabled(true);
			}
			
		});
		editMenu.add(deshacerMenuItem);
		rehacerMenuItem= new JMenuItem("Rehacer");
		rehacerMenuItem.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tablaPanel.rehacerCambios();
				if(tablaPanel.poderRehacer()==false) {
					rehacerMenuItem.setEnabled(false);
				} 
				deshacerMenuItem.setEnabled(true);
				
			}
			
			
		});
		rehacerMenuItem.setEnabled(false);
		editMenu.add(rehacerMenuItem);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		
		mainFrame.setJMenuBar(menuBar); //Anyadir la barra de menu
		
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Deshabilitar que por defecto cierre el jframe
		mainFrame.addWindowListener(new WindowAdapter() { //windowadapter para sobreescribir los metodos que quieres implementar e ignorar el resto
			 @Override
	            public void windowClosing(WindowEvent e) { //Antes de cerrarse el programa pasa por aquí
	                System.out.println("Closed");
	                e.getWindow().dispose();
	                System.exit(0);
	            }
		});
		mainFrame.pack(); //El frame no se redimensiona hasta que hacer pack (si no tiene el tamanyo minimo)
		mainFrame.setVisible(true); //Para que se vea en la pantalla
		mainFrame.setLocationRelativeTo(null); //Para que aparezca en el centro de la pantalla
		
	}
	
	public static void main (String[] args) {
		new MainUI();
	}
	

    public static String numALetras(int i) { //Convierte un numero a una letra
        int quotient, remainder;
        String result = "";
        quotient = i-1;

        while (quotient >= 0)
        {
            remainder = quotient % 26;
            result = (char)(remainder + 65) + result;
            quotient = (int)Math.floor(quotient/26) - 1;
        }
        return result;
    }
    

	
}