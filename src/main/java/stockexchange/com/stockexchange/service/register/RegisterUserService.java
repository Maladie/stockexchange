package stockexchange.com.stockexchange.service.register;

import stockexchange.com.stockexchange.model.NewUserDto;

public interface RegisterUserService {
    public RegisterResult register(NewUserDto newUserDto);
}
