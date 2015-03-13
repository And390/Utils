package utils.xmlparser;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 0:59
 */
public class ExactlyOne extends OccurenceRule
{
    public ExactlyOne(Rule inner)  {  super(inner);  }

    @Override
    public BodyHandler begin(XMLParser parser)
    {
        if (parser.<Integer>getData("occurence")!=0)  throw new XMLException (XMLParser.MORE_ONE_ELEMENT, parser);
        return super.begin(parser);
    }

    @Override
    public void parentEnd(XMLParser parser)
    {
        super.parentEnd(parser);
        if (parser.<Integer>getData("occurence")==0)  throw new XMLException (XMLParser.NO_ONE_ELEMENT, parser);
    }
}
