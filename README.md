# Ayte :: Utility :: Function

Simple collection of commonly reused functions for internal needs and
to eliminate common boilerplate such as:

```
// Functions.orNull()
Stream<Optional<T>>.map(optional -> optional.orElse(null))
// Functions.toList(mapper)
Stream<Collection<I>>.map(collection -> collection.map(mapper).collect(Collectors.toSet()))
// Functions.allMatch(predicate)
Stream<Collection<I>>.filter(collection -> collection.stream().allMatch(predicate))
```

and more simpler things like:

```
// Predicates.anyTrue()
(Predicate<?>) any -> true
// Functions.withLeft()
(BiOperator<I>) (a, b) -> a
```

since they will be wrapped in named classes with arguments to simplify
debug. 

Subject for fast pace development and breaking changes until 1.0.0. No
explicit documentation is needed, just check the classes.

## Licensing

MIT / Ayte Labs, 2018
