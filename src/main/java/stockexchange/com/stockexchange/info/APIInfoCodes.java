package stockexchange.com.stockexchange.info;

public enum APIInfoCodes {

    OK(200000L),
    INVALID_USERNAME(400001L),
    INVALID_PASSWORD(400002L),
    USERNAME_ALREADY_USED(400003L),
    USERNAME_NOT_FOUND(400004L),
    WRONG_USER_PASSWORD(400005L);

    private long value;

    APIInfoCodes(Long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
