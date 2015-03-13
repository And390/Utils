import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: andreyzaharov
 * Date: 01.11.2011
 * Time: 16:06:32
 */
public class Test {

    public static abstract class TestClass<T extends TestClass<T>> implements Iterable<T>
    {
        T next;

        @Override
        public Iterator<T> iterator()
        {
            return new Iterator<T> ()
            {
                private T next_ = next;

                public boolean hasNext()  {  return next_!=null;  }

                public T next()  {
                    if (next==null)  throw new NoSuchElementException ();
                    T result=next_;  next_=next_.next;  return result;
                }

                public void remove()  {  throw new UnsupportedOperationException ();  }
            };
        }
    }

    public static class DerivedClass extends TestClass<DerivedClass>
    {

    }


    public static <T extends TestClass<T>> Iterable<T> iterable(final T firstObject)
    {
        return null;
    }

    public static void main(String[] args)
    {
        TestClass testObject = new DerivedClass ();

        Iterable iterable = iterable (testObject);
        for (Object i : iterable (testObject));
        for (TestClass i : (Iterable<TestClass>) iterable (testObject))  ;

        Iterable<TestClass> iterable2 = iterable (testObject);
        //for (TestClass object : iterable (testObject));  //compile error
    }

}
