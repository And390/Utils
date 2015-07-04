package utils.objects;

import java.util.Iterator;

/**
 * Producer - последовательно выдает какие-то объекты. Интерфес общего назначения.
 * Для определения последнего значения может использоваться null, хотя это не обязательно.
 * User: And390
 * Date: 17.09.14
 * Time: 21:52
 */
public interface Producer<T, Ex extends Throwable>
{
    public T next() throws Ex;

    public static interface E<T> extends Producer<T, Exception> {}
    public static interface R<T> extends Producer<T, RuntimeException> {}


    public static abstract class Instances  {
        public static <T> Producer.R<T> get(final Iterator<T> iterator)  {
            return new Producer.R<T>()  {
                public T next()  {  if (!iterator.hasNext())  return null;  return iterator.next();  }
            };
        }
        public static <T> Producer.R<T> get(final Iterable<T> iterable)  {  return get(iterable.iterator());  }
    }
}
