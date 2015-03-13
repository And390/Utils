package utils.xmlparser;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 17:17
 */
public class PushRule extends ValueRule
{
    public final Class class_;
    public PushRule(Rule inner)  {  super(inner);  this.class_=null;  }
    public PushRule(Class class_, Rule inner)  {  super(inner);  this.class_=class_;  }

    @Override
    public void end(XMLParser parser, String text)
    {
        super.end(parser, text);
        Object value;
        if (class_!=null)
            try  {  value = class_.getConstructor(String.class).newInstance(text);  }
            catch (Exception e)  {  throw new RuntimeException (e);  }
        else
            value=text;
        parser.push(value);
    }
}
