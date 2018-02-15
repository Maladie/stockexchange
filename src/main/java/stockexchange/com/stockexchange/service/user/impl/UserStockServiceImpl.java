package stockexchange.com.stockexchange.service.user.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.exceptions.TokenException;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.repository.UserRepository;
import stockexchange.com.stockexchange.service.authentication.TokenHandlerService;
import stockexchange.com.stockexchange.service.user.UserStockService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

@Service
public class UserStockServiceImpl implements UserStockService {

    private UserRepository userRepository;
    private TokenHandlerService tokenHandlerService;

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public UserStockServiceImpl(UserRepository userRepository, TokenHandlerService tokenHandlerService) {
        this.userRepository = userRepository;
        this.tokenHandlerService = tokenHandlerService;
    }

    @Override
    public Set<Stock> getStocks(String token) {
        User user = null;
        try {
            user = tokenHandlerService.parseUserFromToken(token);
            user = userRepository.findById(user.getId());
        } catch (TokenException e) {
            log.debug("Token not found", e);
        }
        return user != null ? user.getStocks() : Collections.emptySet();
    }

    @Override
    public BigDecimal getCash(String token) {
        User user = null;
        try {
            user = tokenHandlerService.parseUserFromToken(token);
            user = userRepository.findById(user.getId());
        } catch (TokenException e) {
            log.debug("Token not found", e);
        }
        return user != null ? user.getCash() : new BigDecimal(0);
    }
}
