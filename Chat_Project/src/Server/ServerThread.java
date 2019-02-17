package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import Shared.Users;

public class ServerThread extends Thread {
	private Socket s;
	private int id;
	private DataInputStream in;
	private DataOutputStream ou;
	private Users list;
	private boolean exit;
	private String message,name;
	
	public ServerThread(Socket s,Users list,int id){
		this.s=s;
		this.id=id;
		this.list=list;
	}
	
	public ServerThread(){}
	
	public void run(){
		try{
			in=new DataInputStream(s.getInputStream());
			ou=new DataOutputStream(s.getOutputStream());
			exit=false;//recive exit
			name=in.readUTF();//recive name Ok
			sendMessage(name+" connected");
		}catch(IOException io1){
			System.out.println("Error Server Thread io1");
		}
		while(!exit){
			try{
				message=in.readUTF();
				if(message.equalsIgnoreCase("exit")){					
					exit=true;
					sendMessage("- "+name+" disconected");//recive message
					removeClient();
				}else{
					sendMessage("- "+name+": "+message);//recive message
				}
				
			}catch(IOException io2){
				System.out.println("Error Server Thread");
				io2.printStackTrace();
			}
		}
	}
	
	private void sendMessage(String message){
		try{	
			@SuppressWarnings("rawtypes")
			Iterator it=list.getUsers().entrySet().iterator();
			while(it.hasNext()){
				@SuppressWarnings("rawtypes")
				Map.Entry pair = (Map.Entry)it.next();
				ou= new DataOutputStream(((Socket) pair.getValue()).getOutputStream());
				ou.writeUTF(message);
			}
		}catch(IOException io){
			System.out.println("Error send menssage");
		}
	}
	
	private void removeClient(){
		list.removeUser(id);
	}
}
