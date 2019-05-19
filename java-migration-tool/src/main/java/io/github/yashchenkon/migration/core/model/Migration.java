package io.github.yashchenkon.migration.core.model;

import io.github.yashchenkon.migration.core.version.Version;

/**
 * @author Mykola Yashchenko
 */
public interface Migration {
    Version version();

    void execute();
}
