package io.github.yashchenkon.migration.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.github.yashchenkon.migration.Migration;
import io.github.yashchenkon.migration.exception.MigrationException;
import io.github.yashchenkon.migration.version.Version;

/**
 * @author Mykola Yashchenko
 */
public class SimpleMigrationRegistry implements MigrationRegistry {

    private final List<Migration> migrations;

    public SimpleMigrationRegistry(Migration... migrations) {
        this(Arrays.asList(migrations));
    }

    public SimpleMigrationRegistry(List<Migration> migrations) {
        this.migrations = new ArrayList<>(migrations);
    }

    @Override
    public Version latestVersion() {
        return migrations
                .stream()
                .map(Migration::version)
                .max(Comparator.naturalOrder())
                .orElseThrow(MigrationException::new);
    }

    @Override
    public List<Migration> migrationsSince(Version version) {
        return migrations
                .stream()
                .filter(migration -> migration.version().compareTo(version) > 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Migration> migrationsUntil(Version version) {
        return migrations
                .stream()
                .filter(migration -> migration.version().compareTo(version) <= 0)
                .collect(Collectors.toList());
    }

}
