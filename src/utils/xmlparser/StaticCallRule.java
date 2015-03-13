package utils.xmlparser;

import java.lang.reflect.Method;

/**
 * User: And390
 * Date: 26.09.12
 * Time: 20:50
 */
public class StaticCallRule extends ValueRule
{
    public final Method method;

    public StaticCallRule(Class class_, String methodName, Rule inner)
    {
        super(inner);
        try  {  this.method = class_.getMethod(methodName, String.class);  }
        catch (RuntimeException e)  {  throw e;  }
        catch (Exception e)  {  throw new RuntimeException (e);  }
    }

    @Override
    public void end(XMLParser parser, String value)
    {
        super.end (parser, value);
        try  {
            Object result = method.invoke(null, value);
            if (method.getReturnType()!=Void.TYPE)  parser.push(result);
        }
        catch (RuntimeException e)  {  throw e;  }
        catch (Exception e)  {  throw new RuntimeException (e);  }
    }
}
