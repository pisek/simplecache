package ga.servu.simplecache;

import java.io.Closeable;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation of fully Concurrent Cache - multiple threads may access/write to it.
 */
class SimpleCacheImpl<K, V> implements SimpleCache<K, V>, Closeable {
	
	private final ReentrantReadWriteLock RWL = new ReentrantReadWriteLock();
	private final ExecutorService POOL = Executors.newFixedThreadPool(1);

	private HashMap<K, Element<V>> CACHE = new HashMap<K, Element<V>>();
	private RemoverThread<K, V> REMOVER;
	
	private int timeToLive;
	private int cleaningPeriod;
	private boolean useRemover = true;

	SimpleCacheImpl() {}

	void init() {
		if (useRemover) {
			REMOVER = new RemoverThread<K, V> (RWL, CACHE, cleaningPeriod);
			REMOVER.start();
		}
	}
	
	@Override
	public void close() {
		if (useRemover) {
			REMOVER.close();
			try {
				REMOVER.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@Override
	public V get(K key, Callable<V> valueLoader) throws ExecutionException {
		
		try {
			RWL.readLock().lock();
			Element<V> e = CACHE.get(key);
			if (e == null || !e.isActive()) {

				try {
					RWL.readLock().unlock();
					RWL.writeLock().lock();
					e = CACHE.get(key);
					if (e == null || !e.isActive()) {
						V v = POOL.submit(valueLoader).get();
						e = new Element<V>(v, timeToLive);
						CACHE.put(key, e);
					}
				} catch (InterruptedException ex) {
					throw new ExecutionException(ex);
				} finally {
					RWL.readLock().lock();
					RWL.writeLock().unlock();
				}
				
			}
			return e.getValue();
		} finally {
			RWL.readLock().unlock();
		}
		
	}

	void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
		this.useRemover = timeToLive<=0 ? false : this.useRemover;
	}

	void setCleaningPeriod(int cleaningPeriod) {
		this.cleaningPeriod = cleaningPeriod;
		this.useRemover = cleaningPeriod<=0 ? false : this.useRemover;
	}

}
