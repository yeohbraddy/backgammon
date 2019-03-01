package game;

/**
 * Stack interface.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * 
 */
public interface StackInterface<E> {
	boolean isEmpty();
	int size();
	E top();
	void push(E e);
	E pop();
	void clear();
}
