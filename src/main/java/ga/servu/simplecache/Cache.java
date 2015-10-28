package ga.servu.simplecache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Simple interface of Cache
 */
public interface Cache<K, V> {
	
	/**
	 * Get method which will try to resolve value from cache.<br>
	 * If it is not found, cache will fireup the Callable which will resolve value from actual source.<br>
	 * 
	 * @param key
	 * @param valueLoader
	 * @return
	 * @throws ExecutionException
	 */
	public V get(K key, Callable<V> valueLoader) throws ExecutionException;
	
}
