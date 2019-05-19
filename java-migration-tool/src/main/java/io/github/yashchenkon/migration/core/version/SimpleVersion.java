package io.github.yashchenkon.migration.core.version;

/**
 * @author Mykola Yashchenko
 */
public class SimpleVersion implements Version {

    private final String value;

    public SimpleVersion(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
