package io.github.yashchenkon.migration.version;

/**
 * @author Mykola Yashchenko
 */
public interface Version extends Comparable<Version> {
    int value();

    default int compareTo(Version o) {
        return value() - o.value();
    }
}
