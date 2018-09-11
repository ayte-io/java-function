package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Predicates {
    public static final AnyTrue ANY_TRUE = new AnyTrue();
    public static final AnyFalse ANY_FALSE = new AnyFalse();

    private Predicates() {}

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> anyTrue() {
        return ANY_TRUE;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> anyFalse() {
        return ANY_FALSE;
    }

    /**
     * This method exists because ternary operations like
     * {@code condition ? Predicates.anyTrue() : Predicates.anyFalse()}
     * are expected.
     */
    public static <T> Predicate<T> anyFixed(boolean value) {
        return value ? anyTrue() : anyFalse();
    }

    public static <T> Predicate<T> equalTo(Object reference) {
        return new EqualTo<>(reference);
    }

    public static Predicate<String> equalToInAnyCase(String reference) {
        return new EqualToInAnyCase(reference);
    }

    public static <T> Predicate<T> isElementOf(Collection<? super T> pool) {
        return new IsElementOf<>(pool);
    }

    public static <T> Predicate<T> isGreaterThan(T reference, Comparator<T> comparator) {
        return new IsGreaterThan<>(reference, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> isGreaterThan(T reference) {
        return isGreaterThan(reference, Comparators.intrinsic());
    }

    public static <T> Predicate<T> isGreaterThanOrEqualTo(T reference, Comparator<T> comparator) {
        return new IsGreaterThanOrEqualTo<>(reference, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> isGreaterOrEqualThan(T reference) {
        return isGreaterThanOrEqualTo(reference, Comparators.intrinsic());
    }

    public static <T> Predicate<T> isLessThan(T reference, Comparator<T> comparator) {
        return new IsLessThan<>(reference, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> isLessThan(T reference) {
        return isLessThan(reference, Comparators.intrinsic());
    }

    public static <T> Predicate<T> isLessThanOrEqualTo(T reference, Comparator<T> comparator) {
        return new IsLessThanOrEqualTo<>(reference, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> isLessThanOrEqualTo(T reference) {
        return isLessThanOrEqualTo(reference, Comparators.intrinsic());
    }

    public static <T> Predicate<T> within(
            T lower,
            T upper,
            boolean lowerInclusive,
            boolean upperInclusive,
            Comparator<T> comparator
    ) {
        Predicate<T> l = lowerInclusive ? isGreaterThanOrEqualTo(lower, comparator) : isGreaterThan(lower, comparator);
        Predicate<T> u = upperInclusive ? isLessThanOrEqualTo(upper, comparator) : isLessThan(upper, comparator);
        return new Within<>(l, u);
    }

    public static <T extends Comparable<T>> Predicate<T> within(
            T lower,
            T upper,
            boolean lowerInclusive,
            boolean upperInclusive
    ) {
        return within(lower, upper, lowerInclusive, upperInclusive, Comparators.intrinsic());
    }

    public static <T> Predicate<T> withinInclusive(T lower, T upper, Comparator<T> comparator) {
        return within(lower, upper, true, true, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> withinInclusive(T lower, T upper) {
        return withinInclusive(lower, upper, Comparators.intrinsic());
    }

    public static <T> Predicate<T> withinExclusive(T lower, T upper, Comparator<T> comparator) {
        return within(lower, upper, false, false, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> withinExclusive(T lower, T upper) {
        return withinExclusive(lower, upper, Comparators.intrinsic());
    }

    public static <T> Predicate<T> withinLowerExclusive(T lower, T upper, Comparator<T> comparator) {
        return within(lower, upper, false, true, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> withinLowerExclusive(T lower, T upper) {
        return withinLowerExclusive(lower, upper, Comparators.intrinsic());
    }

    public static <T> Predicate<T> withinLowerInclusive(T lower, T upper, Comparator<T> comparator) {
        return withinUpperExclusive(lower, upper, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> withinLowerInclusive(T lower, T upper) {
        return withinLowerInclusive(lower, upper, Comparators.intrinsic());
    }

    public static <T> Predicate<T> withinUpperExclusive(T lower, T upper, Comparator<T> comparator) {
        return within(lower, upper, true, false, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> withinUpperExclusive(T lower, T upper) {
        return withinUpperExclusive(lower, upper, Comparators.intrinsic());
    }

    public static <T> Predicate<T> withinUpperInclusive(T lower, T upper, Comparator<T> comparator) {
        return withinLowerExclusive(lower, upper, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> withinUpperInclusive(T lower, T upper) {
        return withinUpperInclusive(lower, upper, Comparators.intrinsic());
    }

    public static <T, S> Predicate<T> mapping(Function<T, S> mapper, Predicate<S> predicate) {
        return new Mapping<>(mapper, predicate);
    }

    private static class AnyTrue<T> implements Predicate<T> {
        @Override
        public boolean test(T subject) {
            return true;
        }
    }

    private static class AnyFalse<T> implements Predicate<T> {
        @Override
        public boolean test(T subject) {
            return false;
        }
    }

    @RequiredArgsConstructor
    private static class EqualTo<T> implements Predicate<T> {
        private final Object reference;

        @Override
        public boolean test(T subject) {
            return Objects.equals(subject, reference);
        }
    }

    @RequiredArgsConstructor
    private static class EqualToInAnyCase implements Predicate<String> {
        private final String reference;

        @Override
        public boolean test(String subject) {
            return subject != null && subject.equalsIgnoreCase(reference);
        }
    }

    @RequiredArgsConstructor
    private static class IsElementOf<T> implements Predicate<T> {
        private final Collection<? super T> pool;

        @Override
        public boolean test(T subject) {
            return pool.contains(subject);
        }
    }

    @RequiredArgsConstructor
    private static class IsGreaterThan<T> implements Predicate<T> {
        private final T reference;
        private final Comparator<T> comparator;

        @Override
        public boolean test(T subject) {
            return comparator.compare(subject, reference) > 0;
        }
    }

    @RequiredArgsConstructor
    private static class IsGreaterThanOrEqualTo<T> implements Predicate<T> {
        private final T reference;
        private final Comparator<T> comparator;

        @Override
        public boolean test(T subject) {
            return comparator.compare(subject, reference) >= 0;
        }
    }

    @RequiredArgsConstructor
    private static class IsLessThan<T> implements Predicate<T> {
        private final T reference;
        private final Comparator<T> comparator;

        @Override
        public boolean test(T subject) {
            return comparator.compare(subject, reference) < 0;
        }
    }

    @RequiredArgsConstructor
    private static class IsLessThanOrEqualTo<T> implements Predicate<T> {
        private final T reference;
        private final Comparator<T> comparator;

        @Override
        public boolean test(T subject) {
            return comparator.compare(subject, reference) <= 0;
        }
    }

    @RequiredArgsConstructor
    private static class Within<T> implements Predicate<T> {
        private final Predicate<T> lower;
        private final Predicate<T> upper;

        @Override
        public boolean test(T subject) {
            return lower.test(subject) && upper.test(subject);
        }
    }

    @RequiredArgsConstructor
    private static class Mapping<T, S> implements Predicate<T> {
        private final Function<T, S> mapper;
        private final Predicate<S> predicate;

        @Override
        public boolean test(T subject) {
            return predicate.test(mapper.apply(subject));
        }
    }
}
