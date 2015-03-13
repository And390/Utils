package utils.iterators;

/**
 * User: And390
 * Date: 17.01.15
 * Time: 15:16
 */
public abstract class IterableUtil
{
    private IterableUtil() {}


    public static <T> Iterable<T> get(Iterable<T>... iterables)
    {
        if (iterables.length==0)  return new EmptyIterable<T> ();
        else if (iterables.length==1)  return iterables[0];
        else if (iterables.length==2)  return new IterableJoin<T> (iterables[0], iterables[1]);
        else  return new IterablesJoin<T> (iterables);
    }

    public static <T> Iterable<T> get(T[]... arrays)
    {
        if (arrays.length==0)  return new EmptyIterable<T> ();
        else if (arrays.length==1)  return new ArrayIterable<T> (arrays[0]);
        else if (arrays.length==2)  return new IterableJoin<T> (new ArrayIterable<T> (arrays[0]), new ArrayIterable<T> (arrays[1]));
        else  {
            @SuppressWarnings("unchecked")
            Iterable<T>[] iterables = new Iterable [arrays.length];
            for (int i=0; i<arrays.length; i++)  iterables[i] = new ArrayIterable<T> (arrays[i]);
            return new IterablesJoin<T> (iterables);
        }
    }

    // TODO создание AloneIteratpr для каждого одиночного объекта может быть неэффективно, если их много подряд
    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> get(Object... values)
    {
        for (int i=0; i<values.length; i++)
            if (!(values[i] instanceof Iterable))
                if (values[i].getClass().isArray())  values[i] = new ArrayIterator<T> ((T[])values[i]);
                else  values[i] = new AloneIterator<T> ((T)values[i]);
        if (values.length==0)  return new EmptyIterable<T> ();
        else if (values.length==1)  return (Iterable<T>)values[0];
        else if (values.length==2)  return new IterableJoin<T> ((Iterable<T>)values[0], (Iterable<T>)values[1]);
        else  {
            @SuppressWarnings("unchecked")
            Iterable<T>[] iterables = new Iterable [values.length];
            for (int i=0; i<values.length; i++)  iterables[i] = (Iterable<T>)values[i];
            return new IterablesJoin<T> (iterables);
        }
    }
}
