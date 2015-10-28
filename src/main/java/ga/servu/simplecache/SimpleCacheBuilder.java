package ga.servu.simplecache;

public class SimpleCacheBuilder<K, V> {
	
	private static final int DEFAULT_TTL = 0;
	private static final int DEFAULT_CLEANING_PERIOD = 120;
	
	private int timeToLive = DEFAULT_TTL;
	private int cleaningPeriod = DEFAULT_CLEANING_PERIOD;
	
	/**
	 * TTL of the cache item in seconds (default 0 - always exists)
	 * @param timeToLive
	 * @return
	 */
	public SimpleCacheBuilder<K, V> timeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
		return this;
	}
	
	/**
	 * Cleaning period in seconds (default 120 seconds)
	 * @param cleaningPeriod
	 * @return
	 */
	public SimpleCacheBuilder<K, V> cleaningPeriod(int cleaningPeriod) {
		this.cleaningPeriod = cleaningPeriod;
		return this;
	}
	
	/**
	 * Builds {@code SimpleCache} using set properties
	 * @return {@code SimpleCache} instance
	 */
	public SimpleCache<K, V> build() {
		SimpleCacheImpl<K, V> simpleCache = new SimpleCacheImpl<K, V>();
		simpleCache.setTimeToLive(timeToLive);
		simpleCache.setCleaningPeriod(cleaningPeriod);
		simpleCache.init();
		return simpleCache;
	}
	
}
