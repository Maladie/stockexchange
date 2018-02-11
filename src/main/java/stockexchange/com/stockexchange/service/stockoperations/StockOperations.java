package stockexchange.com.stockexchange.service.stockoperations;

import stockexchange.com.stockexchange.exceptions.NotEnoughCashException;
import stockexchange.com.stockexchange.exceptions.NotEnoughStockException;
import stockexchange.com.stockexchange.model.StockDto;

public interface StockOperations {
    void buyStock(StockDto stockDto) throws NotEnoughCashException;

    void sellStock(StockDto stockDto) throws NotEnoughStockException;
}
