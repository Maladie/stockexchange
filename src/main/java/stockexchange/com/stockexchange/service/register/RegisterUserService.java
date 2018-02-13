package stockexchange.com.stockexchange.service.register;

import stockexchange.com.stockexchange.info.Info;
import stockexchange.com.stockexchange.model.NewUserDto;

public interface RegisterUserService {
     Info register(NewUserDto newUserDto);
}
