package utils.xmlparser;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 17:01
 */
public class SetFlagRule extends WrapRule
{
    public final String name;
    public SetFlagRule(Rule inner)  {  super(inner);  this.name=null;  }
    public SetFlagRule(String name, Rule inner)  {  super(inner);  this.name=name;  }

    @Override
    public void end(XMLParser parser, String value)
    {
        super.end(parser, value);
        SetRule.set(parser, name, true);
    }
}
