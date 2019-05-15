package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;

import Message.GsonMessage;

public class ClientThread extends Thread{
	private Socket s;
	private DataInputStream in;
	private boolean end;
	private String message,name;
	ClientMainChat fatherWindow;
	private Gson gson = new Gson();
	
	public ClientThread(Socket s,String name, ClientMainChat window){
		this.s=s;
		this.name=name;
		this.fatherWindow = window;
	}
	
	public ClientThread(){
		
	}
	
	public void run(){
		try{
			end=false;
			in=new DataInputStream(s.getInputStream());
			while(!end){
				String msg = in.readUTF();
				System.out.println("Received message " + msg);
				GsonMessage gMessage = gson.fromJson(msg, GsonMessage.class);
				message = gMessage.getMessage();
				System.out.println("Father " + message);
				if(gMessage.getContentCode() == 1) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							fatherWindow.getChatField().setText( fatherWindow.getChatField().getText() + "\n" + message);
						}
					});
					if(message.equalsIgnoreCase("- "+name+" disconected")){
						end=true;					
					}
				}
			}
			s.close();
		}catch(IOException io){
			System.out.println("Error client thread");
			io.printStackTrace();
			end=true;
		}
	}
}
