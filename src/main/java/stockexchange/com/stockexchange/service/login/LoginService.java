package stockexchange.com.stockexchange.service.login;

public interface LoginService {
    boolean isLoginValid(String login);
    boolean isPasswordValid(String login, String password);
    boolean isLoginFree(String login);
}
