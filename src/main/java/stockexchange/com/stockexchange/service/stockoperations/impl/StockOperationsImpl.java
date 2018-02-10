package stockexchange.com.stockexchange.service.stockoperations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.exceptions.NotEnoughCashException;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.model.StockDto;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.repository.StockRepository;
import stockexchange.com.stockexchange.repository.UserRepository;
import stockexchange.com.stockexchange.service.stockoperations.StockOperations;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class StockOperationsImpl implements StockOperations {
    private UserRepository userRepository;
    private StockRepository stockRepository;

    @Autowired
    public StockOperationsImpl(UserRepository userRepository, StockRepository stockRepository) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public void buyStock(StockDto stockDto) {
        User user = userRepository.findById(stockDto.getUserId());
        try {
            checkIfUserHasEnoughMoney(user.getCash(), stockDto);
            Set<Stock> usersStock = user.getStocks();
        } catch (NotEnoughCashException e) {
        }
    }

    private void checkIfUserHasEnoughMoney(BigDecimal cash, StockDto stockDto) throws NotEnoughCashException {
        BigDecimal totalPrice = stockDto.getPrice().multiply(new BigDecimal(stockDto.getUnit()));
        if(cash.compareTo(totalPrice) > 0){
            throw new NotEnoughCashException();
        }
    }

    @Override
    public void sellStock(StockDto stockDto) {

    }
}
