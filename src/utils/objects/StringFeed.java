package utils.objects;

import java.util.Iterator;

/**
 * User: And390
 * Date: 17.09.14
 * Time: 21:56
 */
public interface StringFeed<E extends Throwable> extends Feed<String, E>
{
    public static interface E extends StringFeed<Exception>  {}
    public static interface R extends StringFeed<RuntimeException>  {}


    public static abstract class Instances  {
        public static StringFeed.R get(final Iterator<String> iterator)  {
            return new StringFeed.R()  {
                public String next()  {  if (!iterator.hasNext())  return null;  return iterator.next();  }
            };
        }
        public static StringFeed.R get(final Iterable<String> iterable)  {  return get(iterable.iterator());  }
    }
}