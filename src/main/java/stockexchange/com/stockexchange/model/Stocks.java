package stockexchange.com.stockexchange.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Stocks {

    private Set<Stock> items;
    private String publicationDate;

    public Stocks(Set<Stock> items) {
        this.items = items;
    }

    public Stocks() {
    }

    public Stocks(Set<Stock> items, String publicationDate) {
        this.items = items;
        this.publicationDate = Instant.parse("2014-09-11T21:28:29.429209Z")
                        .atZone(ZoneId.of("Europe/Warsaw"))
                        .format(DateTimeFormatter.ofPattern("MMM d uuuu hh:mm a z", Locale.ROOT));
    }

    public Set<Stock> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public void setItems(Set<Stock> items) {
        this.items = items;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }
}
