import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

class BotonesJPanel extends JPanel {
	JButton calcularButton;
	
	public BotonesJPanel(ActionListener calcularListener) {
		super();
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setMinimumSize(new Dimension(1,20));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
		calcularButton= new JButton("Calcular");
		this.add(calcularButton);
		calcularButton.addActionListener(calcularListener);
	}
}