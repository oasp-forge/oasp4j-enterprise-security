package config;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyHandler {

	public PrivateKey getPrivateKey(String filename)
			  throws Exception {

				ClassLoader classLoader = this.getClass().getClassLoader();
				File file = new File(classLoader.getResource(filename).getFile());
			    FileInputStream fis = new FileInputStream(file);
			    DataInputStream dis = new DataInputStream(fis);
			    byte[] keyBytes = new byte[(int)file.length()];
			    dis.readFully(keyBytes);
			    dis.close();

			    PKCS8EncodedKeySpec spec =
			      new PKCS8EncodedKeySpec(keyBytes);
			    KeyFactory kf = KeyFactory.getInstance("RSA");
			    return kf.generatePrivate(spec);
			  }
	
	  public PublicKey getPublicKey(String filename)
			    throws Exception {

				ClassLoader classLoader = this.getClass().getClassLoader();
				File file = new File(classLoader.getResource(filename).getFile());
			    FileInputStream fis = new FileInputStream(file);
			    DataInputStream dis = new DataInputStream(fis);
			    byte[] keyBytes = new byte[(int)file.length()];
			    dis.readFully(keyBytes);
			    dis.close();

			    X509EncodedKeySpec spec =
			      new X509EncodedKeySpec(keyBytes);
			    KeyFactory kf = KeyFactory.getInstance("RSA");
			    return kf.generatePublic(spec);
			  }
	
}
