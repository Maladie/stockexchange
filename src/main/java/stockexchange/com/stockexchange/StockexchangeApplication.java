package stockexchange.com.stockexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import stockexchange.com.stockexchange.utils.CacheUtil;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class StockexchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockexchangeApplication.class, args);
		CacheUtil.init();
	}
}
