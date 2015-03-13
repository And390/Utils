package utils.xmlparser;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 20:38
 */
public class OccurenceRule extends WrapRule
{
    public OccurenceRule(Rule inner)  {  super(inner);  }

    @Override
    public BodyHandler begin(XMLParser parser)
    {
        parser.setData("occurence", parser.<Integer>getData("occurence") + 1);
        return super.begin(parser);
    }

    @Override
    public void parentBegin(XMLParser parser)
    {
        parser.setData("occurence", 0);
        super.parentBegin(parser);
    }
}
