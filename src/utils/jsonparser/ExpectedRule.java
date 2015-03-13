package utils.jsonparser;

/**
 * User: And390
 * Date: 16.11.14
 * Time: 23:44
 */
public class ExpectedRule extends Rule
{
    public String errorCode;
    public ExpectedRule(String errorCode_)  {  errorCode=errorCode_;  }

    @Override
    public Rule.Data objectBegin(JSONParser parser) throws JSONException  {  throw new JSONException (parser, errorCode, ", but object found", null);  }
    @Override
    public Rule.Data arrayBegin(JSONParser parser) throws JSONException  {  throw new JSONException (parser, errorCode, ", but array found", null);  }
    @Override
    public void nullValue(JSONParser parser) throws JSONException  {  throw new JSONException (parser, errorCode, ", but null found", null);  }
    @Override
    public void booleanValue(JSONParser parser, boolean value) throws JSONException  {  throw new JSONException (parser, errorCode, ", but found: ", value);  }
    @Override
    public void stringValue(JSONParser parser, JSONParser.StringToken value) throws JSONException  {  throw new JSONException (parser, errorCode, ", but string found: ", value);  }
    @Override
    public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException  {  throw new JSONException (parser, errorCode, ", but number found: ", value);  }
}
