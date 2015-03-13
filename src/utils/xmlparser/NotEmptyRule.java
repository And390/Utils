package utils.xmlparser;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 3:43
 */
public class NotEmptyRule extends ValueRule
{
    public NotEmptyRule (Rule inner)  {  super(inner);  }

    @Override
    public void end(XMLParser parser, String value)
    {
        super.end(parser, value);
        if (value.trim().length()==0)  throw new XMLException (XMLParser.VALUE_EMPTY, parser);
    }
}
