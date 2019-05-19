package io.github.yashchenkon.migration.core.exception;

/**
 * @author Mykola Yashchenko
 */
public class MigrationLockException extends RuntimeException {

    public MigrationLockException(String message) {
        super(message);
    }
}
