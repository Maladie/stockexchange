package stockexchange.com.stockexchange.info;

public enum APIInfoCodes {

    OK(200000L),
    INVALID_USERNAME(400001L),
    INVALID_PASSWORD(400002L),
    USERNAME_ALREADY_USED(400003L),
    USERNAME_NOT_FOUND(400004L),
    WRONG_USER_PASSWORD(400005L),
    RELOGIN_NEEDED(400006L),
    TOKEN_NOT_FOUND(400007L),
    LACK_OF_CASH(400008L),
    SOLD_OUT(400009L);

    private long value;

    APIInfoCodes(Long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
