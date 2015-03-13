package utils.iterators;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: And390
 * Date: 05.10.14
 * Time: 14:43
 */
public class EnumerationIterator<T> implements Iterator<T>
{
    private Enumeration<T> enumeration;
    public EnumerationIterator(Enumeration<T> enumeration_)  {  enumeration=enumeration_;  }

    public boolean hasNext()
    {
        return enumeration.hasMoreElements();
    }

    public T next()
    {
        return enumeration.nextElement();
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
