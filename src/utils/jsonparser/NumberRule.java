package utils.jsonparser;

/**
 * User: And390
 * Date: 16.11.14
 * Time: 23:37
 */
public class NumberRule extends ExpectedRule
{
    public NumberRule()  {  super(JSONParser.NUMBER_EXPECTED);  }

    @Override
    public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException
    {
    }


    public static class Push extends NumberRule
    {
        @Override
        public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException
        {
            parser.push(value.number);
        }
    }

    public static class PushLong extends NumberRule
    {
        public PushLong()  {  errorCode=JSONParser.LONG_EXPECTED;  }

        @Override
        public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException
        {
            long result = (long)value.number;
            if (result!=value.number)  throw new JSONException (parser, errorCode, ", but found: ", value);
            parser.push(value.number);
        }
    }
}
