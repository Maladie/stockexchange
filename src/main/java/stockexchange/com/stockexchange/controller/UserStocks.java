package stockexchange.com.stockexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import stockexchange.com.stockexchange.config.Constants;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.service.user.UserStockService;

import java.math.BigDecimal;
import java.util.Set;

@RestController
public class UserStocks {

    private UserStockService userStockService;

    @Autowired
    public UserStocks(UserStockService userStockService) {
        this.userStockService = userStockService;
    }

    @GetMapping(value = "/api/userstock")
    public Set<Stock> getUserStocks(@RequestHeader(Constants.HEADER_XSRF_AUTH_TOKEN) String token) {
        return userStockService.getStocks(token);
    }

    @GetMapping(value = "/api/usercash")
    public BigDecimal getUserCash(@RequestHeader(Constants.HEADER_XSRF_AUTH_TOKEN) String token) {
        return userStockService.getCash(token);
    }


}
