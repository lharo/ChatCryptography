package PKI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import Message.GsonMessage;
import Message.PKIMessage;

public class PKIClient {
	
	
	private static int localPort = 5000;
	private static Gson gson = new Gson();
	private static List<PKIConnection> usersOnPKI = new ArrayList<PKIConnection>();
	private ServerSocket serverSocket; 
	
	public void keepListening() {
		while(true) {
			try {
				Socket dummySocket = this.serverSocket.accept();
				System.out.println("New connection found at port: " + dummySocket.getPort());
				new Thread(new PKIThread(dummySocket, this)).start();;
				
			} catch (IOException e) {
				System.out.println("Coudln't accept new connection");
				e.printStackTrace();
			}
		}
	}
	
	public void getServerSocket() {
		//for(int port = Min; port <= Max; port++) {
			try {
				this.serverSocket = new ServerSocket(localPort);
				System.out.println("Connection at port: " + localPort);
			} catch (IOException e) {
				System.out.println("Not possible to connect at port: " + localPort);
			}
		//}
	}

	public synchronized void forwardMessage(GsonMessage msg, DataOutputStream ou) {
		try {
			ou.writeUTF(gson.toJson(msg));
			System.out.println("A message was sent with the msg being " + gson.toJson(msg));
		} catch (IOException e) {
			System.out.println("Could not send message");
			e.printStackTrace();
		}
	}

	public void processObject(GsonMessage msg, DataInputStream in, DataOutputStream ou) {
		
		System.out.println("Received message " + msg.getMessage() + "CC " + msg.getContentCode());
		PKIMessage pkiRequest = gson.fromJson(msg.getMessage(), PKIMessage.class);					
		switch(msg.getContentCode()) {
			case 2:{	//Add public key
				if(!getPublicKeyStatusFromClientName(pkiRequest.getMessage())) {
					String path = pkiRequest.getPath();
					saveUserPublicKey(pkiRequest, in, ou);
					GsonMessage genericResponse = new GsonMessage();
					genericResponse.setContentCode(2);
					PKIMessage pkiResponse = new PKIMessage();
					pkiResponse.setContentCode(1);
					pkiResponse.setMessage("Llave registrada exitosamente");
					pkiResponse.setPath(path);
					genericResponse.setMessage(gson.toJson(pkiResponse));
					forwardMessage(genericResponse, ou);
				}
				else {
					System.out.println("Updating " + pkiRequest.getMessage() +  " public key on the pki");
					String path = pkiRequest.getPath();
					updateUserPublicKey(pkiRequest, in, ou);
					GsonMessage genericResponse = new GsonMessage();
					genericResponse.setContentCode(2);
					PKIMessage pkiResponse = new PKIMessage();
					pkiResponse.setContentCode(2);
					pkiResponse.setMessage("Llave actualizada exitosamente");
					pkiResponse.setPath(path);
					genericResponse.setMessage(gson.toJson(pkiResponse));
					forwardMessage(genericResponse, ou);					
				}
				break;				
			}
			//Public Key Request
			case 3:{
				if(getPublicKeyStatusFromClientName(pkiRequest.getMessage())) {
					System.out.println("Sending " + pkiRequest.getSender() +  " public key requested");
					String path = getKeyFromClientName(pkiRequest);
					GsonMessage genericResponse = new GsonMessage();
					genericResponse.setContentCode(2);
					PKIMessage pkiResponse = new PKIMessage();
					pkiResponse.setContentCode(3);
					pkiResponse.setMessage("Llave publica de: " + pkiRequest.getMessage());
					pkiResponse.setPath(path);
					genericResponse.setMessage(gson.toJson(pkiResponse));
					forwardMessage(genericResponse, ou);								
				}else {
					System.out.println("Sending error to " + pkiRequest.getSender() +  " on public key requested");
					GsonMessage genericResponse = new GsonMessage();
					genericResponse.setContentCode(2);
					PKIMessage pkiResponse = new PKIMessage();
					pkiResponse.setContentCode(4);
					pkiResponse.setMessage("No se encontro la llave publica de: " + pkiRequest.getMessage());
					pkiResponse.setPath("");
					genericResponse.setMessage(gson.toJson(pkiResponse));
					forwardMessage(genericResponse, ou);					
				}
				break;
			}
		}
	}
	
	private void updateUserPublicKey(PKIMessage pkiRequest, DataInputStream in, DataOutputStream ou) {
		// TODO Auto-generated method stub
		PKIConnection con = new PKIConnection();
		con.setIn(in);
		con.setOu(ou);
		con.setName(pkiRequest.getMessage());
		con.setPath(pkiRequest.getPath());
		updateUsersOnPKI(con);
		//usersOnPKI.update(con);
	}

	private void saveUserPublicKey(PKIMessage pkiRequest, DataInputStream in, DataOutputStream ou) {
		PKIConnection con = new PKIConnection();
		con.setIn(in);
		con.setOu(ou);
		con.setName(pkiRequest.getMessage());
		con.setPath(pkiRequest.getPath());
		usersOnPKI.add(con);
	}

	private boolean getPublicKeyStatusFromClientName(String message) {
		for(PKIConnection user : usersOnPKI) {
			if(user.getName().equals(message)) return true;
		}
		return false;
	}

	private String getKeyFromClientName(PKIMessage pkiRequest) {
		for(PKIConnection user : usersOnPKI) {
			if(user.getName().equals(pkiRequest.getMessage())) return user.getPath();
		}
		return null;
	}
	
	private void updateUsersOnPKI(PKIConnection con) {
		for(PKIConnection user : usersOnPKI) {
			if(user.getName().equals(con.getName())) usersOnPKI.remove(user);
		}
		usersOnPKI.add(con);
	}
}
