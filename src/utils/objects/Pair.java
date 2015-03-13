package utils.objects;

/**
 * User: And390
 * Date: 17.09.14
 * Time: 21:52
 */
public class Pair<K,V>
{
    public K key;
    public V value;
    public Pair()  {}
    public Pair(K key_, V value_)  {  key=key_;  value=value_;  }

    public static class SS extends Pair<String,String>  {
        public SS()  {}
        public SS(String key_, String value_)  {  key=key_;  value=value_;  }
    };
}
