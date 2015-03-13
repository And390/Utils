package utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: And390
 * Date: 17.11.14
 * Time: 1:52
 */
public interface KeyValueHandler<K, V, E extends Exception>
{
    public void process(K key, V value) throws E;

    public interface E<K, V> extends KeyValueHandler<K, V, Exception>  {}
    public interface R<K, V> extends KeyValueHandler<K, V, RuntimeException>  {}
    public interface IO<K, V> extends KeyValueHandler<K, V, IOException>  {}

    public interface S<V, E extends Exception> extends KeyValueHandler<String, V, E>  {
        public interface E<V> extends S<V, Exception>  {}
        public interface R<V> extends S<V, RuntimeException>  {}
        public interface IO<V> extends S<V, IOException>  {}
    }

    public interface SO<E extends Exception> extends KeyValueHandler<String, Object, E>  {
        public interface E extends SO<Exception>  {}
        public interface R extends SO<RuntimeException>  {}
        public interface IO extends SO<IOException>  {}
    }

    public interface SS<E extends Exception> extends KeyValueHandler<String, String, E>  {
        public interface E extends SS<Exception>  {}
        public interface R extends SS<RuntimeException>  {}
        public interface IO extends SS<IOException>  {}
    }

    public static class ToMap<K, V> implements KeyValueHandler<K, V, RuntimeException>  {
        public HashMap<K, V> result = new HashMap<K, V> ();
        public void process(K key, V value)  {  result.put(key, value);  }
    }

    public static abstract class Instances  {
        public static <K, V> KeyValueHandler<K, V, RuntimeException> get(final Map<K, V> map)  {
            return new KeyValueHandler<K, V, RuntimeException> ()  {
                @Override
                public void process(K key, V value) throws RuntimeException  {
                    map.put(key, value);
                }
            };
        }
    }
}
