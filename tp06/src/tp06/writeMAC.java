package tp06;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class writeMAC {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		
		FileInputStream fis = new FileInputStream("a.txt");
		FileOutputStream fos = new FileOutputStream("a.mac");
		
		byte [] password = "123456".getBytes();
		
		SecretKey key = new SecretKeySpec(password, "HmacSHA256");
		
		Mac m = Mac.getInstance("HmacSHA256");
		m.init(key);
		byte [] b = new byte[16];
		
        int i = fis.read(b);
        while (i != -1) {
            m.update(b, 0, i);
            i = fis.read(b);
        }
        
        byte [] mac = m.doFinal();
        
        System.out.println("ficheiro macciado com sucesso");
        fos.write(mac);
        fos.close();
        fis.close();
      
	}
}		
