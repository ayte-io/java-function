package io.ayte.utility.function;

import java.util.Comparator;

public class Comparators {
    public static final IntrinsicComparator INTRINSIC_COMPARATOR = new IntrinsicComparator();

    private Comparators() {}

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Comparator<T> intrinsic() {
        return INTRINSIC_COMPARATOR;
    }

    public static class IntrinsicComparator<T extends Comparable<T>> implements Comparator<T> {
        @Override
        public int compare(T left, T right) {
            return left.compareTo(right);
        }
    }
}
