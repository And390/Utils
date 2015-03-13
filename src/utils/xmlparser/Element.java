package utils.xmlparser;

import java.util.Arrays;
import java.util.Comparator;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 19:08
 */
public class Element extends Rule
{
    private final String name;
    private Rule[] childs;
    public Element (String name, Rule... childs)  {
        this.name="/"+name;
        this.childs=childs;
        Arrays.sort(childs, COMPARATOR);
    }

    public void setChilds(Rule... childs)  {
        this.childs = childs;
        Arrays.sort(childs, COMPARATOR);
    }

    public Rule[] getChilds()  {  return childs;  }

    public static final Comparator<Rule> COMPARATOR = new Comparator<Rule>()  {
        @Override
        public int compare(Rule rule1, Rule rule2)  {
            return rule1.getName().compareTo(rule2.getName());
        }
    };

    @Override
    public String getName()  {  return name;  }

    public static Rule find(Rule[] childs, String name)
    {
        int low = 0;
        int high = childs.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            Rule child = childs[mid];
            int cmp = child.getName().compareTo(name);
            if (cmp < 0)  low = mid + 1;
            else if (cmp > 0)  high = mid - 1;
            else  return child;  //found
        }
        return null;
    }

    @Override
    public Rule child(XMLParser parser, String name)
    {
        return find(childs, name);
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
        return BODY_HANDLER;
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

    public static Rule required(String name, Rule... childs)  {  return new ExactlyOne (new Element (name, childs));  }
    public static Rule optional(String name, Rule... childs)  {  return new NoMoreOne (new Element (name, childs));  }
    public static Rule oneOrMore(String name, Rule... childs)  {  return new AtLeastOne (new Element (name, childs));  }
    public static Rule zeroOrMore(String name, Rule... childs)  {  return new Element (name, childs);  }
    public static Element root(String name, Rule... childs)  {  return new Element (name, childs);  }


    public static final BodyHandler BODY_HANDLER = new BodyHandler() {
        @Override
        public void characters(XMLParser parser, char[] ch, int start, int length)
        {
            for (; length!=0; start++, length--)
                if (ch[start]>' ')  throw new XMLException(XMLParser.UNEXPECTED_VALUE, parser);
        }
        @Override
        public String getValue()  {  return null;  }
    };
}
