package go.wikipedi.wikipedigo.util;

/**
 * Created by E460 on 17/01/2017.
 */

public abstract class BaseRunnable<T> {

	T object;

	public abstract void run(T object);
}
