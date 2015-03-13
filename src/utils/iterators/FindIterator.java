package utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * FindIterator
 * ������ ������� hasNext � next ���������� Iterator � ������� findNext,
 * ������� ������ ������� true, ���� ������� ��������� �������, � �������� ��� � nextFinded 
 * User: And390
 * Date: 05.02.2012
 * Time: 16:10:05
 */
public abstract class FindIterator<T> implements Iterator<T> 
{
    protected T nextFinded = null;
    protected abstract boolean findNext();

    public T next() {
        if (nextFinded==null)  if (!findNext())  throw new NoSuchElementException();
        T result = nextFinded;
        nextFinded = null;
        return result;
    }

    public boolean hasNext()  {
        if (nextFinded!=null)  return true;
        else  return findNext();
    }

    public void remove()  {  throw new UnsupportedOperationException();  }
}
