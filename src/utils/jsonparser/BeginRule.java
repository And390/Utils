package utils.jsonparser;

/**
 * User: And390
 * Date: 12.10.14
 * Time: 3:21
 */
public abstract class BeginRule extends WrapRule
{
    public abstract void begin(JSONParser parser) throws JSONException;

    public BeginRule(Rule next)  {  super(next);  }

    @Override
    public Data objectBegin(JSONParser parser) throws JSONException
    {
        begin(parser);
        return super.objectBegin(parser);
    }

    @Override
    public Data arrayBegin(JSONParser parser) throws JSONException
    {
        begin(parser);
        return super.arrayBegin(parser);
    }

    @Override
    public void nullValue(JSONParser parser) throws JSONException
    {
        begin(parser);
        super.nullValue(parser);
    }

    @Override
    public void booleanValue(JSONParser parser, boolean value) throws JSONException
    {
        begin(parser);
        super.booleanValue(parser, value);
    }

    @Override
    public void stringValue(JSONParser parser, JSONParser.StringToken value) throws JSONException
    {
        begin(parser);
        super.stringValue(parser, value);
    }

    @Override
    public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException
    {
        begin(parser);
        super.numberValue(parser, value);
    }
}
