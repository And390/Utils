package utils.xmlparser;

/**
 * User: And390
 * Date: 27.09.12
 * Time: 4:23
 */
public class SetLongRule extends SetRule
{
    public SetLongRule(Rule inner)  {  super(inner);  }
    public SetLongRule(String name, Rule inner)  {  super(name, inner);  }

    @Override
    public Object value(String text)
    {
        return new Long(text);
    }
}
