package utils.jsonparser;

/**
 * User: And390
 * Date: 10.10.14
 * Time: 2:53
 */
public abstract class Rule
{
    public Data objectBegin(JSONParser parser) throws JSONException  {  throw new JSONException (parser, JSONParser.UNEXPECTED_OBJECT);  }
    public void objectEnd(JSONParser parser, Data data) throws JSONException  {  throw new JSONException (parser, JSONParser.UNEXPECTED_OBJECT_END);  }  //must not be executed
    public Rule objectField(JSONParser parser, Data data, String name) throws JSONException  {  return null;  }
    public Data arrayBegin(JSONParser parser) throws JSONException  {  throw new JSONException (parser, JSONParser.UNEXPECTED_ARRAY);  }
    public void arrayEnd(JSONParser parser, Data data) throws JSONException  {  throw new JSONException (parser, JSONParser.UNEXPECTED_ARRAY_END);  }  //must not be executed
    public Rule arrayItem(JSONParser parser, Data data, int index) throws JSONException  {  return null;  }
    public void nullValue(JSONParser parser) throws JSONException  {  throw new JSONException (parser, JSONParser.UNEXPECTED_NULL);  }
    public void booleanValue(JSONParser parser, boolean value) throws JSONException  {  throw new JSONException (parser, JSONParser.UNEXPECTED_BOOLEAN, value);  }
    public void stringValue(JSONParser parser, JSONParser.StringToken value) throws JSONException  {  throw new JSONException (parser, JSONParser.UNEXPECTED_STRING, value);  }
    public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException  {  throw new JSONException (parser, JSONParser.UNEXPECTED_NUMBER, value);  }

    public static class Data  {}
}
