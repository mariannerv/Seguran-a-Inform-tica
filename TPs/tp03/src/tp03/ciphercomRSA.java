package tp03;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class ciphercomRSA {
	 public static void main(String[] args) throws Exception {
		 
		 
		 FileInputStream kfile = new FileInputStream("keystore.mariana");  //keystore
		 KeyStore kstore = KeyStore.getInstance("PKCS12");
		 kstore.load(kfile, "fc55945".toCharArray());           //password
		 Certificate cert = kstore.getCertificate("mariana");  //alias do utilizador
		 
         KeyGenerator kg = KeyGenerator.getInstance("AES");
         kg.init(128);
         SecretKey key = kg.generateKey();
		 
         Cipher c = Cipher.getInstance("RSA");
         c.init(Cipher.WRAP_MODE, cert);
		
         byte[] keyEncoded = c.wrap(key);
         
         FileOutputStream kos = new FileOutputStream("a.key");
         kos.write(keyEncoded);
         kos.close();
	}
}
