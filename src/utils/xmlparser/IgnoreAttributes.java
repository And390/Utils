package utils.xmlparser;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 1:18
 */
public class IgnoreAttributes extends WrapRule
{
    public IgnoreAttributes(Rule inner)  {  super(inner);  }

    @Override
    public Rule child(XMLParser parser, String name)  {
        Rule rule = super.child(parser, name);
        if (rule!=null)  return rule;
        if (name.charAt(0)=='@')  return IGNORE_RULE;
        return null;
    }


    public static final Rule IGNORE_RULE = new Rule() {
        @Override
        public String getName()  {  return "@*";  }
        @Override
        public Rule child(XMLParser parser, String name)  {  return null;  }
    };
}
