package utils.xmlparser;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 3:06
 */
public interface BodyHandler
{
    public void characters(XMLParser parser, char[] ch, int start, int length);
    public String getValue();
}
