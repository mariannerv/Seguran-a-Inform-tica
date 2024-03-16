package tp03;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class VerificarAssinatura {
	public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeyException, SignatureException {
		
		byte[] assinaturaOriginal = new byte[256];
		FileInputStream kfile = new FileInputStream("/home/aluno-di/area-de-aluno/eclipse-workspace/tp03/src/tp03/decrypted.txt.assinatura");
		kfile.read(assinaturaOriginal);
		kfile.close();
		
        FileInputStream kfile1 = new FileInputStream("/home/aluno-di/area-de-aluno/eclipse-workspace/tp03/src/tp03/keystore.mariana");
        KeyStore kstore = KeyStore.getInstance("PKCS12");
        kstore.load(kfile1, "fc55945".toCharArray());
        
        Certificate cert = kstore.getCertificate("mariana");
        
        Signature s = Signature.getInstance("SHA256withRSA");
        s.initVerify(cert);
        
        FileInputStream fis;
        
        fis = new FileInputStream("/home/aluno-di/area-de-aluno/eclipse-workspace/tp03/src/tp03/decrypted.txt");
        
        
        byte[] b = new byte[1024];
        int i = fis.read(b);
        while (i != -1) {
        	s.update(b, 0 , i);
        	i = fis.read(b);
        }
        
        boolean res = s.verify(assinaturaOriginal);
        System.out.println(res);
	}
}
