package io.github.yashchenkon.migration.datastore.lock;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreException;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.yashchenkon.migration.core.exception.MigrationLockException;
import io.github.yashchenkon.migration.core.lock.MigrationLock;

/**
 * @author Mykola Yashchenko
 */
public class DatastoreMigrationLock implements MigrationLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatastoreMigrationLock.class);

    private static final String DEFAULT_LOCK_NAME = "migrationLock";

    private final Datastore datastore;
    private final String lockName;

    public DatastoreMigrationLock(Datastore datastore) {
        this(datastore, DEFAULT_LOCK_NAME);
    }

    public DatastoreMigrationLock(Datastore datastore, String lockName) {
        this.datastore = datastore;
        this.lockName = lockName;
    }

    @Override
    public void acquire() {
        Key key = key();
        FullEntity<Key> entity = FullEntity.newBuilder(key)
                .set("status", "LOCK_ACQUIRED")
                .build();

        LOGGER.info("Acquiring Datastore lock...");
        try {
            datastore.add(entity);

            LOGGER.info("Datastore lock has been successfully acquired");
        } catch (DatastoreException e) {
            LOGGER.info("Failed to acquire Datastore lock: {}", e.getMessage());
            throw new MigrationLockException("Failed to get migration lock");
        }
    }

    @Override
    public void release() {
        LOGGER.info("Releasing Datastore lock");

        datastore.delete(key());

        LOGGER.info("Datastore lock has been successfully released");
    }

    private Key key() {
        return datastore.newKeyFactory()
                .setKind(lockName)
                .newKey(lockName);
    }
}
