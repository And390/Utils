package utils.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: And390
 * Date: 23.09.12
 * Time: 19:08
 */
public class XMLParser extends DefaultHandler
{
    public static final String INTERNAL_ERROR = "internal error";
    public static final String BAD_XML = "xml is not well-formatted";
    public static final String NO_DOCUMENT_RULE = "no document main rule";
    public static final String NO_RULE = "no rule for this element";
    public static final String NO_BODY_HANDLER = "lost BodyHandler";
    public static final String ATTRIBUTE_BODY_HANDLER = "attribute must not contain BodyHandler";
    public static final String NO_ONE_ELEMENT = "no one element";
    public static final String MORE_ONE_ELEMENT = "more than one element";
    public static final String UNEXPECTED_VALUE = "element must not contain value";
    public static final String VALUE_NOT_MATCH = "element value is not match";
    public static final String VALUE_EMPTY = "element value must not be empty";
    public static final String NO_STACK_VALUES = "not enough values in stack";
    public static final String OVERRIDE_BODY_HANDLER = "override not default body handler";  //internal

    public static final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    static  {
        saxParserFactory.setNamespaceAware(true);
    }

    private final SAXParser saxparser;

    public XMLParser() throws ParserConfigurationException, SAXException
    {
        saxparser = saxParserFactory.newSAXParser();
    }

    public XMLParser(Document document_) throws ParserConfigurationException, SAXException
    {
        this();
        document = document_;
    }

    public void parse(String source) throws XMLException, IOException
    {
        path.setLength(0);
        data.clear();
        locator = null;
        currentRule = document;
        if (currentRule==null)  throw new XMLException(NO_DOCUMENT_RULE, this);
//        buffer = null;
        currentBodyHandler = null;
        try  {  saxparser.parse(new InputSource(new StringReader(source)), this);  }
        catch (XMLException e)  {  throw e;  }
        catch (RuntimeException e)  {  throw new XMLException (INTERNAL_ERROR, this, e);  }
        catch (SAXException e)  {  throw new XMLException (BAD_XML, this, e);  }
    }


    //                --------    sax handler    --------

    public Document document;

    public final StringBuilder path = new StringBuilder();

    public Locator locator;

    private Rule currentRule;
//    private StringBuilder buffer;  //String or StringBuilder
    private BodyHandler currentBodyHandler;

    @Override
    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    @Override
    public void startDocument() throws SAXException
    {
        currentBodyHandler = currentRule.begin(this);
    }

