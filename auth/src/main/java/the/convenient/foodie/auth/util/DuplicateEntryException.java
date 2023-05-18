package the.convenient.foodie.auth.util;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String errorMessage) {
        super(errorMessage);
    }
}