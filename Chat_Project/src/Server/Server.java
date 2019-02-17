package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Shared.Users;

public class Server {
	private static ServerSocket ss;
	private static boolean salir;
	private static int cont;
	private static Users list;
	
	public static void main(String[] args) {
		try{
			cont=0;
			list = new Users();
			salir=false;
			ss=new ServerSocket(12345);
			System.out.println("Server");
			while(!salir){
				Socket s=ss.accept();
				list.addUser(cont, s);
				ServerThread st=new ServerThread(s,list,cont);
				st.start();				
				cont++;
			}
		}catch(IOException io){
			System.out.println("Error in server");
		}
	}

}
