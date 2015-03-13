package utils.xmlparser;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 23:28
 */
public class NoMoreOne extends OccurenceRule
{
    public NoMoreOne(Rule inner)  {  super(inner);  }

    @Override
    public BodyHandler begin(XMLParser parser)
    {
        if (parser.<Integer>getData("occurence")!=0)  throw new XMLException (XMLParser.MORE_ONE_ELEMENT, parser);
        return super.begin(parser);
    }
}
