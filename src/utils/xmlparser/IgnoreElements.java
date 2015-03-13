package utils.xmlparser;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 2:27
 */
public class IgnoreElements extends WrapRule
{
    public IgnoreElements(Rule inner)  {  super(inner);  }

    @Override
    public Rule child(XMLParser parser, String name)  {
        Rule rule = super.child(parser, name);
        if (rule!=null)  return rule;
        if (name.charAt(0)=='/')  return IGNORE_RULE;
        return null;
    }


    public static final Rule IGNORE_RULE = new Rule() {
        @Override
        public String getName()  {  return "/*";  }
        @Override
        public Rule child(XMLParser parser, String name)  {
            if (name.charAt(0)=='/')  return IgnoreElements.IGNORE_RULE;
            if (name.charAt(0)=='@')  return IgnoreAttributes.IGNORE_RULE;
            return null;
        }
        @Override
        public BodyHandler begin(XMLParser parser)  {
            return IgnoreBody.BODY_HANDLER;
        }
    };
}
