package stockexchange.com.stockexchange;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.model.Stocks;
import stockexchange.com.stockexchange.utils.CacheUtil;

import java.util.Collections;
import java.util.Set;

public class StockFeed implements Runnable {

    @Override
    public void run() {
        while (true) {
            String resourceURL = "http://webtask.future-processing.com:8068/stocks";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<Stocks> exchange = restTemplate.exchange(resourceURL, HttpMethod.GET, entity, Stocks.class);
            Set<Stock> items = exchange.getBody().getItems();
            items.forEach(stock -> CacheUtil.addToCache(stock.getCode(), stock));
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
