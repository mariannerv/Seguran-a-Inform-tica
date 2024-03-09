package exercicioSemConfusions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;




public class serverEncripter{
	
	//Inicializar o servidor

	public static void main(String[] args) {
		System.out.println("servidor: main");
		serverEncripter server = new serverEncripter();
		server.startServer();
	}
	
	public void startServer (){
		ServerSocket sSoc = null;
	    
		try {
			sSoc = new ServerSocket(23456);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	     
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

	class ServerThread extends Thread{
	    private Socket socket = null;
	
	    ServerThread(Socket inSoc) {
	        socket = inSoc;
	        System.out.println("thread do server para cada cliente");
	    }
	    
	    public void run() {
	        try {
	        	//Ligar a socket 
	            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
	            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
	
	            String user = null;
	            String passwd = null;
	
	            try {
	                user = (String) inStream.readObject();
	                passwd = (String) inStream.readObject();
	                System.out.println("thread: depois de receber a password e o user");
	                
	            } catch (ClassNotFoundException e1) {
	                e1.printStackTrace();
	            }
	            if (user.length() != 0) {
	                // Authentication successful
	                outStream.writeObject((Boolean) true);
	            }
	            
	          //gerar uma chave aleatoria para utilizar com o AES, cifrar o ficheiro .txt
	            KeyGenerator kg = KeyGenerator.getInstance("AES");
	            kg.init(128);
	            SecretKey key = kg.generateKey();
	
	            Cipher c = Cipher.getInstance("AES");
	            c.init(Cipher.ENCRYPT_MODE, key);
	
	            FileInputStream fis;
	            FileOutputStream fos;
	            CipherOutputStream cos;
	
	            fis = new FileInputStream("a.txt"); //ler o conteudo
	            fos = new FileOutputStream("a.cif"); //ficheiro cifrado
	
	            cos = new CipherOutputStream(fos, c); //ler o conteudo cifrado
	            byte[] b = new byte[1024];
	            int i = fis.read(b);
	            while (i != -1) {
	                cos.write(b, 0, i);
	                i = fis.read(b);
	            }
	            cos.close();
	            fis.close();
	            fos.close();
	            
                // Sending the encrypted file to the client
                FileInputStream encryptedFileStream = new FileInputStream("a.cif");
                byte[] encryptedFileBytes = encryptedFileStream.readAllBytes();
                outStream.writeObject(encryptedFileBytes);
                encryptedFileStream.close();
                System.out.println("Ficheiro encriptado enviado com sucesso");

                // Sending the key to the client
                outStream.writeObject(key.getEncoded());
                System.out.println("chave enviada com sucesso");
	            
	        } catch (IOException e) {
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				System.out.println("Abort, deu merda");
				e.printStackTrace();
			} finally {
	        	System.out.println("Acabei, vou dormir");
	        }
	    }
	}
}