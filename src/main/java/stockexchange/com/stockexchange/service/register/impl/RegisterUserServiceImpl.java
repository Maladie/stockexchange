package stockexchange.com.stockexchange.service.register.impl;

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
            userRepository.persistUser(user);
            System.out.println("=========================================================");
            System.out.println(" Registered as {" + user.getUsername() + "}");
            System.out.println("=========================================================");
            return RegisterResult.SUCCESS;
        }
        System.out.println("=========================================================");
        System.out.println(" User login (" + newUserDto.getUsername() + ") already used!!! ");
        System.out.println("=========================================================");
        return RegisterResult.FAIL;
    }
}
