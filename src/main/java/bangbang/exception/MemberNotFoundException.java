package bangbang.exception;

/**
 * Exception thrown when a member is not found
 * <p>
 * Learning Notes:
 * - Extends RuntimeException (Unchecked Exception)
 * - Unchecked Exception: No need to declare in throws clause or force try-catch
 * - Spring @Transactional automatically rollbacks on RuntimeException
 * - Business logic exceptions usually extend RuntimeException for convenience
 * - Checked Exception would require explicit throws/try-catch everywhere
 * - Checked Exception doesn't rollbacks @Transactional by default (= need to explicitly configure)
 */
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
