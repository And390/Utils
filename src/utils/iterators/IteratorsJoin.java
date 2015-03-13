package utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: And390
 * Date: 17.01.15
 * Time: 16:39
 */
public class IteratorsJoin<T> implements Iterator<T>
{
    private Iterator<? extends T>[] iterators;  //must be not empty
    private int index;

    public IteratorsJoin (Iterator<? extends T>[] iterators_)
    {
        if (iterators_.length==0)  throw new IllegalArgumentException ("No arguments passed to IteratorsJoin");
        iterators = iterators_;
        index = 0;
    }

    public boolean hasNext()
    {
        for (;;)
            if (iterators[index].hasNext())  return true;
            else if (index+1==iterators.length)  return false;
            else  index++;
    }

    public T next()
    {
        while (!iterators[index].hasNext() && index<iterators.length+1)  index++;
        return iterators[index].next();
    }

    public void remove()
    {
        iterators[index].remove();
    }
}

