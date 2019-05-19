package io.github.yashchenkon.migration.core.repository;

import java.util.List;

import io.github.yashchenkon.migration.core.model.Migration;
import io.github.yashchenkon.migration.core.version.Version;

/**
 * @author Mykola Yashchenko
 */
public interface MigrationRepository {
    Version latestAppliedVersion();

    void apply(Migration migration);

    List<Version> appliedVersions();
}
