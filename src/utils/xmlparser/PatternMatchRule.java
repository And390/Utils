package utils.xmlparser;

import java.util.regex.Pattern;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 3:40
 */
public class PatternMatchRule extends ValueRule
{
    public Pattern pattern;
    public PatternMatchRule (String regex, Rule inner)  {  super(inner);  this.pattern=Pattern.compile(regex);  }

    @Override
    public void end(XMLParser parser, String value)
    {
        super.end(parser, value);
        if (!pattern.matcher(value).matches())
            throw new XMLException(XMLParser.VALUE_NOT_MATCH, " ("+value+")", parser);
    }
}
