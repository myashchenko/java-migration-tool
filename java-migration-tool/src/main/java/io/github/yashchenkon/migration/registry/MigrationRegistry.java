package io.github.yashchenkon.migration.registry;

import java.util.List;

import io.github.yashchenkon.migration.Migration;
import io.github.yashchenkon.migration.version.Version;

/**
 * @author Mykola Yashchenko
 */
public interface MigrationRegistry {
    Version latestVersion();

    List<Migration> migrationsSince(Version version);

    List<Migration> migrationsUntil(Version version);
}
