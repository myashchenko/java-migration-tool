package io.github.yashchenkon.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.yashchenkon.migration.registry.MigrationRegistry;
import io.github.yashchenkon.migration.repository.MigrationRepository;
import io.github.yashchenkon.migration.version.Version;

/**
 * @author Mykola Yashchenko
 */
public class MigrationTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTask.class);

    private final MigrationRepository migrationRepository;
    private final MigrationRegistry migrationRegistry;

    public MigrationTask(MigrationRepository migrationRepository, MigrationRegistry migrationRegistry) {
        this.migrationRepository = migrationRepository;
        this.migrationRegistry = migrationRegistry;
    }

    public void run() {
        Version currentVersion = migrationRepository.latestVersion();
        Version latestVersionToApply = migrationRegistry.latestVersion();

        if (latestVersionToApply.value() == currentVersion.value()) {
            LOGGER.info("Skipping migration task - all migrations are applied already");
            return;
        }

        migrationRegistry.migrationsSince(currentVersion)
                .stream()
                .sorted()
                .peek(Migration::execute)
                .forEach(migrationRepository::save);
    }

}
