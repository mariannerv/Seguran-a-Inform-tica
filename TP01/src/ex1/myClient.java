package ex1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class myClient {
	//ligar ao porto
	//escrever uma string correspondente ao user e a passwd
	

	public static void main(String[] args) throws IOException, IOException, Exception {
		//Socket socket = new Socket("127.0.0.1", 23450);  

		System.setProperty("javax.net.ssl.trustStore", "cliente.truststore");
		System.setProperty("javax.net.ssl.truststorePassword", "123456");
		
		SocketFactory sf = SSLSocketFactory.getDefault();
		Socket socket = sf.createSocket("127.0.0.1", 23456);
		
		
		ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
		
		
		outStream.writeObject("aa");
		outStream.writeObject("bb");
		
		
		
		Boolean b = (Boolean) inStream.readObject();
		
		//Exercicio 2 
		String filename = "cripto.pdf";
		
		File myFile = new File(filename); //ficheiro a enviar ao servidor
		
		Long dimensao = (Long) myFile.length();
		
		BufferedInputStream myFileB = new BufferedInputStream(new FileInputStream(filename));
		
		byte [] buffer = new byte[1024];
		
		outStream.writeObject(dimensao);
		
		int x;
		while ((x = myFileB.read(buffer, 0, 1024)) > 0) {
		    outStream.write(buffer, 0, x);
		}
		
		outStream.close();
		myFileB.close();
		

		outStream.close();
		inStream.close();
		
		

		System.out.println(b);
		
		socket.close();
	}
}
