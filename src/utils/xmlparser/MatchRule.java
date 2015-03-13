package utils.xmlparser;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 2:59
 */
public class MatchRule extends ValueRule
{
    public final String expected;
    public MatchRule(String expected, Rule inner)  {  super(inner);  this.expected=expected;  }

    @Override
    public void end(XMLParser parser, String value)
    {
        super.end(parser, value);
        if (!expected.equals(value))  throw new XMLException (XMLParser.VALUE_NOT_MATCH, " ("+value+")", parser);
    }
}
