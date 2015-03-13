package utils.xmlparser;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 19:09
 */
public class Attribute extends Rule
{
    public final String name;
    public Attribute (String name)  {  this.name="@"+name;  }

    @Override
    public String getName()  {  return name;  }

    @Override
    public Rule child(XMLParser parser, String name)  {  return null;  }

    public static Rule required(String name)  {  return new AtLeastOne (new Attribute (name));  }
    public static Rule optional(String name)  {  return new Attribute (name);  }
}
