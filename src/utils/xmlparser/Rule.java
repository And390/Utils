package utils.xmlparser;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 19:08
 */
public abstract class Rule
{
    public abstract String getName();
    public abstract Rule child(XMLParser parser, String name);
    public BodyHandler begin(XMLParser parser)  {  return null;  }
    public void end(XMLParser parser, String value)  {}
    public void parentBegin(XMLParser parser)  {}
    public void parentEnd(XMLParser parser)  {}
}
