package stockexchange.com.stockexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stockexchange.com.stockexchange.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, String>{

}
