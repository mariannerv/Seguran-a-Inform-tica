package tp06;

import java.io.FileInputStream;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class verificaMAC {
	public static void main(String args[]) throws Exception {
		
		
		FileInputStream fis = new FileInputStream("a.txt");
		FileInputStream fisMAC = new FileInputStream("a.mac");
		
		
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
        
        byte [] macToBeVerified = new byte[fisMAC.available()];
        fisMAC.read(macToBeVerified);
        
        String s_mac = Base64.getEncoder().encodeToString(mac);
        
        String s_macToBeVerified = Base64.getEncoder().encodeToString(macToBeVerified);
        
        if (s_mac.equals(s_macToBeVerified)) {
        	System.out.println("são iguais :)");
        } else {
        	System.out.println("nac :(");
        }
		
	}
}
