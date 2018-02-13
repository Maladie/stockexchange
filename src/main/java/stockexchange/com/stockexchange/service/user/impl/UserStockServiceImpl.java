package stockexchange.com.stockexchange.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.repository.UserRepository;
import stockexchange.com.stockexchange.service.user.UserStockService;

import java.util.Collections;
import java.util.Set;

@Service
public class UserStockServiceImpl implements UserStockService {

    private UserRepository userRepository;

    @Autowired
    public UserStockServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<Stock> getStocks(Long userId) {
        User user = userRepository.findById(userId);
        return user != null ? user.getStocks() : Collections.emptySet();
    }
}
