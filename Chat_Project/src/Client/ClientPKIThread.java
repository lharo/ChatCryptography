package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;

import Message.GsonMessage;
import Message.PKIMessage;

public class ClientPKIThread extends Thread{

	private ClientMainChat fatherClient;
	private Socket pkiSocket;
	private Boolean end;
	private DataInputStream in;
	private Gson gson = new Gson();
	private String lastKnownPublicKeyLocation;
	
	public ClientPKIThread(Socket pkiSocket, ClientMainChat fatherClient) {
		this.pkiSocket = pkiSocket;
		this.fatherClient = fatherClient;
	}
	
	public void run(){
		try{
			end=false;
			in=new DataInputStream(pkiSocket.getInputStream());
			while(!end){
				String msg = in.readUTF();
				System.out.println("Received message " + msg);
				GsonMessage gMessage = gson.fromJson(msg, GsonMessage.class);
				String message = gMessage.getMessage();
				PKIMessage pkiMessage = gson.fromJson(message, PKIMessage.class);
				System.out.println("Father " + message);
				switch(pkiMessage.getContentCode()) {
					//Clients Public key was successfully registered on the PKI
					case 1:
					case 2:{
						System.out.println(pkiMessage.getMessage());
						break;
					}
					case 3:{
						setLastKnownPublicKeyLocation(pkiMessage.getPath());
						System.out.println(pkiMessage.getMessage());
						break;
					}
					case 4:{
						setLastKnownPublicKeyLocation(null);
						System.out.println(pkiMessage.getMessage());
						break;
					}
				}
			}
			pkiSocket.close();
		}catch(IOException io){
			System.out.println("Error client thread");
			io.printStackTrace();
			end=true;
		}
	}

	public String getLastKnownPublicKeyLocation() {
		return lastKnownPublicKeyLocation;
	}

	public void setLastKnownPublicKeyLocation(String lastKnownPublicKeyLocation) {
		this.lastKnownPublicKeyLocation = lastKnownPublicKeyLocation;
	}

}
