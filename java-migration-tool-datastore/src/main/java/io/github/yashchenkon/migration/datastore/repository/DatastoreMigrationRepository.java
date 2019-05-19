package io.github.yashchenkon.migration.datastore.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

import io.github.yashchenkon.migration.core.model.Migration;
import io.github.yashchenkon.migration.core.repository.MigrationRepository;
import io.github.yashchenkon.migration.core.version.SimpleVersion;
import io.github.yashchenkon.migration.core.version.Version;

/**
 * @author Mykola Yashchenko
 */
public class DatastoreMigrationRepository implements MigrationRepository {

    private static final String DEFAULT_MIGRATION_KIND_NAME = "migration";

    private final Datastore datastore;
    private final String migrationKindName;

    public DatastoreMigrationRepository(Datastore datastore) {
        this(datastore, DEFAULT_MIGRATION_KIND_NAME);
    }

    public DatastoreMigrationRepository(Datastore datastore, String migrationKindName) {
        this.datastore = datastore;
        this.migrationKindName = migrationKindName;
    }

    @Override
    public Version latestAppliedVersion() {
        return appliedVersions()
                .stream()
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    @Override
    public void apply(Migration migration) {
        Key key = datastore.newKeyFactory()
                .setKind(migrationKindName)
                .newKey(migration.version().value());

        datastore.add(
                Entity.newBuilder(key)
                        .set("createdAt", Timestamp.now())
                        .set("className", migration.getClass().getSimpleName())
                        .build()
        );
    }

    @Override
    public List<Version> appliedVersions() {
        QueryResults<Entity> queryResults = datastore.run(
                Query.newEntityQueryBuilder()
                        .setKind(migrationKindName)
                        .build()
        );

        List<Version> appliedVersions = new ArrayList<>();
        while (queryResults.hasNext()) {
            Entity entity = queryResults.next();
            appliedVersions.add(new SimpleVersion(entity.getString("version")));
        }

        return appliedVersions
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
