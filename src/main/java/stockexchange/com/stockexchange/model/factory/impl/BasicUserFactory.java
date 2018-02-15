package stockexchange.com.stockexchange.model.factory.impl;

import org.springframework.stereotype.Component;
import stockexchange.com.stockexchange.model.NewUserDto;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.model.factory.UserFactory;
import stockexchange.com.stockexchange.password.PasswordHasher;
import stockexchange.com.stockexchange.password.SaltGenerator;

import java.math.BigDecimal;
import java.util.Collections;

@Component
public class BasicUserFactory implements UserFactory {

    @Override
    public User createNewUser(NewUserDto userDto) {
        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setSurname(userDto.getSurname());
        newUser.setUsername(userDto.getUsername());
        newUser.setCash(userDto.getCash() != null ? userDto.getCash() : new BigDecimal(0));
        newUser.setCurrency(userDto.getCurrency() != null ? userDto.getCurrency() : "PLN");
        newUser.setStocks(Collections.emptySet());
        setUserPassword(newUser, userDto.getPassword());
        return newUser;
    }

    private void setUserPassword(User user, String password) {
        String salt = SaltGenerator.generate();
        user.setSalt(salt);
        String userPassword = PasswordHasher.generate(password, salt);
        user.setPassword(userPassword);
    }
}
