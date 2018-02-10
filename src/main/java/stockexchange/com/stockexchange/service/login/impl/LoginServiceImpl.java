package stockexchange.com.stockexchange.service.login.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.password.PasswordHasher;
import stockexchange.com.stockexchange.repository.UserRepository;
import stockexchange.com.stockexchange.service.login.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    UserRepository userRepository;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isLoginValid(String login){
        if(login != null && !login.equals("")){
            User user = userRepository.findByUsername(login);
            return user != null;
        }
        return false;
    }

    public boolean isLoginFree(String login){
        return !isLoginValid(login);
    }

    public  boolean isPasswordValid(String login, String password){
        User user = userRepository.findByUsername(login);
        String userPassHash = user.getPassword();
        String userPassSalt = user.getSalt();
        return PasswordHasher.verifyPassword(password, userPassHash, userPassSalt);
    }
}
