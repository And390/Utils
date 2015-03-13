package utils.iterators;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * User: And390
 * Date: 05.10.14
 * Time: 6:02
 */
public class IteratorEnumeration<T> implements Enumeration<T>
{
    private final Iterator<T> iterator;

    public IteratorEnumeration(Iterator<T> iterator)
    {
        this.iterator = iterator;
    }

    public T nextElement()
    {
        return iterator.next();
    }

    public boolean hasMoreElements()
    {
        return iterator.hasNext();
    }
}
