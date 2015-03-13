package utils.iterators;

import java.util.Iterator;

/**
 * User: And390
 * Date: 17.01.15
 * Time: 17:01
 */
public class EmptyIterable<T> implements Iterable<T>
{
    public Iterator<T> iterator()
    {
        return new EmptyIterator<T> ();
    }
}
