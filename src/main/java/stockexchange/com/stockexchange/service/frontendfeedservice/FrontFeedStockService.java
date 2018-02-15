package stockexchange.com.stockexchange.service.frontendfeedservice;

import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.model.Stock;

import java.util.Set;

@Service
public interface FrontFeedStockService {

    Set<Stock> retrieveStocksFromCache();

    String retrievePublicationDate();
}
