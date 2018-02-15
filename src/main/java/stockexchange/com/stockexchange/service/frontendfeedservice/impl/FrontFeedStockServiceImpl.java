package stockexchange.com.stockexchange.service.frontendfeedservice.impl;

import org.springframework.stereotype.Service;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.service.frontendfeedservice.FrontFeedStockService;
import stockexchange.com.stockexchange.utils.CacheUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class FrontFeedStockServiceImpl implements FrontFeedStockService {

    @Override
    public Set<Stock> retrieveStocksFromCache() {
        Set<Stock> stocks = new HashSet<>();
        Map<String, Object> cache = CacheUtil.getCache();
        for (Object o : cache.values()) {
            if (o instanceof Stock) {
                stocks.add((Stock) o);
            }
        }
        return stocks;
    }

    @Override
    public String retrievePublicationDate() {
        return (String) CacheUtil.getFromCache("publicationDate");
    }
}
