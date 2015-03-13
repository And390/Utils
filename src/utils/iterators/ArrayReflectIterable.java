package utils.iterators;

import java.util.Iterator;

/**
 * User: And390
 * Date: 28.04.14
 * Time: 21:13
 */
public class ArrayReflectIterable implements Iterable
{
    public Object array;
    public ArrayReflectIterable(Object array)  {  this.array = array;  }

    @Override
    public Iterator iterator()
    {
        return new ArrayReflectIterator (array);
    }
}
