package utils.iterators;

import java.util.Iterator;

/**
 * User: And390
 * Date: 05.02.2012
 * Time: 15:32:06
 */
public class IteratorJoin<T> implements Iterator<T>
{
    private Iterator<? extends T> iterator1;
    private Iterator<? extends T> iterator2;

    public IteratorJoin (Iterator<? extends T> _iterator1, Iterator<? extends T> _iterator2)
    {
        iterator1=_iterator1;
        iterator2=_iterator2;
    }

    public boolean hasNext()
    {
        if (iterator1!=null)  if (iterator1.hasNext())  return true;
                              else  iterator1=null;
        return iterator2.hasNext();
    }

    public T next()
    {
        if (iterator1!=null)  if (iterator1.hasNext())  return iterator1.next();
                              else  iterator1=null;
        return iterator2.next();
    }

    public void remove()
    {
        if (iterator1!=null)  iterator1.remove();
        else  iterator2.remove();
    }
}
