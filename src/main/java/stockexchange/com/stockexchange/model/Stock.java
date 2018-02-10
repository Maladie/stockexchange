package stockexchange.com.stockexchange.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Stock {
    @Id
    private String name;

    private String code;

    private Long unit;

    private BigDecimal price;

    public Stock() {
    }

    public Stock(String name, String code, Long unit, BigDecimal price) {
        this.name = name;
        this.code = code;
        this.unit = unit;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUnit() {
        return unit;
    }

    public void setUnit(Long unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public StockDto toDto(Long userId){
        StockDto dto = new StockDto();
        dto.setName(name);
        dto.setCode(code);
        dto.setPrice(price);
        dto.setUnit(unit);
        dto.setUserId(userId);
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;

        Stock stock = (Stock) o;

        return (name != null ? name.equals(stock.name) : stock.name == null) && (code != null ? !code.equals(stock.code) : stock.code != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    public static Stock fromDto(StockDto stockDto){
        Stock stock = new Stock();
        stock.setCode(stockDto.getCode());
        stock.setName(stockDto.getName());
        stock.setPrice(stockDto.getPrice());
        stock.setUnit(stockDto.getUnit());
        return stock;
    }
}