    @Override
    public void endDocument() throws SAXException
    {
        currentRule.end(this, currentBodyHandler.getValue());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        setData("rule", currentRule);
        setData("BodyHandler", currentBodyHandler);
        path.append('/').append(localName);
        currentRule = currentRule.child(this, "/"+localName);
        if (currentRule==null)  throw new XMLException(NO_RULE, this);
        currentBodyHandler = currentRule.begin(this);
//        if (currentRule.needBody())  {
//            buffer = getData("buffer");
//            if (buffer==null)  {
//                buffer = new StringBuilder ();
//                setData("buffer", buffer);
//            }
//            buffer.setLength(0);
//        }
        //currentBodyHandler = getData("BodyHandler");
        //if (currentBodyHandler==null)  currentBodyHandler = DEFAULT_BODY_HANDLER;
        //    attributes
        for (int i=0; i<attributes.getLength(); i++)  {
            String name = attributes.getLocalName(i);
            String value = attributes.getValue(i);
            path.append('@').append(name);
            Rule attrRule = currentRule.child(this, "@"+name);
            if (attrRule==null)  throw new XMLException(NO_RULE, this);
            if (attrRule.begin(this)!=null)  throw new XMLException (ATTRIBUTE_BODY_HANDLER, this);
            attrRule.end(this, value);
            path.setLength(path.length() - name.length() - 1);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
//        if (buffer!=null)  buffer.append(ch, start, length);
//        else
//            for (; length!=0; start++, length--)
//                if (ch[start]>' ')  throw new XMLException(UNEXPECTED_VALUE, this);
        currentBodyHandler.characters(this, ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
//        currentRule.end(this, buffer!=null ? buffer.toString() : null);
        currentRule.end(this, currentBodyHandler.getValue());
        path.setLength(path.length() - localName.length() - 1);
        currentRule = getData("rule");
        if (currentRule==null)  throw new XMLException(NO_RULE, " after "+localName, this);
//        buffer = getData("buffer");
        currentBodyHandler = getData("BodyHandler");
        if (currentBodyHandler==null)  throw new XMLException(NO_BODY_HANDLER, " after "+localName, this);
    }


    //                --------    elements data    --------

    //todo созможно эти данные элементов можно эффективнее реализовать
    public final HashMap<String, Object> data = new HashMap<String, Object> ();

    @SuppressWarnings("unchecked")
    public <T> T getData(String name)
    {
        path.append('#').append(name);
        Object object = data.get(path.toString());
        path.setLength(path.length() - name.length() - 1);
        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public <T> T setData(String name, T object)
    {
        path.append('#').append(name);
        object = (T) data.put(path.toString(), object);
        path.setLength(path.length() - name.length() - 1);
        return object;
    }


    //                --------    objects    --------

    public ArrayList<Object> objects = new ArrayList<Object> ();

    public void push(Object object)  {  objects.add(object);  }

    @SuppressWarnings("unchecked")
    public <T> T pop()  {
        if (objects.size()==0)  throw new RuntimeException (NO_STACK_VALUES);
        return (T) objects.remove(objects.size()-1);
    }

    @SuppressWarnings("unchecked")
    public <T> T top()  {
        if (objects.size()==0)  throw new RuntimeException (NO_STACK_VALUES);
        return (T) objects.get(objects.size()-1);
    }

    public String currentName()  {  return path.substring(getPathPosition()+1);  }

    public String parentPath()  {  return path.substring(0, getPathPosition());  }

    public int getPathPosition()
    {
        for (int i=path.length(); ; )  {
            i--;
            if (path.charAt(i)=='@' || path.charAt(i)=='/')  return i;
        }
    }


    //                --------    test    --------

    public static class Test
    {
        static void test_clear(XMLParser parser, String xml) throws Exception {
            parser.parse(xml);
        }

        static void test_error(XMLParser parser, String xml, String errorCode, String path) throws Exception
        {
            String error = "";
            if (path!=null)  error += ": " + path;
            if (errorCode!=null)  error += ": " + errorCode;

            try  {  parser.parse(xml);  throw new Exception ("this test must be final with Exception"+error);  }
            catch (XMLException e)  {
                if (e.code==errorCode)  if (path==null ? e.path==null : path.equals(e.path))  return;
                throw new Exception("another error expected"+error, e);
            }
            catch (SAXException e)  {
                if (errorCode!=null)  throw new Exception("another error expected"+error, e);
            }
        }

        public static void main(String[] args) throws Exception
        {
            XMLParser parser = new XMLParser ();

            //    basic test document
            test_error(parser, "", NO_DOCUMENT_RULE, "");
            parser.document = new Document ();
            test_error(parser, "", BAD_XML, "");
            test_error(parser, "<root></root>", NO_RULE, "/root");

            //    basic test elements
            parser.document = new Document (Element.root("root"));
            test_clear(parser, "<root></root>");
            test_error(parser, "<xml></xml>", NO_RULE, "/xml");
            test_error(parser, "<root></root><root></root>", BAD_XML, "");

            parser.document = new Document (Element.root ("root", new Element ("sub")));
            test_clear(parser, "<root><sub></sub></root>");
            test_error(parser, "<root><sub></sub><item></item></root>", NO_RULE, "/root/item");
            test_error(parser, "<root><sub><sub></sub></sub></root>", NO_RULE, "/root/sub/sub");

            //    test different roots
            parser.document = new Document (
                Element.root ("root1",
                    new Element ("itemA"),
                    new Element ("itemB")
                ),
                Element.root ("root2",
                    new Element ("itemA"),
                    new Element ("itemC")
                )
            );
            test_clear(parser, "<root1><itemA></itemA><itemB></itemB></root1>");
            test_clear(parser, "<root2><itemA></itemA><itemC></itemC></root2>");
            test_error(parser, "<root1><itemA></itemA><itemB></itemB><itemC></itemC></root1>", NO_RULE, "/root1/itemC");
            test_error(parser, "<root2><itemA></itemA><itemB></itemB><itemC></itemC></root2>", NO_RULE, "/root2/itemB");

            //    test elements occurrence
            parser.document = new Document (Element.root ("root",
                    Element.required ("req"),
                    Element.optional ("opt"),
                    Element.zeroOrMore ("more0"),
                    Element.oneOrMore ("more1")
            ));
            test_error(parser, "<root><more1></more1></root>", NO_ONE_ELEMENT, "/root/req");
            test_error(parser, "<root><req></req></root>", NO_ONE_ELEMENT, "/root/more1");
            test_clear(parser, "<root><req></req><more1></more1></root>");
            test_error(parser, "<root><req></req><more1></more1><req></req></root>", MORE_ONE_ELEMENT, "/root/req");
            test_clear(parser, "<root><opt></opt><req/><more1/></root>");
            test_error(parser, "<root><opt></opt><opt></opt><req/></root>", MORE_ONE_ELEMENT, "/root/opt");
            test_clear(parser, "<root><more1/><more0></more0><opt></opt><req/></root>");
            test_clear(parser, "<root><more0></more0><opt></opt><req/><more0></more0><more1/></root>");
            //repeat
            test_clear(parser, "<root><more0></more0><opt></opt><req/><more0></more0><more1/></root>");
            test_error(parser, "<root><opt></opt><opt></opt><req/></root>", MORE_ONE_ELEMENT, "/root/opt");
            test_error(parser, "<root><req></req></root>", NO_ONE_ELEMENT, "/root/more1");
            test_error(parser, "<root><more1></more1></root>", NO_ONE_ELEMENT, "/root/req");

            //    value
            parser.document = new Document (
                Element.root ("root",
                    Element.optional ("item0"),
                    new MatchRule("value", Element.optional ("item1")),
                    new MatchRule("value", new MatchRule("value", Element.optional ("item2"))),
                    new MatchRule("valueA", new MatchRule("valueB", Element.optional ("item3"))),
                    new PatternMatchRule("[a-zA-Z_\\d\\.\\-]+@[a-zA-Z\\.]+", Element.optional ("item4")),
                    new PatternMatchRule("[a-zA-Z_\\d]+", new MatchRule("value", Element.optional ("item5"))),
                    new IgnoreBody (Element.optional ("item6")),
                    new MatchRule("", Element.optional ("item7"))
                )
            );
            test_clear(parser, "<root><item0/></root>");
            test_error(parser, "<root><item0>value</item0>></root>", UNEXPECTED_VALUE, "/root/item0");
            test_error(parser, "<root><item1/></root>", VALUE_NOT_MATCH, "/root/item1");
            test_error(parser, "<root><item1>xxx</item1></root>", VALUE_NOT_MATCH, "/root/item1");
            test_clear(parser, "<root><item1>value</item1></root>");
            test_clear(parser, "<root><item2>value</item2></root>");
            test_error(parser, "<root><item3>valueA</item3></root>", VALUE_NOT_MATCH, "/root/item3");
            test_error(parser, "<root><item3>valueB</item3></root>", VALUE_NOT_MATCH, "/root/item3");
            test_clear(parser, "<root><item4>test.nbki.collat_auto@yanex.ru</item4></root>");
            test_error(parser, "<root><item4>test.nbki.collat_auto@@yanex.ru</item4></root>", VALUE_NOT_MATCH, "/root/item4");
            test_clear(parser, "<root><item5>value</item5></root>");
            test_clear(parser, "<root><item6>value xxx bla bla bla</item6></root>");
            test_clear(parser, "<root><item7></item7></root>");
            test_error(parser, "<root><item7>value</item7></root>", VALUE_NOT_MATCH, "/root/item7");

            //    attributes
            parser.document = new Document (
                Element.root ("root",
                    Element.optional ("item1",
                        new MatchRule ("valueA", Attribute.required ("a1")),
                        new MatchRule ("valueB", Attribute.optional ("a2"))
                    ),
                    new MatchRule ("text", Element.optional ("item2",
                        new MatchRule ("aaa", new Attribute ("a1"))
                    ))
                )
            );
            test_error(parser, "<root><item1/></root>", NO_ONE_ELEMENT, "/root/item1@a1");
            test_error(parser, "<root><item1/><a1/></root>", NO_ONE_ELEMENT, "/root/item1@a1");
            test_error(parser, "<root><item1 a1=''/></root>", VALUE_NOT_MATCH, "/root/item1@a1");
            test_clear(parser, "<root><item1 a1='valueA'/></root>");
            test_clear(parser, "<root><item1 a1='valueA' a2='valueB'/></root>");
            test_error(parser, "<root><item1 a1='valueA' a2='valueB' a3=''/></root>", NO_RULE, "/root/item1@a3");
            test_clear(parser, "<root><item1 a1='valueA' a2='valueB'/><item2 a1='aaa'>text</item2></root>");

            //    ignore
            parser.document = new Document (
                Element.root("root",
                    Element.optional("sub1",
                        Element.required("item1"),
                        new IgnoreAttributes (Element.optional ("item2")),
                        new IgnoreElements (Element.optional ("item3")),
                        new IgnoreBody (Element.optional ("item4"))
                    ),
                    Element.optional("sub2",
                        new IgnoreChilds (new IgnoreBody (Element.optional("item",
                            new MatchRule ("value1", Attribute.optional("a")),
                            new MatchRule ("value2", Element.optional("a"))
                        )))
                    )
                )
            );
            test_error(parser, "<root><sub1></sub1></root>", NO_ONE_ELEMENT, "/root/sub1/item1");
            test_clear(parser, "<root><sub1><item1/></sub1></root>");
            test_clear(parser, "<root><sub1><item1/><item2 id='xxx' attr='value'/></sub1></root>");
            test_error(parser, "<root><sub1><item1/><item2 id='xxx'>value</item2></sub1></root>", UNEXPECTED_VALUE, "/root/sub1/item2");
            test_error(parser, "<root><sub1><item1/><item2 id='xxx'><a/></item2></sub1></root>", NO_RULE, "/root/sub1/item2/a");
            test_error(parser, "<root><sub1><item1/><item3 id='xxx'>value</item3></sub1></root>", NO_RULE, "/root/sub1/item3@id");
            test_error(parser, "<root><sub1><item1/><item3>value</item3></sub1></root>", UNEXPECTED_VALUE,
                    "/root/sub1/item3");
            test_clear(parser,
                    "<root><sub1><item1/><item3><a><any>any value bla bla bla</any></a></item3></sub1></root>");
            test_error(parser, "<root><sub1><item1/><item4 id='xxx'>value</item4></sub1></root>", NO_RULE,
                    "/root/sub1/item4@id");
            test_clear(parser, "<root><sub1><item1/><item4>value</item4></sub1></root>");
            test_error(parser, "<root><sub1><item1/><item4><a>any value</a></item4></sub1></root>", NO_RULE, "/root/sub1/item4/a");
            test_error(parser, "<root><sub2><item a='value'><a>value</a></item></sub2></root>", VALUE_NOT_MATCH, "/root/sub2/item@a");
            test_error(parser, "<root><sub2><item a='value1'><a>value</a></item></sub2></root>", VALUE_NOT_MATCH,
                    "/root/sub2/item/a");
            test_clear(parser, "<root><sub2><item a='value1'><a>value2</a></item></sub2></root>");
            test_error(parser, "<root><sub2><item a='value1'><a a=''>value2</a></item></sub2></root>", NO_RULE, "/root/sub2/item/a@a");
            test_error(parser, "<root><sub2><item a='value1'><a>value2<b/></a></item></sub2></root>", NO_RULE,
                    "/root/sub2/item/a/b");
            test_clear(parser,
                    "<root><sub2><item a='value1' b='zzzzz'> bla <a>value2</a> bla <b><c>ssss</c></b> bla </item></sub2></root>");


        }


    }
}
