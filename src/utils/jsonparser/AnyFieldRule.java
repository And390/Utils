package utils.jsonparser;

/**
 * User: And390
 * Date: 12.10.14
 * Time: 1:54
 */
public class AnyFieldRule extends Rule // may be WrapRule
{
    public Rule fieldRule;
    public AnyFieldRule(Rule fieldRule_)  {  fieldRule=fieldRule_;  }

    @Override
    public Data objectBegin(JSONParser parser) throws JSONException
    {
        //Data data = fieldRule.objectBegin(parser);
        return null;
    }

    @Override
    public void objectEnd(JSONParser parser, Data data) throws JSONException
    {
        //fieldRule.objectEnd(parser, data);
    }

    @Override
    public Rule objectField(JSONParser parser, Data data, String name) throws JSONException
    {
        return fieldRule;
    }


    public static class Push extends AnyFieldRule
    {
        public Push(Rule fieldRule_)  {  super(fieldRule_);  }

        @Override
        public Rule objectField(JSONParser parser, Data data, String name) throws JSONException
        {
            parser.push(name);
            return super.objectField(parser, data, name);
        }
    }
}
