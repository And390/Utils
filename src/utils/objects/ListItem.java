package utils.objects;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * And390 - 04.04.15.
 */
public class ListItem<T> implements Iterable<T>
{
    public ListItem<T> next;
    public T value;
    public ListItem()  {}
    public ListItem(T value_)  {  value = value_;  }
    public ListItem(T value_, ListItem<T> next_)  {  value=value_;  next=next_;  }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T> ()
        {
            ListItem<T> item = ListItem.this;

            @Override
            public boolean hasNext()  {
                return item!=null;
            }

            @Override
            public T next()  {
                if (item==null)  throw new NoSuchElementException ();
                T result = item.value;
                item = item.next;
                return result;
            }

            @Override
            public void remove()  {
                throw new UnsupportedOperationException ();
            }
        };
    }
}
