package utils.xmlparser;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 17:02
 */
public class SetCharArrayRule extends SetRule
{
    public SetCharArrayRule(Rule inner)  {  super(inner);  }
    public SetCharArrayRule(Rule inner, String name)  {  super(name, inner);  }

    public Object value(String text)
    {
        return text.toCharArray();
    }
}
