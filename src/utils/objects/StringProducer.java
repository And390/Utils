package utils.objects;

import java.util.Iterator;

/**
 * User: And390
 * Date: 17.09.14
 * Time: 21:56
 */
public interface StringProducer extends Producer<String, RuntimeException>
{
    @Override
    public String next();


    public static abstract class Instances
    {
        public static StringProducer.R<String> get(final Iterator<String> iterator)  {
            return new StringProducer.R<String>()  {
                public String next()  {  if (!iterator.hasNext())  return null;  return iterator.next();  }
            };
        }
        public static StringProducer.R<String> get(final Iterable<String> iterable)  {  return get(iterable.iterator());  }
    }
}