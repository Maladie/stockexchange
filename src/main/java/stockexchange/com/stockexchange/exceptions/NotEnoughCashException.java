package stockexchange.com.stockexchange.exceptions;

public class NotEnoughCashException extends Exception {
    public NotEnoughCashException(String message) {
        super(message);
    }
}
