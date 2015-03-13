package utils.xmlparser;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 17:07
 */
public class CreateRule extends WrapRule
{
    public final Class class_;
    public CreateRule(Class class_, Rule inner)  {  super(inner);  this.class_=class_;  }

    @Override
    public BodyHandler begin(XMLParser parser)
    {
        try  {  parser.push(class_.newInstance());  }
        catch (RuntimeException e)  {  throw e;  }
        catch (Exception e)  {  throw new RuntimeException (e);  }
        return super.begin(parser);
    }
}
