package stockexchange.com.stockexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import stockexchange.com.stockexchange.config.Constants;
import stockexchange.com.stockexchange.info.Info;
import stockexchange.com.stockexchange.model.StockDto;
import stockexchange.com.stockexchange.model.Stocks;
import stockexchange.com.stockexchange.service.stockoperations.StockOperations;

import java.util.Collections;

@RestController
@RequestMapping(value = "/api")
public class StockExchange {

    private StockOperations stockOperations;

    @Autowired
    public StockExchange(StockOperations stockOperations) {
        this.stockOperations = stockOperations;
    }

    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
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
    public ResponseEntity<Info> buyStocks(@RequestBody StockDto stockDto, @RequestHeader(Constants.HEADER_XSRF_AUTH_TOKEN) String token) {
        Info info = stockOperations.buyStock(stockDto, token);
        return new ResponseEntity<>(info, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public ResponseEntity<Info> sellStocks(@RequestBody StockDto stockDto, @RequestHeader(Constants.HEADER_XSRF_AUTH_TOKEN) String token) {
        Info info = stockOperations.sellStock(stockDto, token);
        return new ResponseEntity<>(info, HttpStatus.ACCEPTED);
    }
}
