package io.github.yashchenkon.migration.version;

/**
 * @author Mykola Yashchenko
 */
public class SimpleVersion implements Version {

    private final int value;

    public SimpleVersion(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
