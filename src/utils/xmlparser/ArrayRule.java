package utils.xmlparser;

import java.lang.reflect.Array;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 17:08
 */
public class ArrayRule extends WrapRule
{
    public final Class itemClass;
    public final boolean nullIfEmpty;
    public ArrayRule(Class itemClass, Rule inner)  {  super(inner);  this.nullIfEmpty=false;  this.itemClass=itemClass;  }
    public ArrayRule(Class itemClass, boolean nullIfEmpty, Rule inner)  {  super(inner);  this.nullIfEmpty=nullIfEmpty;  this.itemClass=itemClass;  }

    @Override
    public BodyHandler begin(XMLParser parser)
    {
        parser.setData(Integer.toHexString(hashCode()), parser.objects.size());  //свое значения для каждого правила
        return super.begin(parser);
    }

    @Override
    public void end(XMLParser parser, String value)
    {
        super.end(parser, value);
        int length = parser.objects.size() - parser.<Integer>getData(Integer.toHexString(hashCode()));
        if (length<0)  throw new XMLException (XMLParser.NO_STACK_VALUES, parser);
        if (length==0 && nullIfEmpty)  {  parser.push(null);  return;  }
        Object array = Array.newInstance(itemClass, length);
        while (length!=0)  {  length--;  Array.set(array, length, parser.pop());  }
        parser.push(array);
    }
}
