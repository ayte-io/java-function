package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@SuppressWarnings({"squid:S1452", "unused"})
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
     * are expected and we're trying to reduce boilerplate here.
     */
    public static <T> Predicate<T> constant(boolean value) {
        return value ? anyTrue() : anyFalse();
    }

    public static <T> Predicate<T> inverse(Predicate<T> predicate) {
        return new Inversion<>(predicate);
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return inverse(predicate);
    }

    public static <T> Predicate<T> conjunction(Predicate<? super T> left, Predicate<? super T> right) {
        return new Conjunction<>(left, right);
    }

    public static <T> Predicate<T> and(Predicate<? super T> left, Predicate<? super T> right) {
        return conjunction(left, right);
    }

    public static <T> Predicate<T> all(Predicate<? super T> left, Predicate<? super T> right) {
        return conjunction(left, right);
    }

    public static <T> Predicate<? super T> disjunction(Predicate<? super T> left, Predicate<? super T> right) {
        return new Disjunction<>(left, right);
    }

    public static <T> Predicate<? super T> or(Predicate<? super T> left, Predicate<? super T> right) {
        return disjunction(left, right);
    }

    public static <T> Predicate<? super T> any(Predicate<? super T> left, Predicate<? super T> right) {
        return disjunction(left, right);
    }

    public static <T> Predicate<T> equalTo(Object reference) {
        return new EqualTo<>(reference);
    }

    public static Predicate<String> equalToInAnyCase(String reference) {
        return new EqualToInAnyCase(reference);
    }

    public static <T> Predicate<T> elementOf(Collection<? super T> pool) {
        return new ElementOf<>(pool);
    }

    public static <T> Predicate<T> contains(Collection<? super T> pool) {
        return elementOf(pool);
    }

    public static <T> Predicate<T> keyOf(Map<? super T, ?> pool) {
        return new KeyOf<>(pool);
    }

    public static <T> Predicate<T> valueOf(Map<?, ? super T> pool) {
        return new ValueOf<>(pool);
    }

    public static <T> Predicate<T> greaterThan(T reference, Comparator<T> comparator) {
        return new GreaterThan<>(reference, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> greaterThan(T reference) {
        return greaterThan(reference, Comparators.intrinsic());
    }

    public static <T> Predicate<T> greaterThanOrEqualTo(T reference, Comparator<T> comparator) {
        return new GreaterThanOrEqualTo<>(reference, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> greaterThanOrEqualTo(T reference) {
        return greaterThanOrEqualTo(reference, Comparators.intrinsic());
    }

    public static <T> Predicate<T> lessThan(T reference, Comparator<T> comparator) {
        return new LessThan<>(reference, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> lessThan(T reference) {
        return lessThan(reference, Comparators.intrinsic());
    }

    public static <T> Predicate<T> lessThanOrEqualTo(T reference, Comparator<T> comparator) {
        return new LessThanOrEqualTo<>(reference, comparator);
    }

    public static <T extends Comparable<T>> Predicate<T> lessThanOrEqualTo(T reference) {
        return lessThanOrEqualTo(reference, Comparators.intrinsic());
    }

    public static <T> Predicate<T> within(
            T lower,
            T upper,
            boolean lowerInclusive,
            boolean upperInclusive,
            Comparator<T> comparator
    ) {
        Predicate<T> l = lowerInclusive ? greaterThanOrEqualTo(lower, comparator) : greaterThan(lower, comparator);
        Predicate<T> u = upperInclusive ? lessThanOrEqualTo(upper, comparator) : lessThan(upper, comparator);
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

    public static <T, S> Predicate<T> extracting(Function<T, S> extractor, Predicate<S> predicate) {
        return mapping(extractor, predicate);
    }

    public static <I> Predicate<? extends Collection<? extends I>> allMatch(Predicate<I> predicate) {
        return new AllMatch<>(predicate);
    }

    public static <I> Predicate<? extends Collection<? extends I>> anyMatch(Predicate<I> predicate) {
        return new AnyMatch<>(predicate);
    }

    public static <I> Predicate<? extends Collection<? extends I>> noneMatch(Predicate<I> predicate) {
        return new NoneMatch<>(predicate);
    }

    public static <I> Predicate<? extends Collection<? extends I>> allEqual(Object reference) {
        return allMatch(equalTo(reference));
    }

    public static <I> Predicate<? extends Collection<? extends I>> anyEqual(Object reference) {
        return anyMatch(equalTo(reference));
    }

    public static <I> Predicate<? extends Collection<? extends I>> noneEqual(Object reference) {
        return noneMatch(equalTo(reference));
    }

    private interface ExplicitPredicate<T> extends Predicate<T> {
        @Override
        default Predicate<T> and(Predicate<? super T> other) {
            return new Conjunction<>(this, other);
        }

        @Override
        default Predicate<T> or(Predicate<? super T> other) {
            return new Disjunction<>(this, other);
        }

        @Override
        default Predicate<T> negate() {
            return new Inversion<>(this);
        }
    }

    @RequiredArgsConstructor
    private static class Conjunction<T> implements ExplicitPredicate<T> {
        private final Predicate<? super T> left;
        private final Predicate<? super T> right;

        @Override
        public boolean test(T subject) {
            return left.test(subject) && right.test(subject);
        }
    }

    @RequiredArgsConstructor
    private static class Disjunction<T> implements ExplicitPredicate<T> {
        private final Predicate<? super T> left;
        private final Predicate<? super T> right;

        @Override
        public boolean test(T subject) {
            return left.test(subject) || right.test(subject);
        }
    }

    @RequiredArgsConstructor
    private static class Inversion<T> implements ExplicitPredicate<T> {
        private final Predicate<? super T> delegate;

        @Override
        public boolean test(T subject) {
            return !delegate.test(subject);
        }
    }

    private static class AnyTrue<T> implements ExplicitPredicate<T> {
        @Override
        public boolean test(T subject) {
            return true;
        }
    }

    private static class AnyFalse<T> implements ExplicitPredicate<T> {
        @Override
        public boolean test(T subject) {
            return false;
        }
    }

    @RequiredArgsConstructor
    private static class EqualTo<T> implements ExplicitPredicate<T> {
        private final Object reference;

        @Override
        public boolean test(T subject) {
            return Objects.equals(subject, reference);
        }
    }

    @RequiredArgsConstructor
    private static class EqualToInAnyCase implements ExplicitPredicate<String> {
        private final String reference;

        @Override
        public boolean test(String subject) {
            return subject != null && subject.equalsIgnoreCase(reference);
        }
    }

    @RequiredArgsConstructor
    private static class ElementOf<T> implements ExplicitPredicate<T> {
        private final Collection<? super T> pool;

        @Override
        public boolean test(T subject) {
            return pool.contains(subject);
        }
    }

    @RequiredArgsConstructor
    private static class KeyOf<T> implements ExplicitPredicate<T> {
        private final Map<? super T, ?> pool;

        @Override
        public boolean test(T subject) {
            return pool.containsKey(subject);
        }
    }

    @RequiredArgsConstructor
    private static class ValueOf<T> implements ExplicitPredicate<T> {
        private final Map<?, ? super T> pool;

        @Override
        public boolean test(T subject) {
            return pool.containsValue(subject);
        }
    }

    @RequiredArgsConstructor
    private static class GreaterThan<T> implements ExplicitPredicate<T> {
        private final T reference;
        private final Comparator<T> comparator;

        @Override
        public boolean test(T subject) {
            return comparator.compare(subject, reference) > 0;
        }
    }

    @RequiredArgsConstructor
    private static class GreaterThanOrEqualTo<T> implements ExplicitPredicate<T> {
        private final T reference;
        private final Comparator<T> comparator;

        @Override
        public boolean test(T subject) {
            return comparator.compare(subject, reference) >= 0;
        }
    }

    @RequiredArgsConstructor
    private static class LessThan<T> implements ExplicitPredicate<T> {
        private final T reference;
        private final Comparator<T> comparator;

        @Override
        public boolean test(T subject) {
            return comparator.compare(subject, reference) < 0;
        }
    }

    @RequiredArgsConstructor
    private static class LessThanOrEqualTo<T> implements ExplicitPredicate<T> {
        private final T reference;
        private final Comparator<T> comparator;

        @Override
        public boolean test(T subject) {
            return comparator.compare(subject, reference) <= 0;
        }
    }

    @RequiredArgsConstructor
    private static class Within<T> implements ExplicitPredicate<T> {
        private final Predicate<T> lower;
        private final Predicate<T> upper;

        @Override
        public boolean test(T subject) {
            return lower.test(subject) && upper.test(subject);
        }
    }

    @RequiredArgsConstructor
    private static class Mapping<T, S> implements ExplicitPredicate<T> {
        private final Function<T, S> mapper;
        private final Predicate<S> predicate;

        @Override
        public boolean test(T subject) {
            return predicate.test(mapper.apply(subject));
        }
    }

    @RequiredArgsConstructor
    private static class AllMatch<I, C extends Collection<I>> implements ExplicitPredicate<C> {
        private final Predicate<I> predicate;

        @Override
        public boolean test(C subject) {
            return subject.stream().allMatch(predicate);
        }
    }

    @RequiredArgsConstructor
    private static class AnyMatch<I, C extends Collection<I>> implements ExplicitPredicate<C> {
        private final Predicate<I> predicate;

        @Override
        public boolean test(C subject) {
            return subject.stream().anyMatch(predicate);
        }
    }

    @RequiredArgsConstructor
    private static class NoneMatch<I, C extends Collection<I>> implements ExplicitPredicate<C> {
        private final Predicate<I> predicate;

        @Override
        public boolean test(C subject) {
            return subject.stream().noneMatch(predicate);
        }
    }

    public static class Operations {
        private Operations() {}

        @SuppressWarnings("unchecked")
        public static <T> BinaryOperator<Predicate<? super T>> conjunction() {
            return (BinaryOperator<Predicate<? super T>>) ConjunctionOperator.INSTANCE;
        }

        public static <T> BinaryOperator<Predicate<? super T>> and() {
            return conjunction();
        }

        public static <T> BinaryOperator<Predicate<? super T>> all() {
            return conjunction();
        }

        @SuppressWarnings("unchecked")
        public static <T> BinaryOperator<Predicate<? super T>> disjunction() {
            return (BinaryOperator<Predicate<? super T>>) DisjunctionOperator.INSTANCE;
        }

        public static <T> BinaryOperator<Predicate<? super T>> or() {
            return disjunction();
        }

        public static <T> BinaryOperator<Predicate<? super T>> any() {
            return disjunction();
        }

        @SuppressWarnings("unchecked")
        public static <T> UnaryOperator<Predicate<T>> inverse() {
            return (UnaryOperator<Predicate<T>>) InversionOperator.INSTANCE;
        }

        public static <T> UnaryOperator<Predicate<T>> not() {
            return inverse();
        }

        private static class ConjunctionOperator<T> implements BinaryOperator<Predicate<? super T>> {
            public static final ConjunctionOperator INSTANCE = new ConjunctionOperator<>();

            @Override
            public Predicate<? super T> apply(Predicate<? super T> left, Predicate<? super T> right) {
                return new Conjunction<>(left, right);
            }
        }

        private static class DisjunctionOperator<T> implements BinaryOperator<Predicate<? super T>> {
            public static final DisjunctionOperator INSTANCE = new DisjunctionOperator<>();

            @Override
            public Predicate<? super T> apply(Predicate<? super T> left, Predicate<? super T> right) {
                return new Disjunction<>(left, right);
            }
        }

        private static class InversionOperator<T> implements UnaryOperator<Predicate<T>> {
            public static final InversionOperator INSTANCE = new InversionOperator<>();

            @Override
            public Predicate<T> apply(Predicate<T> predicate) {
                return new Inversion<>(predicate);
            }
        }
    }
}
