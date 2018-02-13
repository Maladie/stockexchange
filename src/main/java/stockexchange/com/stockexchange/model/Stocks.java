package stockexchange.com.stockexchange.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

public class Stocks {

    private Set<Stock> items;
    private LocalDateTime publicationDate;

    public Stocks(Set<Stock> items) {
        this.items = items;
    }

    public Stocks() {
    }

    public Stocks(Set<Stock> items, String publicationDate) {
        this.items = items;
        this.publicationDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(publicationDate)),
                TimeZone.getDefault().toZoneId());
    }

    public Set<Stock> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public void setItems(Set<Stock> items) {
        this.items = items;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }
}
