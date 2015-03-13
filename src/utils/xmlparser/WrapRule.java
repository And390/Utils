package utils.xmlparser;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 20:33
 */
public class WrapRule extends Rule
{
    public final Rule inner;
    public WrapRule(Rule inner)  {  this.inner=inner;  }

    @Override
    public String getName()  {  return inner.getName();  }

    @Override
    public Rule child(XMLParser parser, String name)  {  return inner.child(parser, name);  }

    @Override
    public BodyHandler begin(XMLParser parser)  {  return inner.begin(parser);  }

    @Override
    public void end(XMLParser parser, String value)  {  inner.end(parser, value);  }

    @Override
    public void parentBegin(XMLParser parser)  {  inner.parentBegin(parser);  }

    @Override
    public void parentEnd(XMLParser parser)  {  inner.parentEnd(parser);  }

//    @Override
//    public boolean needBody()  {  return inner.needBody();  }
}

