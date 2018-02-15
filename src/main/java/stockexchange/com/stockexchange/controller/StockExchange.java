package stockexchange.com.stockexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stockexchange.com.stockexchange.config.Constants;
import stockexchange.com.stockexchange.info.Info;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.model.StockDto;
import stockexchange.com.stockexchange.service.frontendfeedservice.FrontFeedStockService;
import stockexchange.com.stockexchange.service.stockoperations.StockOperations;

import java.util.Set;

@RestController
@RequestMapping(value = "/api")
public class StockExchange {

    private StockOperations stockOperations;
    private FrontFeedStockService frontFeedStockService;

    @Autowired
    public StockExchange(StockOperations stockOperations, FrontFeedStockService frontFeedStockService) {
        this.stockOperations = stockOperations;
        this.frontFeedStockService = frontFeedStockService;
    }

    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
    private Set<Stock> getStocks() {
        return frontFeedStockService.retrieveStocksFromCache();
    }

    @RequestMapping(value = "/publication", method = RequestMethod.GET)
    private String getPublicationDate() {
        return frontFeedStockService.retrievePublicationDate();
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
