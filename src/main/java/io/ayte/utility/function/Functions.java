package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1452")
public class Functions {
    public static final WithLeft WITH_LEFT = new WithLeft();
    public static final WithRight WITH_RIGHT = new WithRight();
    public static final OrElse OR_NULL = new OrElse<>(null);

    private Functions() {}

    @SuppressWarnings("unchecked")
    public static <T> BinaryOperator<T> withLeft() {
        return WITH_LEFT;
    }

    @SuppressWarnings("unchecked")
    public static <T> BinaryOperator<T> withRight() {
        return WITH_RIGHT;
    }

    public static <I, M, O> Function<Collection<? extends I>, O> stream(Function<I, M> mapper, Collector<M, ?, O> collector) {
        return new StreamMapper<>(mapper, collector);
    }

    public static <I, O> Function<Collection<? extends I>, List<O>> toList(Function<I, O> mapper) {
        return stream(mapper, Collectors.toList());
    }

    public static <I, O> Function<Collection<? extends I>, Set<O>> toSet(Function<I, O> mapper) {
        return stream(mapper, Collectors.toSet());
    }

    public static <I, O> Function<Collection<? extends I>, O> collect(Collector<I, ?, O> collector) {
        return new StreamCollector<>(collector);
    }

    public static <I> Function<Optional<I>, I> orElse(I value) {
        return new OrElse<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <I> Function<Optional<I>, I> orNull() {
        return OR_NULL;
    }

    public static <I> Function<Optional<I>, I> orElseGet(Supplier<I> supplier) {
        return new OrElseGet<>(supplier);
    }

    private static class WithLeft<T> implements BinaryOperator<T> {
        @Override
        public T apply(T value, T any) {
            return value;
        }
    }

    private static class WithRight<T> implements BinaryOperator<T> {
        @Override
        public T apply(T any, T value) {
            return value;
        }
    }

    @RequiredArgsConstructor
    private static class StreamMapper<I, M, O> implements Function<Collection<? extends I>, O> {
        private final Function<I, M> mapper;
        private final Collector<M, ?, O> collector;

        @Override
        public O apply(Collection<? extends I> subject) {
            return subject.stream().map(mapper).collect(collector);
        }
    }

    @RequiredArgsConstructor
    private static class StreamCollector<I, O> implements Function<Collection<? extends I>, O> {
        private final Collector<I, ?, O> collector;

        @Override
        public O apply(Collection<? extends I> subject) {
            return subject.stream().collect(collector);
        }
    }

    @RequiredArgsConstructor
    private static class OrElse<I> implements Function<Optional<I>, I> {
        private final I value;

        @Override
        public I apply(Optional<I> subject) {
            return subject.orElse(value);
        }
    }

    @RequiredArgsConstructor
    private static class OrElseGet<I> implements Function<Optional<I>, I> {
        private final Supplier<I> supplier;

        @Override
        public I apply(Optional<I> subject) {
            return subject.orElseGet(supplier);
        }
    }
}
