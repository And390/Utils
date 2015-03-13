package utils.jsonparser;

/**
 * User: And390
 * Date: 11.10.14
 * Time: 15:44
 */
public class StringRule extends ExpectedRule
{
    public StringRule()  {  super(JSONParser.STRING_EXPECTED);  }

    @Override
    public void stringValue(JSONParser parser, JSONParser.StringToken value) throws JSONException
    {
    }


    public static class Push extends StringRule
    {
        @Override
        public void stringValue(JSONParser parser, JSONParser.StringToken value) throws JSONException
        {
            parser.push(value.toString());
        }
    }

    public static class PushLong extends StringRule
    {
        public PushLong()  {  errorCode=JSONParser.STRING_LONG_EXPECTED;  }

        @Override
        public void stringValue(JSONParser parser, JSONParser.StringToken value_) throws JSONException
        {
            String value = value_.toString();
            try  {  long result = Long.parseLong(value);
                    parser.push(result);  }
            catch (NumberFormatException e)  {  throw new JSONException (parser, errorCode, ", but found: ", value_, e);  }
        }
    }
}
