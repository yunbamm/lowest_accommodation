package bangbang.exception;

/**
 * Exception thrown when there is an attempt to register a duplicate watched accommodation
 */
public class DuplicateWatchedException extends RuntimeException {
    
    public DuplicateWatchedException(String message) {
        super(message);
    }
    
    public DuplicateWatchedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DuplicateWatchedException(String accommodationId, String checkIn, String checkOut) {
        super(String.format("이미 등록된 관심 숙소입니다. 숙소ID: %s, 체크인: %s, 체크아웃: %s", 
                           accommodationId, checkIn, checkOut));
    }
}
