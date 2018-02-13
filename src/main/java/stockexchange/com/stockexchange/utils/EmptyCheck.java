package stockexchange.com.stockexchange.utils;

public class EmptyCheck {

    public static boolean isNotNullOrEmpty(String str) {
        return str != null || !str.trim().isEmpty();
    }

    public static boolean isNullObject(Object object) {
        return object == null;
    }
}
