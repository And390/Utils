package utils.jsonparser;


/**
 * User: And390
 * Date: 12.10.14
 * Time: 3:05
 */
public class DupRule extends EndRule
{
    public DupRule(Rule next)  {  super(next);  }

    @Override
    public void end(JSONParser parser) throws JSONException
    {
        Object value = parser.peek();
        parser.push(value);
    }


    public static class Before extends BeginRule
    {
        public Before(Rule next)  {  super(next);  }

        @Override
        public void begin(JSONParser parser) throws JSONException
        {
            Object value = parser.peek();
            parser.push(value);
        }
    }
}
