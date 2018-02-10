package stockexchange.com.stockexchange.password;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;

public class PasswordHasher {

    public static String generate(String password, String salt) {
        String saltedPass = salt + password;
        return DigestUtils.sha256Hex(saltedPass.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean verifyPassword(String password, String hash, String salt) {
        if (password != null && hash != null && salt != null) {
            String regeneratedHash = generate(password, salt);
            return regeneratedHash.equals(hash);
        }
        throw new IllegalArgumentException("One of arguments is null");
    }
}
