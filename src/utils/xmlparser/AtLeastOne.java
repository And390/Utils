package utils.xmlparser;

import com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 20:26
 */
public class AtLeastOne extends OccurenceRule
{
    public AtLeastOne(Rule inner)  {  super(inner);  }

    @Override
    public void parentEnd(XMLParser parser)
    {
        super.parentEnd(parser);
        if (parser.<Integer>getData("occurence")==0)  throw new XMLException (XMLParser.NO_ONE_ELEMENT, parser);
    }
}