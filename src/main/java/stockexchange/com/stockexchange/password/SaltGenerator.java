package stockexchange.com.stockexchange.password;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

public class SaltGenerator {

    public static String generate() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return toBase64(bytes);
    }

    private static byte[] fromBase64(String hex)
            throws IllegalArgumentException {
        return DatatypeConverter.parseBase64Binary(hex);
    }

    private static String toBase64(byte[] array) {
        return DatatypeConverter.printBase64Binary(array);
    }

}
