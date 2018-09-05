package io.ayte.utility.function;

import java.util.function.BinaryOperator;

public class Functions {
    public static final WithLeft WITH_LEFT = new WithLeft();
    public static final WithRight WITH_RIGHT = new WithRight();

    private Functions() {}

    @SuppressWarnings("unchecked")
    public static <T> BinaryOperator<T> withLeft() {
        return WITH_LEFT;
    }

    @SuppressWarnings("unchecked")
    public static <T> BinaryOperator<T> withRight() {
        return WITH_RIGHT;
    }

    public static class WithLeft<T> implements BinaryOperator<T> {
        @Override
        public T apply(T value, T any) {
            return value;
        }
    }

    public static class WithRight<T> implements BinaryOperator<T> {
        @Override
        public T apply(T any, T value) {
            return value;
        }
    }
}
