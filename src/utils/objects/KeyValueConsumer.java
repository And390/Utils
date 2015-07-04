package utils.objects;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: And390
 * Date: 17.11.14
 * Time: 1:52
 */
public interface KeyValueConsumer<K, V, Ex extends Throwable>
{
    public void process(K key, V value) throws Ex;

    public interface E<K, V> extends KeyValueConsumer<K, V, Exception> {}
    public interface R<K, V> extends KeyValueConsumer<K, V, RuntimeException> {}
    public interface IO<K, V> extends KeyValueConsumer<K, V, IOException> {}

    public interface S<V, Ex extends Throwable> extends KeyValueConsumer<String, V, Ex> {
        public interface E<V> extends S<V, Exception>  {}
        public interface R<V> extends S<V, RuntimeException>  {}
        public interface IO<V> extends S<V, IOException>  {}
    }

    public interface SO<Ex extends Throwable> extends KeyValueConsumer<String, Object, Ex> {
        public interface E extends SO<Exception>  {}
        public interface R extends SO<RuntimeException>  {}
        public interface IO extends SO<IOException>  {}
    }

    public interface SS<Ex extends Throwable> extends KeyValueConsumer<String, String, Ex> {
        public interface E extends SS<Exception>  {}
        public interface R extends SS<RuntimeException>  {}
        public interface IO extends SS<IOException>  {}
    }

    public static class ToMap<K, V> implements KeyValueConsumer<K, V, RuntimeException> {
        public HashMap<K, V> result = new HashMap<K, V> ();
        public void process(K key, V value)  {  result.put(key, value);  }
    }

    public static abstract class Instances  {
        public static <K, V> KeyValueConsumer<K, V, RuntimeException> get(final Map<K, V> map)  {
            return new KeyValueConsumer<K, V, RuntimeException>()  {
                @Override
                public void process(K key, V value) throws RuntimeException  {
                    map.put(key, value);
                }
            };
        }
    }
}
