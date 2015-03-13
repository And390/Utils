package utils.iterators;

import java.util.Iterator;

/**
 * User: And390
 * Date: 20.12.14
 * Time: 8:40
 */
public class AloneIterable<T> implements Iterable<T>
{
    public T value;

    public AloneIterable(T value_)
    {
        value=value_;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new AloneIterator<T> (value);
    }
}
