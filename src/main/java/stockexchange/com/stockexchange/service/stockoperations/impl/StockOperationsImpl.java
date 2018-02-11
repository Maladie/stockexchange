package stockexchange.com.stockexchange.service.stockoperations.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.exceptions.NotEnoughCashException;
import stockexchange.com.stockexchange.exceptions.NotEnoughStockException;
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
    private StockRepository stockRepository;

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public StockOperationsImpl(UserRepository userRepository, StockRepository stockRepository) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
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
        Optional<Stock> optionalStock = stocks.stream().filter(stock -> stock.getName().equals(newStock.getName())).findFirst();
        if(optionalStock.isPresent()){
            Stock stock = optionalStock.get();
            updatePrice(stock, newStock);
            stockRepository.save(stock);
        } else {
            stocks.add(newStock);
            stockRepository.save(newStock);

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
    public void sellStock(StockDto stockDto) throws NotEnoughStockException {
        User user = userRepository.findById(stockDto.getUserId());
        if (!checkIfUserHasEnoughStock(user.getStocks(), stockDto)) {
            throw new NotEnoughStockException("Current stock is less than amount user wants to sell");
        }
        BigDecimal totalPrice = stockDto.getPrice().multiply(new BigDecimal(stockDto.getUnit()));
        increaseCashAmount(user, totalPrice);
        decreaseStockUnitsHeld(user, stockDto);
        log.info(stockDto.getUnit() + " " + stockDto.getName() + " sold by " + user.getUsername());
        userRepository.save(user);
    }

    private boolean checkIfUserHasEnoughStock(Set<Stock> stocks, StockDto stockDto) {
        Optional<Stock> optionalStock = stocks.stream().filter(stock -> stock.getName().equals(stockDto.getName())).findFirst();
        return optionalStock.filter(stock -> stock.getUnit() > stockDto.getUnit()).isPresent();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void decreaseStockUnitsHeld(User user, StockDto stockDto) {
        Set<Stock> stocks = user.getStocks();;
        Stock stock = stocks.stream().filter(stock1 -> stock1.getName().equals(stockDto.getName())).findFirst().get();
        stock.setUnit(stock.getUnit() - stockDto.getUnit());
        stockRepository.save(stock);
    }

    private void decreaseCashAmount(User user, BigDecimal totalPrice) {
        increaseCashAmount(user, totalPrice.negate());
    }
}
