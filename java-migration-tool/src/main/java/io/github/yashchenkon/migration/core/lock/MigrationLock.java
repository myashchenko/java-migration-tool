package io.github.yashchenkon.migration.core.lock;

/**
 * @author Mykola Yashchenko
 */
public interface MigrationLock {
    void acquire();

    void release();
}
