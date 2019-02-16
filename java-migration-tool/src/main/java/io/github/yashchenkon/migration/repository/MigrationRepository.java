package io.github.yashchenkon.migration.repository;

import java.util.List;

import io.github.yashchenkon.migration.Migration;
import io.github.yashchenkon.migration.version.Version;

/**
 * @author Mykola Yashchenko
 */
public interface MigrationRepository {
    Version latestVersion();

    void save(Migration migration);

    List<Migration> appliedMigrations();
}
