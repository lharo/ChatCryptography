package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientLoginUI {

	private JFrame frame;
	private JTextField textField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLoginUI window = new ClientLoginUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientLoginUI() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 298, 215);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(25, 72, 233, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblBienvenidoEligeTu = new JLabel("Bienvenido, Elige tu nombre de usuario");
		lblBienvenidoEligeTu.setBounds(25, 36, 223, 14);
		frame.getContentPane().add(lblBienvenidoEligeTu);
		
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().equals("")) {
					ClientMainChat cChat = new ClientMainChat();
					cChat.OpenChat(textField.getText());	
					frame.dispose();
				}else {
					JOptionPane.showMessageDialog(frame, "Elige un nombre de usuario");
				}
			}
		});
		btnEntrar.setBounds(169, 123, 89, 23);
		frame.getContentPane().add(btnEntrar);
	}
}
