# SimpleCache

__Fully Concurrent__ Java Cache library for simple and fast usage.

## Usage

To use this cache library, you are given only 2 classes: 
* `SimpleCache` interface 
* `SimpleCacheBuilder` class

Nothing else matters:)

### Using `SimpleCacheBuilder`
In order to create new instance of `SimpleCache` use `SimpleCacheBuilder` and its builder-methods

```java
Cache<Key, Value> sc = new SimpleCacheBuilder<Key, Value>()
			      .cleaningPeriod(10) // how often should cleaning-thread be executed (in seconds; 0 means never)
			      .timeToLive(2)  // how long the value in cache should remain valid (in seconds; 0 means for-ever)
			      .build();
```
Of course you can choose your own `Key` and `Value` class.

### Using `SimpleCache.get()`
Use `SimpleCache.get()` method to get/put value in cache

```java
SomeValue value = sc.get("key_one", () -> resolveSomeValue();
```

or using older Java versions:
```java
SomeValue value = sc.get("key_one", new Callable<SomeValue>() {
				@Override
				public SomeValue call() throws Exception {
					return resolveSomeValue();
				}
			});
```
Method `resolveSomeValue()` must return `SomeValue` object, or null

### Closing `SimpleCache` instance
If you will not use `SimpleCache` instance anymore, close it with `close()` method - it will kill, ongoing cleaner thread

```java
sc.close();
```

## Example usage

```java
	private static final SimpleCache<String, BigDecimal> SC = new SimpleCacheBuilder<String, BigDecimal>()
		      .cleaningPeriod(10)
		      .timeToLive(2)
		      .build();
	...
		try {
			BigDecimal value = SC.get("key_one", () -> doSomeExpensiveComputations();
			
			// do something with the value
			
		} catch (ExecutionException e) {
			// some exception was thrown during execution of Callable
		}
	...
	SC.close();	// close cache after usage
```
