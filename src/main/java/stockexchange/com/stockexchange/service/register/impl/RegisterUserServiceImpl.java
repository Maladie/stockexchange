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

import java.util.UUID;

import static stockexchange.com.stockexchange.utils.EmptyCheck.isNotNullOrEmpty;

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
        String username = newUserDto.getUsername();
        String password = newUserDto.getPassword();
        if (!isUsernameNotEmpty(username)) {
            return returnInvalidUsernameInfo(info);
        }
        if (!isUsernameFree(username)) {
            return returnUsernameTakenInfo(info);
        }
        if (!isPassNotEmpty(password)) {
            return returnInvalidPaswordInfo(info);
        }
        User user = userFactory.createNewUser(newUserDto);
        userRepository.save(user);
        info.setDesc("Registration successful!");
        info.setInfoCode(APIInfoCodes.OK);
        log.debug("User created succesfuly");
        System.out.println(" Registered as {" + user.getUsername() + "}");
        return info;
    }

    private Info returnInvalidPaswordInfo(Info info) {
        return cannotRegisterInfo("Password is invalid", APIInfoCodes.INVALID_PASSWORD, info);
    }

    private Info returnUsernameTakenInfo(Info info) {
        return cannotRegisterInfo("Username already taken", APIInfoCodes.USERNAME_ALREADY_USED, info);
    }

    private Info returnInvalidUsernameInfo(Info info) {
        return cannotRegisterInfo("Username is empty", APIInfoCodes.INVALID_USERNAME, info);
    }

    private boolean isUsernameFree(String username) {
        return loginService.isLoginFree(username);
    }

    private boolean isPassNotEmpty(String pass) {
        return isNotNullOrEmpty(pass);
    }

    private boolean isUsernameNotEmpty(String username) {
        return isNotNullOrEmpty(username);
    }

    private Info cannotRegisterInfo(String description, APIInfoCodes apiCode, Info info) {
        info.setHttpStatusCode(400L);
        info.setDesc(description);
        info.setInfoCode(apiCode);
        return info;
    }
}
