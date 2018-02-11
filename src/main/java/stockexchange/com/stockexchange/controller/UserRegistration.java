package stockexchange.com.stockexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import stockexchange.com.stockexchange.model.NewUserDto;
import stockexchange.com.stockexchange.service.register.RegisterResult;
import stockexchange.com.stockexchange.service.register.RegisterUserService;

@RestController
public class UserRegistration {

    private RegisterUserService registerUserService;

    @Autowired
    public UserRegistration(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        NewUserDto userDto = new NewUserDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RegisterResult registerNewUser(@RequestBody NewUserDto newUserDto){
        RegisterResult result = registerUserService.register(newUserDto);
        if(result.equals(RegisterResult.SUCCESS)){
            //TODO Autologowanie
            return RegisterResult.SUCCESS;
        }else {
            return RegisterResult.FAIL;
        }
    }
}
