# Ayte :: Utility :: Function

Simple collection of commonly reused functions and functional 
interfaces for our internal needs and to eliminate common boilerplate 
such as:

```
// Stream<Optional<T>>.map(Functions.orNull())
Stream<Optional<T>>.map(optional -> optional.orElse(null))
// Stream<Collection<I>>.map(Functions.toSet(mapper))
Stream<Collection<I>>.map(collection -> collection.map(mapper).collect(Collectors.toSet()))
// Stream<Collection<I>>.filter(Functions.allMatch(predicate))
Stream<Collection<I>>.filter(collection -> collection.stream().allMatch(predicate))
```

and more simpler things like:

```
// Predicates.anyTrue()
(Predicate<T>) any -> true
// Functions.withLeft()
(BiOperator<I>) (a, b) -> a
```

since they will be wrapped in named classes with arguments to simplify
debug (it's always easier to understand things when it's called AnyTrue
instead of lambda&dollars&hashcode). 

Subject for fast pace development and breaking changes until 1.0.0. No
explicit documentation is needed, just check the classes.

## Conventions

- Remove all casts for end user where possible.
- Expose all factory methods as `fromX`, and, if type erasure and 
semantics allow that, as overloaded `from`.
- Interfaces do not hold anything but interface methods, all static
methods and instances are placed in `Xs` class for `X` interface 
(e.g. `AsyncTask` and `AsyncTasks`).
- All functions and operations offered by static methods are expressed
as separate private classes, so entire composition may be readable from
debugger or even from `.toString()` call. Such private classes should 
belong to `Xs` classes, unless they're tightly coupled with interface
(e.g. are used in default methods).
- Aim for getting rid of long and unpleasant everyday lambdas.
- Be reasonable about performance and evade extra calls where possible.

## Licensing

MIT / Ayte Labs, 2018
