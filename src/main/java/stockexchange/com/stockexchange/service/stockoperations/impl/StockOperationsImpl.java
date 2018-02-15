package stockexchange.com.stockexchange.service.stockoperations.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.exceptions.TokenException;
import stockexchange.com.stockexchange.info.APIInfoCodes;
import stockexchange.com.stockexchange.info.Info;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.model.StockDto;
import stockexchange.com.stockexchange.model.User;
import stockexchange.com.stockexchange.repository.StockRepository;
import stockexchange.com.stockexchange.repository.UserRepository;
import stockexchange.com.stockexchange.service.authentication.TokenHandlerService;
import stockexchange.com.stockexchange.service.stockoperations.StockOperations;
import stockexchange.com.stockexchange.utils.CacheUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class StockOperationsImpl implements StockOperations {
    private UserRepository userRepository;
    private StockRepository stockRepository;
    private TokenHandlerService tokenHandlerService;

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public StockOperationsImpl(UserRepository userRepository, StockRepository stockRepository, TokenHandlerService tokenHandlerService) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.tokenHandlerService = tokenHandlerService;
    }

    @Override
    public Info buyStock(StockDto stockDto, String token) {
        User user;
        try {
            user = tokenHandlerService.parseUserFromToken(token);
            user = userRepository.findById(user.getId());
        } catch (TokenException e) {
            return returnInfoWhenTokenExceptionCatched(e);
        }
        if (!checkIfUnitsAndPriceAreCorrectWhileBuying(stockDto)) {
            return returnUnsucesfulInfo("Current price/units are not correct", APIInfoCodes.INCORECT_REQUEST);
        }
        ;
        BigDecimal totalPrice = stockDto.getPrice().multiply(new BigDecimal(stockDto.getUnit()));
        if (!checkIfUserHasEnoughMoney(user.getCash(), totalPrice)) {
            log.debug("Not enough cash to buy " + stockDto.getName() + " by " + user.getUsername());
            return returnUnsucesfulInfo("Current cash was not enough to buy given amount of stock", APIInfoCodes.LACK_OF_CASH);
        }
        decreaseCashAmount(user, totalPrice);
        addStockToUsersStocksWallet(user, stockDto);
        log.debug(stockDto.getUnit() + " " + stockDto.getName() + " bought by " + user.getUsername());
        userRepository.save(user);
        return returnSuccesfulInfo("Stock bought succesfuly");
    }

    private boolean checkIfUnitsAndPriceAreCorrectWhileBuying(StockDto stockDto) {
        Stock cachedStock = (Stock) CacheUtil.getFromCache(stockDto.getCode());
        return cachedStock.getUnit().compareTo(stockDto.getUnit()) <= 0 && cachedStock.getPrice().compareTo(stockDto.getPrice()) <= 0;
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
    public Info sellStock(StockDto stockDto, String token) {
        User user;
        try {
            user = tokenHandlerService.parseUserFromToken(token);
            user = userRepository.findById(user.getId());
        } catch (TokenException e) {
            return returnInfoWhenTokenExceptionCatched(e);
        }
        if (!checkIfUserHasEnoughStock(user.getStocks(), stockDto)) {
            log.debug("Current stock is less than amount user: " + user.getUsername() + " wants to sell");
            return returnUnsucesfulInfo("Current stock is less than amount user wants to sell", APIInfoCodes.SOLD_OUT);
        }
        if (!checkIfUnitsAndPriceAreCorrectWhileSelling(stockDto)) {
            return returnUnsucesfulInfo("Current price/units are not correct", APIInfoCodes.INCORECT_REQUEST);
        }
        BigDecimal totalPrice = stockDto.getPrice().multiply(new BigDecimal(stockDto.getUnit()));
        increaseCashAmount(user, totalPrice);
        decreaseStockUnitsHeld(user, stockDto);
        log.info(stockDto.getUnit() + " " + stockDto.getName() + " sold by " + user.getUsername());
        userRepository.save(user);
        return returnSuccesfulInfo("Stock sold succesfuly");
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

    private Info returnInfoWhenTokenExceptionCatched(TokenException e) {
        Info info = new Info();
        info.setKey(UUID.randomUUID().toString());
        info.setDesc("Token not found");
        info.setHttpStatusCode(400L);
        info.setInfoCode(APIInfoCodes.TOKEN_NOT_FOUND);
        log.debug("Token not found " + e);
        return info;
    }

    private Info returnSuccesfulInfo(String description) {
        Info info = new Info();
        info.setKey(UUID.randomUUID().toString());
        info.setDesc(description);
        info.setHttpStatusCode(200L);
        info.setInfoCode(APIInfoCodes.OK);
        return info;
    }

    private Info returnUnsucesfulInfo(String description, APIInfoCodes code) {
        Info info = new Info();
        info.setKey(UUID.randomUUID().toString());
        info.setDesc(description);
        info.setHttpStatusCode(400L);
        info.setInfoCode(APIInfoCodes.SOLD_OUT);
        return info;
    }

    private boolean checkIfUnitsAndPriceAreCorrectWhileSelling(StockDto stockDto) {
        Stock cachedStock = (Stock) CacheUtil.getFromCache(stockDto.getCode());
        return cachedStock.getUnit().compareTo(stockDto.getUnit()) >= 0 && cachedStock.getPrice().compareTo(stockDto.getPrice()) >= 0;
    }

}
