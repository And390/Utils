package utils.iterators;

import java.util.Iterator;

/**
 * User: And390
 * Date: 05.02.2012
 * Time: 15:49:01
 */
public class IteratorIterable<T> implements Iterable<T>
{
    private Iterator<T> _iterator;
    public IteratorIterable (Iterator<T> iterator)  {  _iterator=iterator;  }
    public Iterator<T> iterator()  {  return _iterator;  }
}
