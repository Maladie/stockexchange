package stockexchange.com.stockexchange.service.stockoperations.impl;

import org.slf4j.Logger;
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
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Set;

@Service
public class StockOperationsImpl implements StockOperations {
    private UserRepository userRepository;
    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public StockOperationsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void buyStock(StockDto stockDto) throws NotEnoughCashException {
        User user = userRepository.findById(stockDto.getUserId());
        BigDecimal totalPrice = stockDto.getPrice().multiply(new BigDecimal(stockDto.getUnit()));
        if (!checkIfUserHasEnoughMoney(user.getCash(), totalPrice)) {
            log.info("Not enough cash to buy " + stockDto.getName() + " by " + user.getUsername());
            throw new NotEnoughCashException("Current cash was not enough to buy given amount of stock");
        }
        decreaseCashAmount(user, totalPrice);
        addStockToUsersStocksWallet(user, stockDto);
        log.info(stockDto.getUnit() + " " + stockDto.getName() + " bought by " + user.getUsername());
        userRepository.save(user);
    }

    private void addStockToUsersStocksWallet(User user, StockDto stockDto) {
        Set<Stock> stocks = user.getStocks();
        Stock newStock = Stock.fromDto(stockDto);
        Optional<Stock> optionalStock = stocks.stream().filter(newStock::equals).findFirst();
        if(optionalStock.isPresent()){
            Stock stock = optionalStock.get();
            updatePrice(stock, newStock);
        } else {
            stocks.add(newStock);
        }
    }

    private void updatePrice(Stock stock, Stock newStock) {
        BigDecimal currentStockPricePerUnit = stock.getPrice();
        BigDecimal currentUnits = new BigDecimal(stock.getUnit());
        BigDecimal currentStockTotalPrice = currentStockPricePerUnit.multiply(currentUnits);

        BigDecimal addedStockPricePerUnit = newStock.getPrice();
        BigDecimal addedUnits = new BigDecimal(newStock.getUnit());
        BigDecimal addedStockTotalPrice = addedStockPricePerUnit.multiply(addedUnits);

        BigDecimal totalUnitsAmount = currentUnits.add(addedUnits);
        BigDecimal totalPrice = currentStockTotalPrice.add(addedStockTotalPrice);

        BigDecimal totalPricePerUnit = totalPrice.divide(totalUnitsAmount, RoundingMode.HALF_DOWN);

        stock.setUnit(totalUnitsAmount.longValue());
        stock.setPrice(totalPricePerUnit);
    }

    private void increaseCashAmount(User user, BigDecimal totalPrice) {
        BigDecimal usersCash = user.getCash();
        user.setCash(usersCash.add(totalPrice));
    }

    private boolean checkIfUserHasEnoughMoney(BigDecimal cash, BigDecimal totalPrice) {
        return (cash.compareTo(totalPrice) > 0);
    }

    @Override
    public void sellStock(StockDto stockDto) {
        User user = userRepository.findById(stockDto.getUserId());
        BigDecimal totalPrice = stockDto.getPrice().multiply(new BigDecimal(stockDto.getUnit()));
        increaseCashAmount(user, totalPrice);
        decreaseStockUnitsHeld(user, stockDto);
        log.info(stockDto.getUnit() + " " + stockDto.getName() + " sold by " + user.getUsername());
        userRepository.save(user);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void decreaseStockUnitsHeld(User user, StockDto stockDto) {
        Set<Stock> stocks = user.getStocks();
        Stock soldStock = Stock.fromDto(stockDto);
        Stock stock = stocks.stream().filter(soldStock::equals).findFirst().get();
        stock.setUnit(stock.getUnit() - soldStock.getUnit());
    }

    private void decreaseCashAmount(User user, BigDecimal totalPrice) {
        increaseCashAmount(user, totalPrice.negate());
    }
}
