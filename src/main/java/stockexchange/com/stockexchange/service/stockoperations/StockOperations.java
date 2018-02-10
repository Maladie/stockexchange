package stockexchange.com.stockexchange.service.stockoperations;

import stockexchange.com.stockexchange.model.StockDto;

public interface StockOperations {
    void buyStock(StockDto stockDto);
    void sellStock(StockDto stockDto);
}
