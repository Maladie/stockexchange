package stockexchange.com.stockexchange.model.factory;

import stockexchange.com.stockexchange.model.NewUserDto;
import stockexchange.com.stockexchange.model.User;

public interface UserFactory {
    public User createNewUser(NewUserDto userDto);
}
