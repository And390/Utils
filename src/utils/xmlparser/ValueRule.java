package utils.xmlparser;

/**
 * User: And390
 * Date: 24.09.12
 * Time: 1:42
 */
public class ValueRule extends WrapRule
{
    public ValueRule(Rule inner)  {  super(inner);  }

    @Override
    public utils.xmlparser.BodyHandler begin(XMLParser parser)
    {
        utils.xmlparser.BodyHandler innerBodyHandler = super.begin(parser);
        if (innerBodyHandler!=Element.BODY_HANDLER)  return innerBodyHandler;
        BodyHandler bodyHandler = parser.getData("BodyHandler");
        if (bodyHandler==null)  {
            bodyHandler = new BodyHandler();
            parser.setData("BodyHandler", bodyHandler);
        }
        bodyHandler.builder.setLength(0);
        return bodyHandler;
    }

    public static class BodyHandler implements utils.xmlparser.BodyHandler
    {
        public StringBuilder builder = new StringBuilder ();

        @Override
        public void characters(XMLParser parser, char[] ch, int start, int length)
        {
            builder.append(ch, start, length);
        }

        @Override
        public String getValue()
        {
            return builder.toString();
        }
    }


    // !!! Это ошибочная версия, так как передаваемый массив может в дальнейшем изменяться SAX-парсером
//    public static class BodyHandler implements utils.xmlparser.BodyHandler
//    {
//        public static class DataPart
//        {
//            public DataPart last;
//            public char[] ch;
//            public int start;
//            public int length;
//
//            public DataPart (DataPart last, char[] ch, int start, int length)  {
//                this.last = last;
//                this.ch = ch;
//                this.start = start;
//                this.length = length;
//            }
//        }
//
//        public DataPart data;
//
//        @Override
//        public void characters(XMLParser parser, char[] ch, int start, int length)
//        {
//            data = new DataPart(data, ch, start, length);
//        }
//
//        @Override
//        public String getValue()
//        {
//            if (data==null)  return "";
//            else if (data.last==null)  return new String (data.ch, data.start, data.length);
//            else  {
//                int n=0;
//                for (DataPart part=data; part!=null; part=part.last)  n += part.length;
//                char[] buffer = new char [n];
//                for (DataPart part=data;  part!=null; part=part.last)  {
//                    n -= data.length;
//                    System.arraycopy(data.ch, data.start, buffer, n, data.length);
//                }
//                return new String (buffer);
//            }
//        }
//    }
}
