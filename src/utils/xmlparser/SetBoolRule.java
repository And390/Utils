package utils.xmlparser;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 16:59
 */
public class SetBoolRule extends SetRule
{
    public SetBoolRule(Rule inner)  {  super(inner);  }
    public SetBoolRule(String name, Rule inner)  {  super(name, inner);  }

    @Override
    public Object value(String text)
    {
        if (text.equals("true"))  return true;
        if (text.equals("false"))  return false;
        throw new IllegalArgumentException ("value must be equal true or false");
    }
}
