package utils.xmlparser;

import com.sun.org.apache.bcel.internal.classfile.InnerClass;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 1:13
 */
public class IgnoreBody extends WrapRule
{
    public IgnoreBody(Rule inner)  {  super(inner);  }

    @Override
    public BodyHandler begin(XMLParser parser)
    {
        if (super.begin(parser)!=Element.BODY_HANDLER)  throw new XMLException(XMLParser.OVERRIDE_BODY_HANDLER, parser);
        return BODY_HANDLER;
    }


    public static BodyHandler BODY_HANDLER = new BodyHandler ()  {
        @Override
        public void characters(XMLParser parser, char[] ch, int start, int length)  {}
        @Override
        public String getValue()  {  return null;  }
    };
}
