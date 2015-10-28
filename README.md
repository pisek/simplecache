# SimpleCache

Concurrent Java Cache library for simple and fast usage.

## Usage

To use this cache library, you are given only 2 classes: _SimpleCache_ interface and _SimpleCacheBuilder_ class.
Nothing else matters:)

### Using _SimpleCacheBuilder_
In order to create new instance of _SimpleCache_ use _SimpleCacheBuilder_ and its builder-methods

```java
Cache<Key, Value> sc = new SimpleCacheBuilder<Key, Value>()
			      .cleaningPeriod(10) // how often should cleaning-thread be executed (in seconds; 0 means never)
			      .timeToLive(2)  // how long the value in cache should remain valid (in seconds; 0 means for-ever)
			      .build();
```
Of course you can choose your own _Key_ and _Value_ class.

### Using _SimpleCache.get()_
Use _SimpleCache.get()_ method to get/put value in cache

```java
SomeValue value = sc.get("key_one", new Callable<SomeValue>() {
				@Override
				public SomeValue call() throws Exception {
					return resolveSomeValue();
				}
			});
```
_resolveSomeValue()_ must return _SomeValue_ object, or null

### Closing _SimpleCache_ instance
If you will not use _SimpleCache_ instance anymore, close it with _close()_ method - it will kill, ongoing cleaner thread

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
			BigDecimal value = SC.get("key_one", new Callable<BigDecimal>() {
				@Override
				public BigDecimal call() throws Exception {
					return doSomeExpensiveComputations();
				}
			});
			
			// do something with the value
			
		} catch (ExecutionException e) {
			// some exception was thrown during execution of Callable
		}
	...
	SC.close();	// close cache after usage
```
