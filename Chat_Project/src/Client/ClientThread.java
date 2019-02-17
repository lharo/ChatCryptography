package Client;

import java.awt.Window;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class ClientThread extends Thread{
	private Socket s;
	private DataInputStream in;
	private boolean end;
	private String message,name;
	ClientMainChat fatherWindow;
	
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
				message=in.readUTF();
				System.out.println(message);
				SwingUtilities.invokeLater(new Runnable() {
				    public void run() {
				    	fatherWindow.getChatField().setText( fatherWindow.getChatField().getText() + "\n" + message);
				    }
				  });
				if(message.equalsIgnoreCase("- "+name+" disconected")){
					end=true;					
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
