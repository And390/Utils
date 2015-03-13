package utils.jsonparser;

/**
 * User: And390
 * Date: 12.10.14
 * Time: 3:07
 */
public abstract class EndRule extends WrapRule
{
    public abstract void end(JSONParser parser) throws JSONException;

    public EndRule(Rule next)  {  super(next);  }

    @Override
    public void objectEnd(JSONParser parser, Rule.Data data) throws JSONException
    {
        super.objectEnd(parser, data);
        end(parser);
    }

    @Override
    public void arrayEnd(JSONParser parser, Rule.Data data) throws JSONException
    {
        super.arrayEnd(parser, data);
        end(parser);
    }

    @Override
    public void nullValue(JSONParser parser) throws JSONException
    {
        super.nullValue(parser);
        end(parser);
    }

    @Override
    public void booleanValue(JSONParser parser, boolean value) throws JSONException
    {
        super.booleanValue(parser, value);
        end(parser);
    }

    @Override
    public void stringValue(JSONParser parser, JSONParser.StringToken value) throws JSONException
    {
        super.stringValue(parser, value);
        end(parser);
    }

    @Override
    public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException
    {
        super.numberValue(parser, value);
        end(parser);
    }
}
