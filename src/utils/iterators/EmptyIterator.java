package utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: And390
 * Date: 17.01.15
 * Time: 16:59
 */
public class EmptyIterator<T> implements Iterator<T>
{
    public boolean hasNext()
    {
        return false;
    }

    public T next()
    {
        throw new NoSuchElementException ();
    }

    public void remove()
    {
        throw new UnsupportedOperationException ();
    }
}
