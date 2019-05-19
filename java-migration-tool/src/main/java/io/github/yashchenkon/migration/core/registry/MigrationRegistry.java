package io.github.yashchenkon.migration.core.registry;

import java.util.List;

import io.github.yashchenkon.migration.core.model.Migration;
import io.github.yashchenkon.migration.core.version.Version;

/**
 * @author Mykola Yashchenko
 */
public interface MigrationRegistry {
    Version latestVersion();

    List<Migration> migrationsSince(Version version);

    List<Migration> migrations();
}
