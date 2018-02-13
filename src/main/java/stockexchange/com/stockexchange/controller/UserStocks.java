package stockexchange.com.stockexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import stockexchange.com.stockexchange.model.Stock;
import stockexchange.com.stockexchange.service.user.UserStockService;

import javax.servlet.http.HttpSession;
import java.util.Set;

@RestController
public class UserStocks {

    private UserStockService userStockService;

    @Autowired
    public UserStocks(UserStockService userStockService) {
        this.userStockService = userStockService;
    }

    @GetMapping(value = "/userStock")
    public Set<Stock> getUserStocks(HttpSession session){
        Long userId = Long.valueOf((String) session.getAttribute("userId"));
        return userStockService.getStocks(userId);
    }
}
