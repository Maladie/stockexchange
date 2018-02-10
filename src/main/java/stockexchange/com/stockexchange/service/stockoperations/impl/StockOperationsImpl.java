package stockexchange.com.stockexchange.service.stockoperations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.model.StockDto;
import stockexchange.com.stockexchange.repository.UserRepository;
import stockexchange.com.stockexchange.service.stockoperations.StockOperations;

@Service
public class StockOperationsImpl implements StockOperations {
    private UserRepository userRepository;

    @Autowired
    public StockOperationsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void buyStock(StockDto stockDto) {
        
    }

    @Override
    public void sellStock(StockDto stockDto) {

    }
}
