package stockexchange.com.stockexchange.model;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
class Wallet {
    private BigDecimal cash;
    private String currency;

    Wallet() {
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}