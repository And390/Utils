package utils.iterators;

import java.util.Iterator;

/**
 * FindIterator
 * Адаптер реализует FindIterator для имеющегося итератора путем определения функции accept(item);
 * которой скармливаются все элементы исходного итератора, пока она не вернет true, или пока не закончатся элементы
 * User: And390
 * Date: 05.02.2012
 * Time: 21:28:07
 */
public abstract class FilterIterator<T> extends FindIterator<T>
{
    private Iterator<? extends T> iterator;
    public FilterIterator (Iterator<? extends T> _iterator)  {  iterator=_iterator;  }

    protected abstract boolean accept(T item);

    protected boolean findNext()
    {
        while (iterator.hasNext())
        {
            T item = iterator.next();
            if (accept(item)) {
                nextFinded = item;
                return true;
            }
        }
        return false;
    }

    public void remove()  {  iterator.remove();  }
}
