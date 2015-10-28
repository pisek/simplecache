# SimpleCache

Concurrent Java Cache library for simple and fast usage.

## Usage

To use this cache library, you are given only 2 classes: SimpleCache interface and SimpleCacheBuilder class.
Nothing else is necessary.

### Using SimpleCacheBuilder to create cache

```java
Cache<Key, Value> sc = new SimpleCacheBuilder<Key, Value>()
			      .cleaningPeriod(10) // how often should cleaning-thread be executed (in seconds; 0 means never)
			      .timeToLive(2)  // how long the value in cache should remain valid (in seconds; 0 means for-ever)
			      .build();
```
Of course you can choose your own _Key_ and _Value_ class.

### Using SimpleCache.get method get/put value in cache

```java
SomeValue value = sc.get("key_one", new Callable<SomeValue>() {
				@Override
				public SomeValue call() throws Exception {
					return resolveSomeValue();
				}
			});
```
_resolveSomeValue()_ must return _SomeValue_ object, or null

### If you are not going to use SimpleCache instance, close it with close method

```java
sc.close();
```

## Example usage

```java
		SimpleCache<String, Integer> sc = new SimpleCacheBuilder<String, Integer>()
			      .cleaningPeriod(10)
			      .timeToLive(2)
			      .build();
			      
		try {
			Integer value = sc.get("key_one", new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					return new Random().nextInt();
				}
			});
			
			// do something with the value
			
		} catch (ExecutionException e) {
			// some exception was thrown during execution of Callable
		}
```
