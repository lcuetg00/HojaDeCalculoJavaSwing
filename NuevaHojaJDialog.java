import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class NuevaHojaJDialog extends JDialog {
	boolean exito;
	int filas;
	int columnas;
	public NuevaHojaJDialog(JFrame frame) {
		
		super(frame,"Nueva hoja de calculo",true);
		JPanel panelPrincipal = new JPanel();
		JPanel filasPanel = new JPanel();
		JPanel columnasPanel = new JPanel();
		JPanel botonesPanel = new JPanel();
		JLabel filasLabel = new JLabel("Numero Filas:");
		JLabel columnasLabel = new JLabel("Numero  Columnas:");
		JTextField filasTextField = new JTextField();

		JTextField columnasTextField = new JTextField();
		
		filasTextField.setPreferredSize(new Dimension(80,20));
		columnasTextField.setPreferredSize(new Dimension(80,20));
		JButton aceptarButton = new JButton("Aceptar");
		ActionListener a =new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					try {	
						filas = Integer.parseInt(filasTextField.getText());
					} catch (Exception e) {
						throw new NumberFormatException("Las filas no pueden contener caracteres que no sean numeros");
					}
					try {	
						columnas = Integer.parseInt(columnasTextField.getText());
					} catch (Exception e) {
						throw new NumberFormatException("Las columnas no pueden contener caracteres que no sean numeros");
					}
					if(filas<=0) {
						throw new NumberFormatException("El numero de filas debe ser mayores que 0");
					}
					if(filas>999) {
						throw new NumberFormatException("El numero de filas no debe ser mayor que 999");
					}
					if(columnas<=0) {
						throw new NumberFormatException("El numero de columnas debe ser mayores que 0");
					}
					if(columnas>17576) {
						throw new NumberFormatException("El numero de columnas no debe ser mayor que 17576 (ZZZ)");
					}
						
					exito=true;
					

					
					dispose();
				} catch(Exception e) {
					JOptionPane.showMessageDialog(frame,
						    e.getMessage(),
						    "Nueva hoja de calculo",
						    JOptionPane.WARNING_MESSAGE);
					
				}
			}
		};
		
		aceptarButton.addActionListener(a);
		JButton cancelarButton = new JButton("Cancelar");
		cancelarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose(); //dispose cierra la ventana
				
			}
		});
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		filasPanel.add(filasLabel);
		filasPanel.add(filasTextField);
		columnasPanel.add(columnasLabel);
		columnasPanel.add(columnasTextField);
		botonesPanel.add(aceptarButton);
		botonesPanel.add(cancelarButton);
		panelPrincipal.add(filasPanel);
		panelPrincipal.add(columnasPanel);
		panelPrincipal.add(botonesPanel);
		this.setContentPane(panelPrincipal);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(frame);
		this.setVisible(true);
		
		
	}
}