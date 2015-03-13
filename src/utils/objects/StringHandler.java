package utils.objects;


import utils.StringList;

import java.util.Collection;

/**
 * User: And390
 * Date: 09.08.14
 * Time: 0:20
 */
public interface StringHandler<E extends Exception>
{
    public void process(String string) throws E;

    public interface E extends StringHandler<Exception>  {}
    public interface R extends StringHandler<RuntimeException>  {}

    public static class ToList implements StringHandler<RuntimeException>  {
        public StringList result = new StringList ();
        public void process(String string)  {  result.add(string);  }
    }

    public static abstract class Instances
    {
        public static StringHandler<RuntimeException> get(final Collection<String> collection)  {
            return new StringHandler<RuntimeException> ()  {
                public void process(String string)  {  collection.add(string); }
            };
        }
    }
}
