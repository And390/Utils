package utils.iterators;

import java.util.Iterator;

/**
 * User: And390
 * Date: 17.01.15
 * Time: 15:13
 */
public class IterableJoin<T> implements Iterable<T>
{
    private Iterable<? extends T> iterable1;
    private Iterable<? extends T> iterable2;

    public IterableJoin(Iterable<? extends T> iterable1_, Iterable<? extends T> iterable2_)  {
        iterable1 = iterable1_;
        iterable2 = iterable2_;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new IteratorJoin<T> (iterable1.iterator(), iterable2.iterator());
    }
}