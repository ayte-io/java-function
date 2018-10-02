package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.function.Function;

public class Comparators {
    public static final Intrinsic INTRINSIC_COMPARATOR = new Intrinsic();

    private Comparators() {}

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Comparator<T> intrinsic() {
        return INTRINSIC_COMPARATOR;
    }

    /**
     * Pretty much the same as Comparators.comparing(), but for Java 8 as well.
     */
    public static <T, V> Comparator<T> extracting(Function<T, V> extractor, Comparator<V> comparator) {
        return new Extracting<>(extractor, comparator);
    }

    public static <T, V extends Comparable<V>> Comparator<T> extracting(Function<T, V> extractor) {
        return extracting(extractor, intrinsic());
    }

    private static class Intrinsic<T extends Comparable<T>> implements Comparator<T> {
        @Override
        public int compare(T left, T right) {
            return left.compareTo(right);
        }
    }

    @RequiredArgsConstructor
    private static class Extracting<T, V> implements Comparator<T> {
        private final Function<T, V> extractor;
        private final Comparator<V> comparator;

        @Override
        public int compare(T left, T right) {
            return comparator.compare(extractor.apply(left), extractor.apply(right));
        }
    }
}
