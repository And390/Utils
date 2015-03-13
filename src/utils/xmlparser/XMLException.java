package utils.xmlparser;

/**
 * User: And390
 * Date: 04.09.12
 * Time: 12:31
 */
public class XMLException extends RuntimeException
{
    public final String code;
    public final String path;
    public final int line;
    public final int col;
    public final String message;

    public XMLException(String code, XMLParser parser)  {  this(code, null, parser, "", null);  }
    public XMLException(String code, String subMessage, XMLParser parser)  {  this(code, subMessage, parser, "", null);  }
    //public XMLException(String code, XMLParser parser, String subPath)  {  this(code, null, parser, subPath, null);  }
    public XMLException(String code, XMLParser parser, Throwable cause)  {  this(code, null, parser, "", cause);  }

    public XMLException(String code, String subMessage, XMLParser parser, String subPath, Throwable cause)
    {
        super(cause);
        this.code=code;
        this.path=parser.path.toString()+subPath;
        if (parser.locator!=null)  {  line=parser.locator.getLineNumber();  col=parser.locator.getColumnNumber();  }
        else  {  line=0;  col=0;  }
        //    message
        StringBuilder buffer = new StringBuilder();
        if (line!=0)  buffer.append(line).append(':').append(col).append(':');
        if (path.length()!=0)  buffer.append(path).append(':');
        if (buffer.length()!=0)  buffer.append(' ');
        buffer.append(code);
        if (subMessage!=null)  buffer.append(subMessage);
        this.message = buffer.toString();
    }

    @Override
    public String getMessage() {  return message;  }
}
