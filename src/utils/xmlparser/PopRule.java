package utils.xmlparser;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 17:19
 */
public class PopRule extends WrapRule
{
    public final String name;
    public PopRule(Rule inner)  {  super(inner);  this.name=null;  }
    public PopRule(String name, Rule inner)  {  super(inner);  this.name=name;  }

    @Override
    public void end(XMLParser parser, String value)
    {
        super.end(parser, value);
        SetRule.set(parser, name, parser.pop());
    }
}
