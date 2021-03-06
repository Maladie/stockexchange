package stockexchange.com.stockexchange.service.authentication;

public enum TokenStatus {
    ACTIVE(0L),
    EXPIRED_TIME(1L);

    private long status;

    TokenStatus(long status) {
        this.status = status;
    }

    public long getTokenStatus() {
        return status;
    }
}
