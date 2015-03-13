package utils.jsonparser;

import java.util.Arrays;

/**
 * Разбирает объект с именами полей, появляющимися в заданном порядке. Какое-то количество первых может быть обязательным.
 * User: And390
 * Date: 15.11.14
 * Time: 23:18
 */
public class ObjectOrderedRule extends WrapRule.WithData
{
    private String[] names;
    private Rule[] rules;
    // эти массивы не должны меняться во време парсинга (между objectBegin и objectEnd для данного объекта)
    private int requiredCount;


    public ObjectOrderedRule(int optionalCount, Object... values)
    {
        this(null, optionalCount, values);
    }

    public ObjectOrderedRule(Rule next, int optionalCount, Object... values)
    {
        super(next);

        if (values.length%2!=0)  throw new IllegalArgumentException ("Values length must be multiple of 2");
        if (optionalCount<0)  throw new IllegalArgumentException ("optionalCount must not be negative: "+optionalCount);
        if (optionalCount>values.length/2)  throw new IllegalArgumentException ("optionalCount must not be greater than childs count: "+optionalCount+" > "+values.length/2);
        requiredCount = values.length/2 - optionalCount;
        //    fill
        names = new String [values.length/2];
        rules = new Rule [values.length/2];
        for (int i=0; i<values.length/2; i++)  {
            names[i] = (String)values[i*2];
            rules[i] = (Rule)values[i*2+1];
        }
        //    find duplicates
        for (int i=1; i<names.length; i++)  for (int j=0; j<i; j++)
            if (names[j].equals(names[i]))  throw new RuntimeException ("Duplicate field: "+names[j]);
    }

    public int size()  {
        return names.length;
    }

    public ObjectOrderedRule add(String fieldName, boolean isRequired, Rule childRule)  {
        if (isRequired && requiredCount!=names.length)  throw new IllegalStateException ("Can not add required child because last child is not required");
        //    expand arrays
        names = Arrays.copyOf(names, names.length+1);
        rules = Arrays.copyOf(rules, rules.length+1);
        //    set new value
        names[names.length-1] = fieldName;
        rules[rules.length-1] = childRule;
        if (isRequired)  requiredCount++;
        return this;
    }

    public Rule get(int index)  {
        return rules[index];
    }


    @Override
    public Data objectBegin(JSONParser parser) throws JSONException
    {
        //    init inner first
        Data data = next==null ? null : next.objectBegin(parser);
        //    create our data and return
        return new ObjectData (data);
    }

    @Override
    public void objectEnd(JSONParser parser, Data data) throws JSONException
    {
        //    check required fields
        int index = ((ObjectData)data).counter;
        if (index<requiredCount)  throw new JSONException (parser, JSONParser.NO_FIELD, names[index]);
        //    end inner
        if (next!=null)  next.objectEnd(parser, ((ObjectData)data).next);
    }

    @Override
    public Rule objectField(JSONParser parser, Data data, String name) throws JSONException
    {
        //    get field index
        int index = ((ObjectData)data).counter;
        //    check name
        if (index>=names.length || !names[index].equals(name))  return next==null ? null : next.objectField(parser, ((ObjectData) data).next, name);
        //    inc counter
        ((ObjectData)data).counter++;
        //    return child rule
        return rules[index];
    }


    public static class ObjectData extends WrapData {
        public int counter = 0;  //количество встреченных детей
        public ObjectData(Data next_)  {  super(next_);  }
    }
}
