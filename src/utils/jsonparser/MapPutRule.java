package utils.jsonparser;

import java.util.HashMap;

/**
 * User: And390
 * Date: 12.10.14
 * Time: 2:07
 */
public class MapPutRule extends EndRule
{
    public MapPutRule(Rule next)  {  super(next);  }

    @Override
    public void end(JSONParser parser) throws JSONException
    {
        Object value = parser.pop();
        Object key = parser.pop();
        HashMap<Object, Object> map = parser.<HashMap<Object, Object>>peek();
        map.put(key, value);
    }
}
