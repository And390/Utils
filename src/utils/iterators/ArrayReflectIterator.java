package utils.iterators;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: And390
 * Date: 28.04.14
 * Time: 21:13
 */
public class ArrayReflectIterator implements Iterator
{
    public Object array;
    public int index = 0;
    public int length;
    public ArrayReflectIterator(Object array)  {  this.array = array;  length = Array.getLength(array);  }

    public boolean hasNext()
    {
        return index!=length;
    }

    public Object next()
    {
        if (index==length) throw new NoSuchElementException();
        return Array.get(array, index++);
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
