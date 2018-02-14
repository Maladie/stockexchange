package stockexchange.com.stockexchange.service.stockoperations;

import stockexchange.com.stockexchange.info.Info;
import stockexchange.com.stockexchange.model.StockDto;

public interface StockOperations {
    Info buyStock(StockDto stockDto, String token);

    Info sellStock(StockDto stockDto, String token);
}
