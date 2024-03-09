package exercicioSemConfusions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class clientDecipher {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 23456);

        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

        // Sending username and password
        outStream.writeObject("aa");
        outStream.writeObject("bb");

        Boolean authenticationResult = (Boolean) inStream.readObject();
        if (authenticationResult) {
            // Authentication successful

            // Receiving the encrypted file
            byte[] encryptedFileBytes = (byte[]) inStream.readObject();
            FileOutputStream encryptedFileStream = new FileOutputStream("received.cif");
            encryptedFileStream.write(encryptedFileBytes);
            encryptedFileStream.close();

            // Receiving the key
            byte[] keyBytes = (byte[]) inStream.readObject();
            SecretKey key = new SecretKeySpec(keyBytes, "AES");

            // Decrypting the file
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, key);

            FileInputStream encryptedInputStream = new FileInputStream("received.cif"); //ficheiro cifrado recebido pelo cliente
            FileOutputStream decryptedOutputStream = new FileOutputStream("decrypted.txt");
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = encryptedInputStream.read(buffer)) != -1) {
                byte[] decryptedBytes = c.update(buffer, 0, bytesRead);
                if (decryptedBytes != null) {
                    decryptedOutputStream.write(decryptedBytes);
                }
            }
            byte[] finalDecryptedBytes = c.doFinal();
            if (finalDecryptedBytes != null) {
                decryptedOutputStream.write(finalDecryptedBytes);
            }

            encryptedInputStream.close();
            decryptedOutputStream.close();

            System.out.println("File decrypted successfully.");
        } else {
            // Authentication failed
            System.out.println("Authentication failed.");
        }

        outStream.close();
        inStream.close();
        socket.close();
    }
}
