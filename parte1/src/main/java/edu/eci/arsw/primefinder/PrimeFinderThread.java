package edu.eci.arsw.primefinder;
import java.util.List;

/**
 * Thread implementation that searches for prime numbers
 * within a given range and stores them in a shared list.
 */
public class PrimeFinderThread extends Thread {

	private final List<Integer> sharedPrimes;
	int a;
	int b;
	boolean stop;

	/**
	 * Constructor for PrimeFinderThread.
	 * @param a start of the range (inclusive)
	 * @param b end of the range (inclusive)
	 * @param sharedPrimes shared list where prime numbers will be stored
	 */
	public PrimeFinderThread(int a, int b, List<Integer> sharedPrimes) {
		super();
		this.a = a;
		this.b = b;
		this.stop = false;
		this.sharedPrimes = sharedPrimes;
	}

	/**
	 * Main execution of the thread. Iterates over the assigned range,
	 * checks for primes, and adds them to the shared list.
	 */
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		for (int i = a; i <= b; i++) {
			try {
				this.checkPause(startTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
				return;
			}
			if (isPrime(i)) {
				this.addPrime(i);
			}
		}
	}
	/**
	 * Adds a prime number to the shared list in a thread-safe way.
	 *
	 * @param primeNumber the prime number to be added
	 */
	public void addPrime(int primeNumber) {
		synchronized (sharedPrimes) {
			sharedPrimes.add(primeNumber);
			System.out.println(Thread.currentThread().getName() + " :" + primeNumber);
		}
	}

	/**
	 * Makes the thread wait if 5 seconds have passed
	 * and execution has not yet been resumed.
	 *
	 * @param startTime the start time of the thread
	 * @throws InterruptedException if the thread is interrupted while waiting
	 */
	public void checkPause(long startTime) throws InterruptedException {
		synchronized (sharedPrimes) {
			while (!stop && System.currentTimeMillis() - startTime >= 5000) {
				sharedPrimes.wait();
			}
		}
	}

	/**
	 * Resumes the execution of the thread after it was paused.
	 */
	public void resumeExecution() {
		this.stop = true;
	}

	/**
	 * Checks if a number is prime.
	 *
	 * @param n number to check
	 * @return true if the number is prime, false otherwise
	 */
	boolean isPrime(int n) {
		if (n%2==0) return false;
		for(int i=3;i*i<=n;i+=2) {
			if(n%i==0)
				return false;
		}
		return true;
	}
}