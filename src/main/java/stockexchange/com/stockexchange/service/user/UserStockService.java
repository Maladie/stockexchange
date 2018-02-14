package stockexchange.com.stockexchange.service.user;

import stockexchange.com.stockexchange.model.Stock;

import java.util.Set;

public interface UserStockService {

    Set<Stock> getStocks(String token);
}
