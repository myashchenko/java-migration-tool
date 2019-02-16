package io.github.yashchenkon.migration;

import io.github.yashchenkon.migration.version.Version;

/**
 * @author Mykola Yashchenko
 */
public interface Migration {
    Version version();

    void execute();
}
