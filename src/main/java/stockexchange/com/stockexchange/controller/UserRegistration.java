package stockexchange.com.stockexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import stockexchange.com.stockexchange.info.APIInfoCodes;
import stockexchange.com.stockexchange.info.Info;
import stockexchange.com.stockexchange.model.NewUserDto;
import stockexchange.com.stockexchange.service.register.RegisterUserService;

@RestController
public class UserRegistration {

    private RegisterUserService registerUserService;

    @Autowired
    public UserRegistration(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Info> registerNewUser(@RequestBody NewUserDto newUserDto){
        Info result = registerUserService.register(newUserDto);
        if(APIInfoCodes.OK.equals(result.getInfoCode())){
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
}
