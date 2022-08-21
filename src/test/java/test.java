import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keystore.PEMKeyStore;
import org.fisco.bcos.sdk.model.CryptoType;
import org.junit.Test;

import javax.servlet.http.HttpServlet;
import java.security.KeyPair;

public class test {

    @Test
    public void getKeyPair() {
        String pemFilePath = "/home/ubuntu/fisco/console/account/ecdsa/0x0a77a457473abe3f65b0e537785587d9995597af.pem";
        // pk of 7af: 8e65a5deddfdc56cdf7fceeb2db6c184fe293b8d5d1096196996e51f2be15c50
        // pk of c1b: cf5c112c7f2a451b62c6776af7283b7cef433f6118ef335461a7139518ebad8e
        PEMKeyStore keyTool = new PEMKeyStore(pemFilePath);

        KeyPair keyPair = keyTool.getKeyPair();
        System.out.println(keyPair.getPrivate().toString());
        System.out.println("======");

        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        System.out.println(cryptoSuite.createKeyPair(keyPair).getHexPrivateKey());
        System.out.println("------");


    }


}
