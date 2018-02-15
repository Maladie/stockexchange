package stockexchange.com.stockexchange.service.user;

import stockexchange.com.stockexchange.model.Stock;

import java.math.BigDecimal;
import java.util.Set;

public interface UserStockService {

    Set<Stock> getStocks(String token);

    BigDecimal getCash(String token);
}
