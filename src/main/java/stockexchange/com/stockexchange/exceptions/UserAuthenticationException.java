package stockexchange.com.stockexchange.exceptions;

import org.springframework.security.core.AuthenticationException;
import stockexchange.com.stockexchange.service.authentication.UserAuthentication;


public class UserAuthenticationException extends AuthenticationException {
    private final UserAuthentication authentication;

    public UserAuthenticationException(String msg, UserAuthentication authentication) {
        super(msg);
        this.authentication = authentication;
    }

    public UserAuthenticationException(String msg) {
        super(msg);
        this.authentication = null;
    }

    public UserAuthentication getAuthentication() {
        return authentication;
    }
}
