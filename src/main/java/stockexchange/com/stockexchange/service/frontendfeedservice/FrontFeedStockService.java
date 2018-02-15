package stockexchange.com.stockexchange.service.frontendfeedservice;

import stockexchange.com.stockexchange.model.Stock;

import java.util.Set;

public interface FrontFeedStockService {

    Set<Stock> retrieveStocksFromCache();

    String retrievePublicationDate();
}
