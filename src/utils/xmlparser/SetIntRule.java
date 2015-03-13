package utils.xmlparser;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 16:57
 */
public class SetIntRule extends SetRule
{
    public SetIntRule(Rule inner)  {  super(inner);  }
    public SetIntRule(String name, Rule inner)  {  super(name, inner);  }

    @Override
    public Object value(String text)
    {
        return new Integer(text);
    }
}
