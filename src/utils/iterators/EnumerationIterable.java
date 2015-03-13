package utils.iterators;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * User: And390
 * Date: 05.10.14
 * Time: 14:41
 */
public class EnumerationIterable<T> implements Iterable<T>
{
    private Enumeration<T> enumeration;
    public EnumerationIterable(Enumeration<T> enumeration_)  {  enumeration=enumeration_;  }

    @Override
    public Iterator<T> iterator()  {  return new EnumerationIterator<T>(enumeration);  }
}
