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

## Licensing

MIT / Ayte Labs, 2018
