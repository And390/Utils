package utils.jsonparser;

/**
 * User: And390
 * Date: 12.10.14
 * Time: 0:17
 */
public class NullableRule extends WrapRule
{
    @Override
    public void nullValue(JSONParser parser) throws JSONException  {
        //do nothing
    }
}
