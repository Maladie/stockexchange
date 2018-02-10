package stockexchange.com.stockexchange.model;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class Stocks {

    private Set<Stock> items;
    private Date publicationDate;

    public Stocks(Set<Stock> items) {
        this.items = items;
    }

    public Stocks() {
    }

    public Stocks(Set<Stock> items, String publicationDate) {
        this.items = items;
        this.publicationDate = new Date(publicationDate);
    }

    public Set<Stock> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public void setItems(Set<Stock> items) {
        this.items = items;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
}
