package ga.servu.simplecache;

/**
 * A wrapper of the input element in cache<br>
 * Sets additional field - expiration date/time
 */
class Element<V> {

	private V value;
	private long expirationTime;

	Element(V value, int timeToLive) {
		this.value = value;
		this.expirationTime = System.currentTimeMillis() + timeToLive*1000L;
	}

	V getValue() {
		return value;
	}

	boolean isActive() {
		return System.currentTimeMillis() <= this.expirationTime;
	}

	boolean isActive(long now) {
		return now <= this.expirationTime;
	}

}
