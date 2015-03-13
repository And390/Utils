package utils.iterators;

import java.util.Iterator;

/**
 * User: And390
 * Date: 20.12.14
 * Time: 8:37
 */
public class AloneIterator<T> implements Iterator<T>
{
    public T value;

    public AloneIterator(T value_)
    {
        if (value_==null)  throw new IllegalArgumentException ("Null is not supported");
        value=value_;
    }

    public boolean hasNext()
    {
        return value!=null;
    }

    public T next()
    {
        return value;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
