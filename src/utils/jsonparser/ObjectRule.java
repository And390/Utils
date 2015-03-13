package utils.jsonparser;

import java.util.Arrays;

/**
 * Имеет установленные правила для заданных имен дочерних полей.
 * При парсинге проверяет их задублированность и может проверять их обязательность.
 * Задублированные поля могут проскочить, если работает в связке с IgnoreRule.
 * User: And390
 * Date: 11.10.14
 * Time: 2:48
 */
public class ObjectRule extends WrapRule.WithData
{
    private String[] names;
    private Rule[] rules;
    private boolean[] required;  //if no one is required then required=null
    //private int[] indexes;
    // эти массивы не должны меняться во време парсинга (между objectBegin и objectEnd для данного объекта)

    public ObjectRule(Object... values)
    {
        this(null, values);
    }

    public ObjectRule(Rule next, Object... values)
    {
        super(next);

        if (values.length%3!=0)  throw new IllegalArgumentException ("Values length must be multiple of 3");
        //    fill
        names = new String [values.length/3];
        rules = new Rule [values.length/3];
        //indexes = new int [values.length/2];
        for (int i=0; i<values.length/3; i++)  {
            names[i] = (String)values[i*3];
            rules[i] = (Rule)values[i*3+2];
            //indexes[i] = i;
            if ((Boolean)values[i*3+1])  {
                if (required==null)  required = new boolean [values.length/3];
                required[i] = true;
            }
        }
        //    sort
        new Runnable ()  {
            public void quickSort(int low, int high)  {
                int i = low;
                int j = high;
                String x = names[(low+high)/2];

                do  {
                    while (names[i].compareTo(x)<0)  i++;
                    while (names[j].compareTo(x)>0)  j--;
                    if (i<=j)  {
                        //    swap
                        String name=names[i];  names[i]=names[j];  names[j]=name;
                        Rule rule=rules[i];  rules[i]=rules[j];  rules[j]=rule;
                        if (required!=null)  {  boolean req=required[i];  required[i]=required[j];  required[j]=req;  }
                        //int index=indexes[i];  indexes[i]=indexes[j];  indexes[j]=index;
                        //
                        i++;  j--;
                    }
                }  while(i<=j);

                if (low<j)  quickSort(low, j);
                if (i<high)  quickSort(i, high);
            }
            public void run()  {
                if (names.length!=0)  quickSort(0, names.length-1);
            }
        }.run();
        //    find duplicates
        for (int i=1; i<names.length; i++)
            if (names[i-1].equals(names[i]))  throw new RuntimeException ("Duplicate field: "+names[i]);
    }

    public int size()  {
        return names.length;
    }

    public ObjectRule add(String fieldName, boolean isRequired, Rule childRule)  {
        //    find insert index
        int i = Arrays.binarySearch(names, fieldName);
        if (i>=0)  throw new RuntimeException ("Object already contains field: "+fieldName);
        i = -i - 1;
        //    expand arrays
        names = Arrays.copyOf(names, names.length+1);
        rules = Arrays.copyOf(rules, rules.length+1);
        if (required!=null)  required = Arrays.copyOf(required, required.length+1);
        //indexes = Arrays.copyOf(indexes, indexes.length+1);
        //    shift
        if (i!=names.length-1)  {
            System.arraycopy(names, i, names, i+1, names.length-i-1);
            System.arraycopy(rules, i, rules, i+1, rules.length-i-1);
            if (required!=null)  System.arraycopy(required, i, required, i+1, required.length-i-1);
            //System.arraycopy(indexes, i, indexes, i+1, indexes.length-i-1);
        }
        //    set new value
        names[i] = fieldName;
        rules[i] = childRule;
        if (isRequired)  {
            if (required==null)  required = new boolean [names.length];
            required[i] = true;
        }
        //indexes[i] = names.length-1;
        return this;
    }

    public Rule get(String fieldName)  {
        int i = Arrays.binarySearch(names, fieldName);
        if (i<0)  return null;
        return rules[i];
    }


    @Override
    public Data objectBegin(JSONParser parser) throws JSONException
    {
        //    init inner first
        Data data = next==null ? null : next.objectBegin(parser);
        //    create our data and return
        return new ObjectData (data, this);
    }

    @Override
    public void objectEnd(JSONParser parser, Data data) throws JSONException
    {
        //    check occurence
        if (required!=null)  {
            boolean[] occurence = ((ObjectData)data).occurence;
            for (int i=0; i<occurence.length; i++)
                if (!occurence[i] && required[i])  throw new JSONException (parser, JSONParser.NO_FIELD, names[i]);
        }
        //    end inner
        if (next!=null)  next.objectEnd(parser, ((ObjectData)data).next);
    }

    @Override
    public Rule objectField(JSONParser parser, Data data, String name) throws JSONException
    {
        //    find field
        int i = Arrays.binarySearch(names, name);
        //    if not found find in inner
        if (i<0)  return next==null ? null : next.objectField(parser, ((ObjectData) data).next, name);
        //    set child occurence
        if (((ObjectData)data).occurence[i])  throw new JSONException (parser, JSONParser.DUPLICATE_FIELD, name);
        ((ObjectData)data).occurence[i] = true;
        //    return child rule
        return rules[i];
    }


    public static class ObjectData extends WrapData {
        public boolean[] occurence;
        public ObjectData(Data next_, ObjectRule rule)  {  super(next_);  occurence = new boolean [rule.names.length];  }
    }


}
