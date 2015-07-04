package utils.objects;

import java.io.IOException;
import java.util.*;

/**
 * User: And390
 * Date: 31.10.14
 * Time: 3:39
 */
public interface KeyValueFeed<K, V, Ex extends Throwable>
{
    public boolean next();
    public K getKey();
    public V getValue();

    public interface E<K, V> extends KeyValueFeed<K, V, Exception>  {}
    public interface R<K, V> extends KeyValueFeed<K, V, RuntimeException>  {}
    public interface IO<K, V> extends KeyValueFeed<K, V, IOException>  {}

    public interface SS<Ex extends Throwable> extends KeyValueFeed<String, String, Ex>  {
        public interface E<K, V> extends SS<Exception>  {}
        public interface R<K, V> extends SS<RuntimeException>  {}
        public interface IO<K, V> extends SS<IOException>  {}
    }
    public interface SO<Ex extends Throwable> extends KeyValueFeed<String, Object, Ex>  {
        public interface E<K, V> extends SO<Exception>  {}
        public interface R<K, V> extends SO<RuntimeException>  {}
        public interface IO<K, V> extends SO<IOException>  {}
    }


    public static abstract class Instances
    {
        public static <T> KeyValueFeed<T, T, RuntimeException> get(final T[] array, final int begin, final int end)
        {
            if ((begin-end)%2!=0)  throw new java.lang.IllegalArgumentException ("elements count must be multiple of 2: "+array.length);
            if (begin>end)  throw new java.lang.IllegalArgumentException ("begin > end: "+begin+" > "+end);
            return new KeyValueFeed<T, T, RuntimeException> ()
            {
                int i=begin;
                public boolean next()  {  if (i==end)  return false;  i+=2;  return true;  }
                public T getKey()  {  if (i==0)  throw new IllegalStateException ();  return array[i-2];  }
                public T getValue()  {  if (i==0)  throw new IllegalStateException ();  return array[i-1];  }
            };
        }

        public static <T> KeyValueFeed<T, T, RuntimeException> get(final T... array)
        {
            if (array.length%2!=0)  throw new java.lang.IllegalArgumentException ("array.length must be multiple of 2: "+array.length);
            return new KeyValueFeed<T, T, RuntimeException> ()
            {
                int i=0;
                public boolean next()  {  if (i==array.length)  return false;  i+=2;  return true;  }
                public T getKey()  {  if (i==0)  throw new IllegalStateException ();  return array[i-2];  }
                public T getValue()  {  if (i==0)  throw new IllegalStateException ();  return array[i-1];  }
            };
        }

        public static <T> KeyValueFeed<T, T, RuntimeException> get(final Collection<T> collection)
        {
            return new KeyValueFeed<T, T, RuntimeException> ()
            {
                Iterator<T> iterator = collection.iterator();
                T key;
                T value;
                public boolean next()  {
                    if (!iterator.hasNext())  return false;
                    key = iterator.next();
                    if (!iterator.hasNext())  throw new java.lang.IllegalArgumentException ("iterator do not return value for last key: "+key);
                    value = iterator.next();
                    return true;
                }
                public T getKey()  {  return key;  }
                public T getValue()  {  return value;  }
            };
        }
//        public static <T> KeyValueFeed<? extends T, ? extends T, RuntimeException> get(final Iterable<T> iterable)  {  return get(iterable.iterator());  }
//        public static <T> KeyValueFeed<? extends T, ? extends T, RuntimeException> get(final Collection<T> collection)  {
//            if (collection.size()%2!=0)  throw new java.lang.IllegalArgumentException ("array.length must be multiple of 2: "+collection.size());
//            return get(collection.iterator());
//        }

        public static <K, V> KeyValueFeed<K, V, RuntimeException> get(final Map<K, V> map)
        {
            return new KeyValueFeed<K, V, RuntimeException> ()
            {
                Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
                Map.Entry<K, V> entity;
                public boolean next()  {  if (!iterator.hasNext())  return false;  entity=iterator.next();  return true;  }
                public K getKey()  {  if (entity==null)  throw new IllegalStateException ();  return entity.getKey();  }
                public V getValue()  {  if (entity==null)  throw new IllegalStateException ();  return entity.getValue();  }
            };
        }
    }
}
