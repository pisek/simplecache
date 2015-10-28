package ga.servu.simplecache;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class which cleans given cache in a loop.
 */
class RemoverThread<K, V> extends Thread {
	
	private ReentrantReadWriteLock rwl;
	private Map<K, Element<V>> cache;
	private long sleepMillis;
	private boolean close = false;
	
	/**
	 * Default constructor.
	 * @param rwl lock object which is used by other threads 
	 * @param cache given cache to clean
	 * @param ttl max time object may reside in cache before removal (in seconds)
	 * @param sleepPeriod how much time must this cleaner thread wait to run again (in seconds)
	 */
	public RemoverThread(ReentrantReadWriteLock rwl, Map<K, Element<V>> cache, int sleepPeriod) {
		this.rwl = rwl;
		this.cache = cache;
		this.sleepMillis = sleepPeriod*1000L;
	}
	
	@Override
	public void run() {
		while (!close) {
			try {
				rwl.writeLock().lock();
				
				long now = System.currentTimeMillis();
				Iterator<Entry<K, Element<V>>> it = cache.entrySet().iterator();
				while (it.hasNext()) {
					Entry<K, Element<V>> e = it.next();
					
					if (!e.getValue().isActive(now)) {
						it.remove();
					}
					
				}
				
			} finally {
				rwl.writeLock().unlock();
			}
			
			try {
				Thread.sleep(sleepMillis);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
		}
	};
	public void close() {
		this.close = true;
	}
};
