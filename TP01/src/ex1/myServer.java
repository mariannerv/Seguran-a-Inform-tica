/***************************************************************************
*   Seguranca Informatica
*
*
***************************************************************************/
package ex1;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;


public class myServer{

	public static void main(String[] args) throws IOException {
		System.out.println("servidor: main");
		myServer server = new myServer();
		server.startServer();
	}

	public void startServer () throws IOException{
		ServerSocketFactory ssf = SSLServerSocketFactory.getDefault( );
		ServerSocket ss = ssf.createServerSocket(9096);
		while (true) {
			new myServer(ss.accept()).start( ); // uma thread por ligação
		}
		System.setProperty("javax.net.ssl.keyStore", "keystore.servidor");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
         
		while(true) {
			try {
				Socket inSoc = sSoc.accept();
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
		    }
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		}
		//sSoc.close();
	}


	//Threads utilizadas para comunicacao com os clientes
	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			System.out.println("thread do server para cada cliente");
		}
 
		public void run(){
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

				String user = null;
				String passwd = null;
			
				try {
					user = (String)inStream.readObject();
					passwd = (String)inStream.readObject();
					System.out.println("thread: depois de receber a password e o user");
				}catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
 			
				//TODO: refazer
				//este codigo apenas exemplifica a comunicacao entre o cliente e o servidor
				//nao faz qualquer tipo de autenticacao
				if (user.length() != 0){
					outStream.writeObject( (Boolean) true);
				}
				else {
					outStream.writeObject( (Boolean) false);
				}
				
				//exercicio 2 
				System.out.println("inicio de ficheiro");
				FileOutputStream outFileStream = new FileOutputStream("teste.pdf");
				//n vamos receber o ficheiro inteiro, vamos recber aos bocados
				
				
				
				Long fileSize = (Long) inStream.readObject();
				byte[] buffer = new byte[1024];
				int bytesRead;
				
				while (fileSize > 0) {
				    int bytesToRead = (int) Math.min(buffer.length, fileSize);
				    bytesRead = inStream.read(buffer, 0, bytesToRead);
				    if (bytesRead == -1) {
				        // Handle end of stream
				        break;
				    }
				    outFileStream.write(buffer, 0, bytesRead);
				    fileSize -= bytesRead;
				}
				System.out.println("fim do ficheiro");
				
				
				outFileStream.close();
				
				outStream.close();
				inStream.close();
 			
				socket.close();

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}