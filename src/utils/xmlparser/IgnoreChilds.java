package utils.xmlparser;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 1:20
 */
public class IgnoreChilds extends WrapRule
{
    public IgnoreChilds(Rule inner)  {  super(inner);  }

    @Override
    public Rule child(XMLParser parser, String name)  {
        Rule rule = super.child(parser, name);
        if (rule!=null)  return rule;
        if (name.charAt(0)=='/')  return IgnoreElements.IGNORE_RULE;
        if (name.charAt(0)=='@')  return IgnoreAttributes.IGNORE_RULE;
        return null;
    }
}
