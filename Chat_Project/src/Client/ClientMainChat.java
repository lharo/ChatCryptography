package Client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import Cryptography.AsymetricEncryption;
import Cryptography.SymetricEncription;
import Shared.FileHandler;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

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
	private static Boolean isSymmetric = true;
	private static PublicKey pubKey = null;
	private static PrivateKey privKey = null;	
	private static SymetricEncription se = new SymetricEncription();
	private static AsymetricEncryption ae = new AsymetricEncryption();
	private static KeyPair kp;
	private static String publicKeySymmetric;

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
		frame.setBounds(100, 100, 598, 436);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		messageField = new JTextField();
		messageField.setBounds(136, 348, 334, 20);
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
		btnNewButton.setBounds(480, 347, 63, 23);
		frame.getContentPane().add(btnNewButton);

		chatField = new JTextArea();
		chatField.setBounds(136, 11, 407, 326);
		frame.getContentPane().add(chatField);
		chatField.setColumns(10);

		JRadioButton rdbtnSymmetric = new JRadioButton("Symmetric");
		rdbtnSymmetric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Not Ass");
				setIsSymmetric(true);
			}
		});
		rdbtnSymmetric.setBounds(21, 105, 109, 23);
		frame.getContentPane().add(rdbtnSymmetric);

		JRadioButton rdbtnAsymmetric = new JRadioButton("Asymmetric");
		rdbtnAsymmetric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Ass");
				setIsSymmetric(false);
			}
		});
		rdbtnAsymmetric.setBounds(21, 162, 109, 23);
		frame.getContentPane().add(rdbtnAsymmetric);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnSymmetric);
		group.add(rdbtnAsymmetric);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnKeys = new JMenu("Keys");
		menuBar.add(mnKeys);

		JMenuItem mntmCreateKeys = new JMenuItem("Create Keys");
		mntmCreateKeys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser(); 
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select Path To Save Keys");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
					System.out.println("getCurrentDirectory(): " 
							+  chooser.getCurrentDirectory());
					System.out.println("getSelectedFile() : " 
							+  chooser.getSelectedFile());
				}
				else {
					System.out.println("No Selection ");
				}
				try {
					ae.buildKeyPair(String.valueOf(chooser.getSelectedFile()));
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnKeys.add(mntmCreateKeys);

		JMenuItem mntmLoadPrivateKey = new JMenuItem("Load APrivate Key");
		mntmLoadPrivateKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileHandler privateKey = openFile();
				try {
					setPrivKey(ae.loadPrivateKeyPair(privateKey.getPath(), "RSA"));
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(getPubKey() != null) {
					setKp(ae.loadKeyPair(getPubKey(), getPrivKey()));
				}

				//setPrivateKeyPath(privateKey.getPath());
			}
		});
		mnKeys.add(mntmLoadPrivateKey);

		JMenuItem mntmLoadPublicKey = new JMenuItem("Load APublic Key");
		mntmLoadPublicKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileHandler publicKey = openFile();
				try {
					setPubKey(ae.loadPublicKeyPair(publicKey.getPath(), "RSA"));
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//setPublicKeyPath(publicKey.getPath());
				if(getPrivKey() != null) {
					setKp(ae.loadKeyPair(getPubKey(), getPrivKey()));
				}
			}
		});
		mnKeys.add(mntmLoadPublicKey);

		JMenuItem mntmLoadSpublicKey = new JMenuItem("Load SPublic Key");
		mntmLoadSpublicKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPublicKeySymmetric(dialoguePaneSymetricKey());
			}
		});
		mnKeys.add(mntmLoadSpublicKey);

		JMenuItem mntmRegisterApublicKey = new JMenuItem("Register APublic Key");
		mntmRegisterApublicKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		mnKeys.add(mntmRegisterApublicKey);

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

	public static Boolean getIsSymmetric() {
		return isSymmetric;
	}

	public static void setIsSymmetric(Boolean isSymmetric) {
		ClientMainChat.isSymmetric = isSymmetric;
	}

	public static PublicKey getPubKey() {
		return pubKey;
	}

	public static void setPubKey(PublicKey pubKey) {
		ClientMainChat.pubKey = pubKey;
	}

	public static PrivateKey getPrivKey() {
		return privKey;
	}

	public static void setPrivKey(PrivateKey privKey) {
		ClientMainChat.privKey = privKey;
	}

	public static KeyPair getKp() {
		return kp;
	}

	public static void setKp(KeyPair kp) {
		ClientMainChat.kp = kp;
	}

	public static String getPublicKeySymmetric() {
		return publicKeySymmetric;
	}

	public static void setPublicKeySymmetric(String publicKeySymmetric) {
		ClientMainChat.publicKeySymmetric = publicKeySymmetric;
	}

	public FileHandler openFile() {
		FileHandler result = new FileHandler();
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.KEY", "key");

		fc.setFileFilter(filter);
		Integer selected = fc.showOpenDialog(frame);
		if(selected == JFileChooser.APPROVE_OPTION){

			File fileChosen = fc.getSelectedFile();

			result.setPath(fileChosen.getAbsolutePath());

			try(FileReader fr = new FileReader(fileChosen)){
				String text = "";
				int value = fr.read();
				while(value != -1){
					text=text + (char)value;
					value=fr.read();
				}
				result.setContent(text);
				result.setIsValid(true);
				//textArea.setText(cadena);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else result = null;
		return result;
	}
	private String dialoguePaneSymetricKey() {

		Boolean isNotFiveCharactersLong = false;
		String name = "";
		while(!isNotFiveCharactersLong) {
			JFrame frame = new JFrame("Symetric Key Gen");
			name = JOptionPane.showInputDialog(frame, "At least 5 Character Symmetric Key");
			System.out.printf("The user's symetric key is '%s'.\n", name);
			System.out.println("And the size of the key was: " + name.length());
			if(name.length() > 5) isNotFiveCharactersLong = true;
		}
		return name;
	}

}
