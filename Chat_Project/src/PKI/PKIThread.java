package PKI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;

import Message.GsonMessage;

public class PKIThread implements Runnable{
	
	private Socket localSocket;
	private DataInputStream in;
	private DataOutputStream ou;
	private Gson gson = new Gson();
	private PKIClient fatherServer;
	/*public static void connectToDataField() {
		try {
			localSocket =  new Socket("127.0.0.1", localPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	@Override
	public void run() {
		try {
			getMessage();
		} catch (ClassNotFoundException e) {
			System.out.println("Coudln't listen for another message");
		}
	}
	
	public PKIThread(Socket localSocket,PKIClient fatherServer) {
		this.fatherServer = fatherServer;
		this.localSocket = localSocket;
	}
	
	public void outputStream() {
		try {
			ou = new DataOutputStream(localSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getMessage() throws ClassNotFoundException {
		System.out.println("Listening for messages");
		try {
			in = new DataInputStream(localSocket.getInputStream());
		} catch (IOException e1) {
			System.out.println("Lost connection");
		}
		while(true) {
			GsonMessage receivedMessage; 
			try {
				receivedMessage = gson.fromJson(in.readUTF(), GsonMessage.class);
				System.out.println("Received message " + receivedMessage.getMessage() + " CC " + receivedMessage.getContentCode());
				fatherServer.processObject(receivedMessage, in, ou);				
			} catch (IOException e) {
				System.out.println("Error reading message");
			}
		}
	}

}
