package Client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientMainChat {

	private JFrame frame;
	private JTextField messageField;
	private JTextArea chatField;
	private static DataOutputStream ou;
	private static BufferedReader in;
	private static Socket s;
	private static String message, username;
	private static boolean end;
	private static ClientThread cl;

	public void OpenChat(String Username) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					username = Username;
					ClientMainChat window = new ClientMainChat();
					window.frame.setVisible(true);
					startThread();
					cl=new ClientThread(s,username, window);
					cl.start();
					try {
						ou.writeUTF(username);
					} catch (IOException e) {}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientMainChat() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 567, 418);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		messageField = new JTextField();
		messageField.setBounds(136, 348, 312, 20);
		frame.getContentPane().add(messageField);
		messageField.setColumns(10);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message=messageField.getText();				
				messageField.setText("");
				try {
					ou.writeUTF(message);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(452, 347, 50, 23);
		frame.getContentPane().add(btnNewButton);
		
		chatField = new JTextArea();
		chatField.setBounds(136, 11, 366, 326);
		frame.getContentPane().add(chatField);
		chatField.setColumns(10);
	}
	private static void startThread(){
		try{
			s=new Socket("localhost",12345);
			ou=new DataOutputStream(s.getOutputStream());
			in=new BufferedReader(new InputStreamReader(System.in));
			end=false;			
		}catch(IOException io){
			io.printStackTrace();
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextField getMessageField() {
		return messageField;
	}

	public void setMessageField(JTextField messageField) {
		this.messageField = messageField;
	}

	public JTextArea getChatField() {
		return chatField;
	}

	public void setChatField(JTextArea chatField) {
		this.chatField = chatField;
	}

	public static DataOutputStream getOu() {
		return ou;
	}

	public static void setOu(DataOutputStream ou) {
		ClientMainChat.ou = ou;
	}

	public static BufferedReader getIn() {
		return in;
	}

	public static void setIn(BufferedReader in) {
		ClientMainChat.in = in;
	}

	public static Socket getS() {
		return s;
	}

	public static void setS(Socket s) {
		ClientMainChat.s = s;
	}

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		ClientMainChat.message = message;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		ClientMainChat.username = username;
	}

	public static boolean isEnd() {
		return end;
	}

	public static void setEnd(boolean end) {
		ClientMainChat.end = end;
	}

	public static ClientThread getCl() {
		return cl;
	}

	public static void setCl(ClientThread cl) {
		ClientMainChat.cl = cl;
	}
}
