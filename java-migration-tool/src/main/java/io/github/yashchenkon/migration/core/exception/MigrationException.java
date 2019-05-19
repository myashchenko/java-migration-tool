package io.github.yashchenkon.migration.core.exception;

/**
 * @author Mykola Yashchenko
 */
public class MigrationException extends RuntimeException {

    public MigrationException() {
    }

    public MigrationException(String message) {
        super(message);
    }
}
