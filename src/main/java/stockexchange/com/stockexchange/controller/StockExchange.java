package stockexchange.com.stockexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import stockexchange.com.stockexchange.exceptions.NotEnoughCashException;
import stockexchange.com.stockexchange.exceptions.NotEnoughStockException;
import stockexchange.com.stockexchange.model.StockDto;
import stockexchange.com.stockexchange.model.Stocks;
import stockexchange.com.stockexchange.service.stockoperations.StockOperations;

import java.util.Collections;

@RestController
public class StockExchange {

    private StockOperations stockOperations;

    @Autowired
    public StockExchange(StockOperations stockOperations) {
        this.stockOperations = stockOperations;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Stocks getStocks(){
        String resourceURL = "http://webtask.future-processing.com:8068/stocks";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Stocks> exchange = restTemplate.exchange(resourceURL, HttpMethod.GET, entity, Stocks.class);
        return exchange.getBody();
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public ExchangeCode buyStocks(@RequestBody StockDto stockDto){
        try {
            stockOperations.buyStock(stockDto);
        } catch (NotEnoughCashException e) {
            return ExchangeCode.LACK_OF_CASH;
        }
        return ExchangeCode.SUCCESS;
    }

    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public ExchangeCode sellStocks(@RequestBody StockDto stockDto) {
        try {
            stockOperations.sellStock(stockDto);
        } catch (NotEnoughStockException e) {
            return ExchangeCode.SOLD_OUT;
        }
        return ExchangeCode.SUCCESS;
    }
}
