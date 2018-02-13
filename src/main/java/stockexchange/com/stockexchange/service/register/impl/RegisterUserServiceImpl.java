package stockexchange.com.stockexchange.service.register.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.info.APIInfoCodes;
import stockexchange.com.stockexchange.info.Info;
import stockexchange.com.stockexchange.model.NewUserDto;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.model.factory.UserFactory;
import stockexchange.com.stockexchange.repository.UserRepository;
import stockexchange.com.stockexchange.service.login.LoginService;
import stockexchange.com.stockexchange.service.register.RegisterUserService;
import stockexchange.com.stockexchange.utils.EmptyCheck;

import java.util.UUID;

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
    public Info register(NewUserDto newUserDto) {
        Info info = new Info();
        info.setKey(UUID.randomUUID().toString());
        if (validUsernameAndPassword(newUserDto)) {
            User user = userFactory.createNewUser(newUserDto);
            userRepository.save(user);
            info.setDesc("Registration successful!");
            info.setInfoCode(APIInfoCodes.OK);
            log.debug("User created succesfuly");
            System.out.println(" Registered as {" + user.getUsername() + "}");
            return info;
        }
        log.debug("User already in database");
        //TODO
        info.setInfoCode(APIInfoCodes.INVALID_USERNAME);
        return info;
    }

    private boolean validUsernameAndPassword(NewUserDto newUserDto) {
        String username = newUserDto.getUsername();
        String pass = newUserDto.getPassword();

        return EmptyCheck.isNotNullOrEmpty(pass) && EmptyCheck.isNotNullOrEmpty(username) && loginService.isLoginFree(username);
    }


}
