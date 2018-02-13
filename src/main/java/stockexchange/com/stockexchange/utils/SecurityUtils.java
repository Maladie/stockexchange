package stockexchange.com.stockexchange.utils;

import stockexchange.com.stockexchange.password.PasswordHasher;

public class SecurityUtils {

    public static boolean isPasswordMatch(String userPassword, String userSalt, String hashToVerify) {
        if (userPassword != null && hashToVerify != null && userSalt != null) {
            String regeneratedHash = PasswordHasher.generate(userPassword, userSalt);
            return regeneratedHash.equals(hashToVerify);
        }
        throw new IllegalArgumentException("One of arguments is null");
    }
}
