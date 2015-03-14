package utils.objects;

import java.util.Iterator;

/**
 * User: And390
 * Date: 17.09.14
 * Time: 21:56
 */
public interface StringProducer<E extends Throwable> extends Producer<String, E>
{
    public static interface E extends StringProducer<Exception> {}
    public static interface R extends StringProducer<RuntimeException> {}


    public static abstract class Instances
    {
        public static StringProducer.R get(final Iterator<String> iterator)  {
            return new StringProducer.R()  {
                public String next()  {  if (!iterator.hasNext())  return null;  return iterator.next();  }
            };
        }
        public static StringProducer.R get(final Iterable<String> iterable)  {  return get(iterable.iterator());  }
    }
}