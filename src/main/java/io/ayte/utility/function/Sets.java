package io.ayte.utility.function;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Sets {
    private Sets() {}

    @SafeVarargs // Hey users deal with your data on your own haha
    public static <T> Set<T> mutable(T... elements) {
        return mutable(Arrays.asList(elements));
    }

    public static <T> Set<T> mutable(Collection<? extends T> elements) {
        return new HashSet<>(elements);
    }

    /**
     * Same as {@code Set.of()} in java 9+, but java 8 is still in play
     * in many places.
     */
    @SafeVarargs
    public static <T> Set<T> of(T... elements) {
        return immutable(elements);
    }

    /**
     * Same as {@code Set.copyOf()} in java 9+, added for java 8
     * compatibility.
     */
    public static <T> Set<T> copyOf(Collection<? extends T> subject) {
        return immutable(subject);
    }

    @SafeVarargs
    public static <T> Set<T> immutable(T... elements) {
        return Collections.unmodifiableSet(mutable(elements));
    }

    public static <T> Set<T> immutable(Collection<? extends T> elements) {
        return Collections.unmodifiableSet(mutable(elements));
    }
}
