package utils.iterators;

import java.util.Iterator;

/**
 * Объекту достаточно реализовать метод getNext() (и соответственно интерфейс NextIterator.Item),
 * чтобы можно было создавать итераторы по связному списку: new NextIterator (object)
 * User: And390
 * Date: 05.02.2012
 * Time: 21:58:01
 */
public class NextIterator<T extends NextIterator.Item<T>> implements Iterator<T>
{
    public interface Item<T>  {
        public T getNext();
    }

    public T next;
    public NextIterator(T first)  {  next=first;  }

    public boolean hasNext() {
        return next!=null;
    }

    public T next() {
        T result = next;
        next = next.getNext();
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
