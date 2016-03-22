package ga.servu.simplecache;

import java.io.Closeable;
import java.util.concurrent.Callable;

/**
 * Simple interface of Cache
 */
public interface SimpleCache<K, V> extends Closeable {
	
	/**
	 * Get method which will try to resolve value from cache.<br>
	 * If it is not found, cache will fireup the {@code Callable} which will resolve value from actual source.<br>
	 * The value resolved from {@code Callable} is then put in cache using given key.
	 * 
	 * @param key key to search for in cache
	 * @param valueLoader {@code Callable} to fire up if given key is not found; it is then put in cache with given key
	 * @return requested value
	 * @throws Exception thrown by {@code Callable}
	 */
	public V get(K key, Callable<V> valueLoader) throws Exception;
	
	/**
	 * Method which closes all instances of inner threads like cleaner-thread etc.<br>
	 * Should be invoked upon not using cache anymore and before creating new cache instance etc.
	 */
	@Override
	public void close();
	
}
