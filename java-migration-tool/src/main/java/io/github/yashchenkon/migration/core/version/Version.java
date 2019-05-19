package io.github.yashchenkon.migration.core.version;

/**
 * @author Mykola Yashchenko
 */
public interface Version extends Comparable<Version> {
    String value();

    default int compareTo(Version o) {
        return value().compareTo(o.value());
    }
}
