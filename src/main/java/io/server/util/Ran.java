package io.server.util;
import java.security.SecureRandom;

/**
 * Used for random generation of numbers for probability events and random selection.
 * 
 * @author Patrick
 *
 */
public class Ran {
	public static SecureRandom gen = new SecureRandom();

	/**
	 * Random generator for hitting a certain probability.
	 * @param denom The 1/denom rate that this returns true.
	 * @return Whether the chance was hit.
	 */
	public static boolean hit(int denom) {
		return gen.nextInt(denom) == 0;
	}
	
	/**
	 * Random generator for hitting a certain probability.
	 * @param denom The percent rate that this returns true.
	 * @return Whether the chance was hit.
	 */
	public static boolean hitPercent(int percent) {
		return gen.nextInt(100) < percent;
	}
	
	/**
	 * Returns a random element of a generic array.
	 * @param array
	 * @return random element from the array
	 */
	public static <T> T ranElement(T[] array) {
		return array[gen.nextInt(array.length)];
	}
	
	/**
	 * Returns a random element of a int[] array.
	 * @param array
	 * @return random element from the array
	 */
	public static int ranElement(int[] array) {
		return array[gen.nextInt(array.length)];
	}
	
	/**
	 * Returns a random element of a boolean[] array.
	 * @param array
	 * @return random element from the array
	 */
	public static boolean ranElement(boolean[] array) {
		return array[gen.nextInt(array.length)];
	}
	
	/**
	 * Returns a random element of a double[] array.
	 * @param array
	 * @return random element from the array
	 */
	public static double ranElement(double[] array) {
		return array[gen.nextInt(array.length)];
	}
	
	/**
	 * Returns a random element of a float[] array.
	 * @param array
	 * @return random element from the array
	 */
	public static float ranElement(float[] array) {
		return array[gen.nextInt(array.length)];
	}

}
