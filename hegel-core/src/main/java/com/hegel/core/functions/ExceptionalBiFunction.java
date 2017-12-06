package com.hegel.core.functions;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface ExceptionalBiFunction<T, U, R, E extends Throwable> extends BiFunction<T, U, Exceptional<R, E>> {

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> R getOrThrowUnchecked(ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
                                                                T param1, U param2) {
        return exceptionalBiFunction.getOrThrowUnchecked(param1, param2);
    }

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> BiFunction<T, U, R> toUnchecked(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction) {
        return exceptionalBiFunction::getOrThrowUnchecked;
    }

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> Function<T, Function<U, ExceptionalSupplier<R, E>>> curry(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction) {
        return exceptionalBiFunction.curry();
    }

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> Function<T, Function<U, Supplier<R>>> curryUnchecked(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction) {
        return exceptionalBiFunction.curryUnchecked();
    }

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> ExceptionalSupplier<R, E> supply(
            ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction, T param1, U param2) {
        return exceptionalBiFunction.supply(param1, param2);
    }

    static <T, U, R, E extends Throwable> Supplier<R> supplyUnchecked(ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
                                                                      T param1, U param2) {
        return exceptionalBiFunction.supplyUnchecked(param1, param2);
    }

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> Function<U, ExceptionalSupplier<R, E>> partialFirst(ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
                                                                                              T param1) {
        return exceptionalBiFunction.partialFirst(param1);
    }

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> Function<U, Supplier<R>> partialFirstUnchecked(ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
                                                                                         T param1) {
        return exceptionalBiFunction.partialFirstUnchecked(param1);
    }

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> Function<T, ExceptionalSupplier<R, E>> partialSecond(ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
                                                                                               U param2) {
        return exceptionalBiFunction.partialSecond(param2);
    }

    @SuppressWarnings("unused")
    static <T, U, R, E extends Throwable> Function<T, Supplier<R>> partialSecondUnchecked(ExceptionalBiFunction<T, U, R, E> exceptionalBiFunction,
                                                                                          U param2) {
        return exceptionalBiFunction.partialSecondUnchecked(param2);
    }

    R reduce(T t, U u) throws E;

    @Override
    default Exceptional<R, E> apply(T t, U u) {
        try {
            return Exceptional.withValue(reduce(t, u));
        } catch (Throwable e) {
            //noinspection unchecked
            return Exceptional.withException((E) e);
        }
    }

    default R getOrThrowUnchecked(T param1, U param2) {
        return apply(param1, param2).getOrThrowUnchecked();
    }

    @SuppressWarnings("unused")
    default BiFunction<T, U, R> toUnchecked() {
        return this::getOrThrowUnchecked;
    }

    default Function<T, Function<U, ExceptionalSupplier<R, E>>> curry() {
        return param1 -> param2 -> () -> this.reduce(param1, param2);
    }

    default Function<T, Function<U, Supplier<R>>> curryUnchecked() {
        return param1 -> param2 -> () -> this.apply(param1, param2).getOrThrowUnchecked();
    }

    default ExceptionalSupplier<R, E> supply(T param1, U param2) {
        return () -> reduce(param1, param2);
    }

    default Supplier<R> supplyUnchecked(T param1, U param2) {
        return supply(param1, param2)::getOrThrowUnchecked;
    }

    default Function<U, ExceptionalSupplier<R, E>> partialFirst(T param1) {
        return param2 -> () -> reduce(param1, param2);
    }

    default Function<U, Supplier<R>> partialFirstUnchecked(T param1) {
        return param2 -> () -> getOrThrowUnchecked(param1, param2);
    }

    default Function<T, ExceptionalSupplier<R, E>> partialSecond(U param2) {
        return param1 -> () -> reduce(param1, param2);
    }

    default Function<T, Supplier<R>> partialSecondUnchecked(U param2) {
        return param1 -> () -> getOrThrowUnchecked(param1, param2);
    }
}
