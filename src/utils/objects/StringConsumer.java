package utils.objects;


import utils.StringList;

import java.util.Collection;

/**
 * User: And390
 * Date: 09.08.14
 * Time: 0:20
 */
public interface StringConsumer extends Consumer<String, RuntimeException>
{
    @Override
    public void process(String string);


    public static class ToList implements StringConsumer
    {
        public StringList result = new StringList ();
        public void process(String string)  {  result.add(string);  }
    }

    public static abstract class Instances
    {
        public static StringConsumer get(final Collection<String> collection)  {
            return new StringConsumer ()  {
                public void process(String string)  {  collection.add(string); }
            };
        }
    }
}
