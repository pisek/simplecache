package ga.servu.simplecache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

public class SimpleCacheTest {
	
	@SuppressWarnings("deprecation")
	@Test
	public void testSimpleCacheMultiThreads() throws Exception {

		final int CLEANING_PERIOD = 0;
		final int THREAD_COUNT = 1000;
		final int THREAD_MAX_SLEEP = 1;

		//safest test-sets are:
		// TTL	TEST_TIME	TEST_CALLS
		// 3	5			2
		// 3	8			3
		// 3	11			4
		final int TTL = 3;
		final int TEST_TIME = 5;
		final int TEST_CALLS = 2;
		
		final SimpleCache<String, Date> sc = new SimpleCacheBuilder<String, Date>().cleaningPeriod(CLEANING_PERIOD).timeToLive(TTL).build();
		try {
			
			//caller
			class CCC implements Callable<Date> {
				public int I = 0;
				@Override
				public Date call() throws Exception {
					I++;
					return new Date();	//some expensive data computations here
				}
			};
			final CCC c = new CCC();
			
			//creating threads
			ArrayList<Thread> threads = new ArrayList<Thread>();
			for (int i = 0; i < THREAD_COUNT; i++) {
				Thread ttt = new Thread() {
					@Override
					public void run() {
						while (true) {
							try {
								sc.get("dupa1", c);
								Thread.sleep(new Random().nextInt(THREAD_MAX_SLEEP*1000));
							} catch (Exception e) {
								fail();
							}
						}
					}
				};
				ttt.start();
				threads.add(ttt);
			}
			
			//sleep
			Thread.sleep(TEST_TIME*1000);
			
			//stopping threads
			for (Thread ttt : threads) {
				ttt.stop();
			}
			
			assertEquals(TEST_CALLS, c.I);
			
		} finally {
			sc.close();
		}
		
	}
	
	@Test
	public void testSimpleCacheBasic() throws Exception {
		
		SimpleCache<String, Integer> sc = new SimpleCacheBuilder<String, Integer>().cleaningPeriod(0).timeToLive(2).build();
		try {
			
			Callable<Integer> c = new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					return new Random().nextInt();
				}
			};
			
			Integer i = sc.get("dupa1", c);
			assertEquals(i, sc.get("dupa1", c));
			
			Thread.sleep(3500);
			
			assertNotEquals(i, sc.get("dupa1", c));
			
		} finally {
			sc.close();
		}
		
	}
	
}
