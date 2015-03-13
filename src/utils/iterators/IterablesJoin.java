package utils.iterators;

import java.util.Iterator;

/**
 * User: And390
 * Date: 17.01.15
 * Time: 16:54
 */
public class IterablesJoin<T> implements Iterable<T>
{
    private Iterable<T>[] iterables;

    public IterablesJoin(Iterable<T>[] iterables_)  {  iterables=iterables_;  }

    public Iterator<T> iterator()
    {
        @SuppressWarnings("unchecked")
        Iterator<T>[] iterators = new Iterator [iterables.length];
        for (int i=0; i<iterators.length; i++)  iterators[i] = iterables[i].iterator();
        return new IteratorsJoin<T> (iterators);
    }
}
