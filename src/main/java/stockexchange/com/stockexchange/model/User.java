package stockexchange.com.stockexchange.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2)
    private String name;

    @NotNull
    @Size(min = 2)
    private String surname;

    @NotNull
    @Size(min = 1)
    private String username;

    @NotNull
    @Size(min = 4)
    private String password;

    private String salt;

    @OneToMany
    private Set<Stock> stocks;

    @Embedded
    private Wallet wallet;

    public User() {
        wallet = new Wallet();
    }

    public void setCash(BigDecimal cash) {
        wallet.setCash(cash);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    public String getCurrency() {
        return wallet.getCurrency();
    }

    public BigDecimal getCash() {
        return wallet.getCash();
    }

    public void setCurrency(String currency) {
        wallet.setCurrency(currency);
    }
}
