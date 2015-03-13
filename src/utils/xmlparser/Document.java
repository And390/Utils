package utils.xmlparser;

import java.util.Arrays;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 23:34
 */
public class Document extends Rule
{
//    public final Element[] childs;
//    public Document (Element... childs)  {  this.childs=childs;  Arrays.sort(childs, Element.COMPARATOR);  }
    private Rule[] childs;
    public Document (Rule... childs)  {  this.childs=childs;  Arrays.sort(childs, Element.COMPARATOR);  }

    public void childs(Rule... childs)  {  this.childs = childs;  Arrays.sort(childs, Element.COMPARATOR);  }

    @Override
    public String getName()  {  return "";  }

    @Override
    public Rule child(XMLParser parser, String name)
    {
        return Element.find(childs, name);
    }

    @Override
    public BodyHandler begin(XMLParser parser)
    {
        for (Rule child : childs)  {
            String childName = child.getName();
            parser.path.append(childName);
            child.parentBegin(parser);
            parser.path.setLength(parser.path.length()-childName.length());
        }
        return Element.BODY_HANDLER;
    }

    @Override
    public void end(XMLParser parser, String value)
    {
        for (Rule child : childs)  {
            String childName = child.getName();
            parser.path.append(childName);
            child.parentEnd(parser);
            parser.path.setLength(parser.path.length()-childName.length());
        }
    }
}
