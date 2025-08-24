package edu.eci.arsw.primefinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

/**
 * Main class that coordinates the execution of multiple threads
 * to find prime numbers within a given range.
 */
public class Main {

	public static void main(String[] args) {
		int max = 30000000;
		int threadsNumber = 3;
		List<PrimeFinderThread> threads = new ArrayList<>();
		List<Integer> sharedPrimes = Collections.synchronizedList(new ArrayList<>());
		int numberPrimesThread = max / threadsNumber;

		for (int i = 0; i < threadsNumber; i++) {
			PrimeFinderThread pft;
			if (i == threadsNumber - 1) {
				pft = new PrimeFinderThread(numberPrimesThread * i, max, sharedPrimes);
			} else {
				pft = new PrimeFinderThread(numberPrimesThread * i, numberPrimesThread * (i + 1) - 1, sharedPrimes);
			}
			threads.add(pft);
		}

		for (PrimeFinderThread pft : threads) {
			pft.start();
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		System.out.println("Número de primos encontrados hasta el momento: " + sharedPrimes.size());
		System.out.print("Presione enter para continuar: ");
		(new Scanner(System.in)).nextLine();

		for (PrimeFinderThread pft : threads) {
			pft.resumeExecution();
		}

		synchronized (sharedPrimes) {
			sharedPrimes.notifyAll();
		}

		for (PrimeFinderThread pft : threads) {
			try {
				pft.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Total de primos encontrados: " + sharedPrimes.size());
	}
}