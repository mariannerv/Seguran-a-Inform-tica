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
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class assinar {

	public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, InvalidKeyException, SignatureException {
		// TODO Auto-generated method stub
		
		
		
        
        //decifrar a chave lida do a.key com a chave privada da mariana que está no keystore
        FileInputStream kfile1 = new FileInputStream("/home/aluno-di/area-de-aluno/eclipse-workspace/tp03/src/tp03/keystore.mariana");
        KeyStore kstore = KeyStore.getInstance("PKCS12");
        kstore.load(kfile1, "fc55945".toCharArray());
        PrivateKey myPrivateKey = (PrivateKey)kstore.getKey("mariana", "fc55945".toCharArray());
        
        Signature s = Signature.getInstance("SHA256withRSA");
        s.initSign(myPrivateKey);
        
        FileInputStream fis;
        FileOutputStream fos;

        
        fis = new FileInputStream("/home/aluno-di/area-de-aluno/eclipse-workspace/tp03/src/tp03/decrypted.txt"); //ler o conteudo
        

        byte[] b = new byte[1024];
        int i = fis.read(b);
        while (i != -1) {
            s.update(b, 0, i);
            i = fis.read(b);
        }
        
        byte [] assinatura = s.sign();
        fos = new FileOutputStream("/home/aluno-di/area-de-aluno/eclipse-workspace/tp03/src/tp03/decrypted.txt.assinatura"); //ficheiro cifrado
        System.out.println("ficheiro assinado com sucesso");
        fos.write(assinatura);
        fos.close();
        fis.close();
      
	}

}
