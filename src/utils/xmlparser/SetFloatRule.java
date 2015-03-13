package utils.xmlparser;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 16:59
 */
public class SetFloatRule extends SetRule
{
    public SetFloatRule(Rule inner)  {  super(inner);  }
    public SetFloatRule(String name, Rule inner)  {  super(name, inner);  }

    @Override
    public Object value(String text)
    {
        return new Float(text);
    }
}
