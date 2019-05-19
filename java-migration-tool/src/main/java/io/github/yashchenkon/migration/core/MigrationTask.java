package io.github.yashchenkon.migration.core;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.yashchenkon.migration.core.exception.MigrationException;
import io.github.yashchenkon.migration.core.lock.MigrationLock;
import io.github.yashchenkon.migration.core.model.Migration;
import io.github.yashchenkon.migration.core.registry.MigrationRegistry;
import io.github.yashchenkon.migration.core.repository.MigrationRepository;
import io.github.yashchenkon.migration.core.version.Version;

import static java.util.stream.Collectors.toMap;

/**
 * @author Mykola Yashchenko
 */
public class MigrationTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTask.class);

    private final MigrationRepository migrationRepository;
    private final MigrationRegistry migrationRegistry;
    private final MigrationLock migrationLock;

    public MigrationTask(MigrationRepository migrationRepository, MigrationRegistry migrationRegistry, MigrationLock migrationLock) {
        this.migrationRepository = migrationRepository;
        this.migrationRegistry = migrationRegistry;
        this.migrationLock = migrationLock;
    }

    public void run() {
        LOGGER.info("Running migration tool...");

        Version currentVersion = migrationRepository.latestAppliedVersion();
        Version latestVersionToApply = migrationRegistry.latestVersion();

        if (currentVersion == null && latestVersionToApply == null) {
            LOGGER.info("Skipping migration task - nothing to apply");
            return;
        }

        assertMigrations();

        if (currentVersion != null && currentVersion.value().equals(latestVersionToApply.value())) {
            LOGGER.info("Skipping migration task - all migrations are applied already");
            return;
        }

        try {
            migrationLock.acquire();

            migrationRegistry.migrationsSince(currentVersion)
                    .forEach(migration -> {
                        migration.execute();
                        migrationRepository.apply(migration);
                    });
        } finally {
            migrationLock.release();
        }
    }

    private void assertMigrations() {
        List<Version> appliedVersions = migrationRepository.appliedVersions();
        List<Migration> migrations = migrationRegistry.migrations();

        Map<String, Migration> migrationByVersion = migrations.stream()
                .collect(toMap(migration -> migration.version().value(), Function.identity()));

        for (Version appliedVersion : appliedVersions) {
            if (!migrationByVersion.containsKey(appliedVersion.value())) {
                throw new MigrationException("Can't find migration " + appliedVersion.value() + " in the code");
            }
        }
    }
}
