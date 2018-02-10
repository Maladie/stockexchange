package stockexchange.com.stockexchange.service.register.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.model.NewUserDto;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.model.factory.UserFactory;
import stockexchange.com.stockexchange.repository.UserRepository;
import stockexchange.com.stockexchange.service.login.LoginService;
import stockexchange.com.stockexchange.service.register.RegisterResult;
import stockexchange.com.stockexchange.service.register.RegisterUserService;


@Service
@Primary
public class RegisterUserServiceImpl implements RegisterUserService {

    private UserFactory userFactory;
    private LoginService loginService;
    private UserRepository userRepository;

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public RegisterUserServiceImpl(UserFactory userFactory, LoginService loginService, UserRepository userRepository) {
        this.userFactory = userFactory;
        this.loginService = loginService;
        this.userRepository = userRepository;

    }

    @Override
    public RegisterResult register(NewUserDto newUserDto) {
        boolean validUsername = loginService.isLoginFree(newUserDto.getUsername());

        if (validUsername) {
            User user = userFactory.createNewUser(newUserDto);
            userRepository.save(user);
            log.debug("User created succesfuly");
            System.out.println(" Registered as {" + user.getUsername() + "}");
            return RegisterResult.SUCCESS;
        }
        log.debug("User already in database");
        return RegisterResult.FAIL;
    }
}
