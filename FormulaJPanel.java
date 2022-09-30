import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

class FormulaJPanel extends JPanel {
	JTextField formulaTextField;
	JButton aceptarButton;
	JButton cancelarButton;
	
	
	
	public FormulaJPanel(ActionListener listenerAceptar,ActionListener listenerCancelar) {
		super();
		//Cambiamos el layout porque por defecto es FlowLayout, un layout que no utiliza maximos y minimos
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS)); //El axis es como se ordena, si en horizontal o en vertical

		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		formulaTextField = new JTextField();
		aceptarButton = new JButton("Aceptar");
		aceptarButton.addActionListener(listenerAceptar);
		cancelarButton = new JButton("Cancelar");
		cancelarButton.addActionListener(listenerCancelar);
		formulaTextField.setEnabled(false);
		aceptarButton.setEnabled(false);
		cancelarButton.setEnabled(false);
		
		//Este peferredsize ignora cualquier ajuste automatico a partir del contenido (el contenido de un label,...)
		//formulaTextField.setPreferredSize(new Dimension(50,50));
		//Si el panel al que le asigna estos componentes maneja maximos y minimos se usan estos
		//en vez del peferredsize
		//formulaTextField.setMinimumSize(new Dimension(20,20));
		//formulaTextField.setMaximumSize(new Dimension(80,80));
		
		//Al cambiar el tamaño del panel, se anula el ajuste automatico de este a partir del
		//peferredsize de la suma de los preferredsize de sus componentes asignados
		//this.setPreferredSize(new Dimension(100,100));
		//Si el panel padre tiene un layout que maneje maximos y minimos se utilizan estos valores en 
		//vez del peferredsize
		//this.setMinimumSize(new Dimension(30,30));
		//this.setMaximumSize(new Dimension(80,80));
			
			
		this.setMinimumSize(new Dimension(1,20));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
		this.add(formulaTextField);
		this.add(aceptarButton);
		this.add(cancelarButton);
	}
	
	//Devuelvo el texto que hay dentro del textfield
	public String recuperarTexto() {
		String texto =formulaTextField.getText();
		formulaTextField.setText("");
		this.desabilitarEntrada();
		return texto;
	}
	
	//Cuando cliqueamos en una celda, pongo el texto que aparece en el textfield y habilito los botones.
	public void prepararEntradaTexto(String texto) {
		formulaTextField.setText(texto);
		formulaTextField.setEnabled(true);
		aceptarButton.setEnabled(true);
		cancelarButton.setEnabled(true);
	}
	
	//Borro lo que hay en el textfield de la formula, y pongo los botones a sisable
	public void desabilitarEntrada() {
		formulaTextField.setText("");
		formulaTextField.setEnabled(false);
		aceptarButton.setEnabled(false);
		cancelarButton.setEnabled(false);
	}
}