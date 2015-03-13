package utils.jsonparser;


import java.lang.reflect.Constructor;

/**
 * User: And390
 * Date: 12.10.14
 * Time: 1:15
 */
public class CreateRule extends WrapRule
{
    //public final Class class_;
    public final Constructor constructor;
    public final int parametersCount;
    public CreateRule(Class class_, Rule next)  throws NoSuchMethodException, SecurityException  {
        this(class_, new Class [0], next);
    }
    public CreateRule(Class class_, Class[] parameters, Rule next) throws NoSuchMethodException, SecurityException  {
        super(next);
        //this.class_=class_;
        //arguments=arguments_;
        constructor = class_.getConstructor(parameters);
        parametersCount = parameters.length;
    }

    @Override
    public Data objectBegin(JSONParser parser) throws JSONException
    {
        try  {
            //parser.push(class_.newInstance());
            Object[] parameters = new Object [parametersCount];
            for (int i=0; i<parameters.length; i++)  parameters[i] = parser.<Object>pop();
            parser.push(constructor.newInstance(parameters));
        }
        catch (RuntimeException e)  {  throw e;  }  //TODO use line:col info here in execptions
        catch (Exception e)  {  throw new InternalException (e);  }
        return super.objectBegin(parser);
    }
}
