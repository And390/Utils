package utils.jsonparser;


import utils.StringList;
import utils.Util;

import java.util.ArrayList;

/**
 * User: And390
 * Date: 10.10.14
 * Time: 4:05
 */
public class JSONException extends Exception
{
    public final String code;
    public final int line;
    public final int col;
    public final String message;
    public final Object parameter;

    public JSONException(JSONParser parser, String messageCode)  {
        this(parser, messageCode, null, null, null);
    }

    public JSONException(JSONParser parser, String messageCode, Object parameter)  {
        this(parser, messageCode, ": ", parameter);
    }

    public JSONException(JSONParser parser, String messageCode, String parameterPrefix, Object parameter)  {
        this(parser, messageCode, parameterPrefix, parameter, null);
    }

    public JSONException(JSONParser parser, String messageCode, String sep, Object parameter, Exception cause)  {
        this(parser.tokenLine, parser.tokenCol, parser.parseStack, messageCode, sep, parameter, cause);
    }

    public JSONException(int line_, int col_, ArrayList<Object> stack, String messageCode)  {
        this(line_, col_, stack, messageCode, null, null, null);
    }

    public JSONException(int line_, int col_, ArrayList<Object> stack, String messageCode, String sep, Object par, Exception cause)
    {
        super(cause);
        code = messageCode;
        line = line_;
        col = col_;
        parameter = par;
        //    make message
        StringList buffer = new StringList().append(line).append(":").append(col);
        if (stack!=null && stack.size()!=0)  {
            buffer.append(":");
            for (Object item : stack)
                if (item instanceof Integer)  buffer.append(".").append(item.toString());
                else  Util.quote(item.toString(), buffer.append("."));
        }
        buffer.append(": ").append(messageCode);
        if (sep!=null)  buffer.append(sep);
        if (par!=null)  buffer.append(par);
        message = buffer.toString();
    }

    @Override
    public String getMessage() {  return message;  }
}
