package io.github.yashchenkon.migration.core;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        verifyMigrations(currentVersion);

        if (currentVersion != null && currentVersion.value().equals(latestVersionToApply.value())) {
            LOGGER.info("Skipping migration task - all migrations are applied already");
            return;
        }

        try {
            migrationLock.acquire();

            migrationRegistry.migrationsSince(currentVersion)
                    .forEach(migration -> {
                        LOGGER.info("Migrating to version {}", migration.version());
                        migration.execute();
                        migrationRepository.apply(migration);
                    });
        } finally {
            migrationLock.release();
        }
    }

    private void verifyMigrations(Version currentVersion) {
        List<Version> appliedVersions = migrationRepository.appliedVersions();
        List<Migration> migrations = migrationRegistry.migrations();

        verifyDuplicates(migrations);

        Map<String, Migration> migrationByVersion = migrations.stream()
                .collect(toMap(migration -> migration.version().value(), Function.identity()));

        for (Version appliedVersion : appliedVersions) {
            if (!migrationByVersion.containsKey(appliedVersion.value())) {
                throw new MigrationException("Version " + appliedVersion.value() + " is already applied while it doesn't exists in the code");
            }
        }

        if (currentVersion == null) {
            return;
        }

        for (Migration migration : migrations) {
            if (migration.version().compareTo(currentVersion) < 0) {
                appliedVersions.stream()
                        .filter(version -> migration.version().value().equals(version.value()))
                        .findFirst()
                        .orElseThrow(() -> new MigrationException("Migration " + migration.getClass() + " has lower version than the latest applied migration. " +
                                "Its version " + migration.version().value() + " while latest version " + currentVersion.value()));
            }
        }
    }

    private void verifyDuplicates(List<Migration> migrations) {
        Set<String> versions = migrations
                .stream()
                .map(migration -> migration.version().value())
                .collect(Collectors.toSet());

        if (migrations.size() != versions.size()) {
            throw new MigrationException("There are migrations with identical versions");
        }
    }
}
