package stockexchange.com.stockexchange.service.authentication;

import stockexchange.com.stockexchange.exceptions.TokenException;
import stockexchange.com.stockexchange.model.User;

public interface TokenHandlerService {

     User parseUserFromToken(String token) throws TokenException;

     String calculateTokenForUser(User user);

     void insertToCache(String token, User user);


}
