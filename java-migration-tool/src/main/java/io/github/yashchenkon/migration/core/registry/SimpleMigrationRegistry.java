package io.github.yashchenkon.migration.core.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.github.yashchenkon.migration.core.model.Migration;
import io.github.yashchenkon.migration.core.version.Version;

/**
 * @author Mykola Yashchenko
 */
public class SimpleMigrationRegistry implements MigrationRegistry {

    private final List<Migration> migrations;

    public SimpleMigrationRegistry(Migration... migrations) {
        this(Arrays.asList(migrations));
    }

    public SimpleMigrationRegistry(List<Migration> migrations) {
        this.migrations = migrations.stream()
                .sorted(Comparator.comparing(Migration::version))
                .collect(Collectors.toList());
    }

    @Override
    public Version latestVersion() {
        return migrations
                .stream()
                .map(Migration::version)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    @Override
    public List<Migration> migrationsSince(Version version) {
        if (version == null) {
            return migrations;
        }

        return migrations
                .stream()
                .filter(migration -> migration.version().compareTo(version) > 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Migration> migrations() {
        return new ArrayList<>(migrations);
    }

}
