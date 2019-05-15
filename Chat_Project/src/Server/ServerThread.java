package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;

import Message.GsonMessage;
import Shared.Users;

public class ServerThread extends Thread {
	private Socket s;
	private int id;
	private DataInputStream in;
	private DataOutputStream ou;
	private Users list;
	private boolean exit;
	private String message,name;
	private Gson gson = new Gson();
	
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
			String msg = in.readUTF();//recive name Ok
			System.out.println("Received first message in thread " + msg);
			GsonMessage gMessage = gson.fromJson(msg, GsonMessage.class);
			message = gMessage.getMessage();
			name = message;
			gMessage.setMessage(message+" connected");
			System.out.println("Responding to said first message " + gson.toJson(gMessage));
			sendMessage(gson.toJson(gMessage));
		}catch(IOException io1){
			System.out.println("Error Server Thread io1");
		}
		while(!exit){
			try{
				String msg = in.readUTF();
				System.out.println("Received message " + msg);
				GsonMessage gMessage = gson.fromJson(msg, GsonMessage.class);
				message = gMessage.getMessage();
				GsonMessage response = new GsonMessage();
				switch(gMessage.getContentCode()) {
					case 1:{
						if(message.equalsIgnoreCase("exit")){					
							exit=true;
							response.setContentCode(1);
							response.setMessage("- "+name+" disconected");
							System.out.println("Response to message " + gson.toJson(response));
							sendMessage(gson.toJson(response));//recive message
							removeClient();
						}else{
							response.setContentCode(1);
							response.setMessage("- "+name+": "+message);
							System.out.println("Response to message " + gson.toJson(response));
							sendMessage(gson.toJson(response));//recive message
						}

						break;
					}
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
