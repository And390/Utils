package utils.objects;

import java.util.Iterator;

/**
 * User: And390
 * Date: 17.09.14
 * Time: 21:52
 */
public interface Feed<T, E extends Throwable>
{
    public T next() throws E;

    public static interface E<T> extends Feed<T, Exception>  {}
    public static interface R<T> extends Feed<T, RuntimeException>  {}


    public static abstract class Instances  {
        public static <T> Feed.R<T> get(final Iterator<T> iterator)  {
            return new Feed.R<T>()  {
                public T next()  {  if (!iterator.hasNext())  return null;  return iterator.next();  }
            };
        }
        public static <T> Feed.R<T> get(final Iterable<T> iterable)  {  return get(iterable.iterator());  }
    }
}
