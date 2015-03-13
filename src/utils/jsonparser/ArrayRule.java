package utils.jsonparser;

/**
 * User: And390
 * Date: 16.11.14
 * Time: 22:47
 */
public class ArrayRule extends WrapRule
{
    public Rule child;
    public ArrayRule(Rule child_)  {  super();  child=child_;  }
    public ArrayRule(Rule child_, Rule next_)  {  super(next_);  child=child_;  }

    @Override
    public Rule.Data arrayBegin(JSONParser parser) throws JSONException  {
        if (next!=null)  return next.arrayBegin(parser);
        return null;
    }

    @Override
    public void arrayEnd(JSONParser parser, Rule.Data data) throws JSONException  {
        if (next!=null)  next.arrayEnd(parser, data);
    }

    @Override
    public Rule arrayItem(JSONParser parser, Rule.Data data, int index) throws JSONException  {
        return child;
    }
}
