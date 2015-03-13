package utils.jsonparser;

/**
 * User: And390
 * Date: 12.10.14
 * Time: 0:29
 */
public abstract class IgnoreRule extends Rule
{
    public static final Value VALUE = new Value ();
    public static class Value extends Rule  {
        @Override
        public void nullValue(JSONParser parser) throws JSONException  {}  //do nothing
        @Override
        public void booleanValue(JSONParser parser, boolean value) throws JSONException  {}  //do nothing
        @Override
        public void stringValue(JSONParser parser, JSONParser.StringToken value) throws JSONException  {}  //do nothing
        @Override
        public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException  {}  //do nothing
    }

    public static final PlainObject PLAIN_OBJECT = new PlainObject ();
    public static class PlainObject extends Rule  {
        @Override
        public Data objectBegin(JSONParser parser) throws JSONException  {  return null;  }  //do nothing
        @Override
        public void objectEnd(JSONParser parser, Data data) throws JSONException  {}  //do nothing
        @Override
        public Rule objectField(JSONParser parser, Data data, String name) throws JSONException  {  return VALUE;  }
    }

    public static final ObjectRecursive OBJECT_RECURSIVE = new ObjectRecursive ();
    public static class ObjectRecursive extends PlainObject  {
        @Override
        public Rule objectField(JSONParser parser, Data data, String name) throws JSONException  {  return ALL_RECURSIVE;  }
    }

    public static final PlainArray PLAIN_ARRAY = new PlainArray ();
    public static class PlainArray extends IgnoreRule  {
        @Override
        public Data arrayBegin(JSONParser parser) throws JSONException  {  return null;  }  //do nothing
        @Override
        public void arrayEnd(JSONParser parser, Data data) throws JSONException  {}  //do nothing
        @Override
        public Rule arrayItem(JSONParser parser, Data data, int index) throws JSONException  {  return VALUE;  }
    }

    public static final ArrayRecursive ARRAY_RECURSIVE = new ArrayRecursive ();
    public static class ArrayRecursive extends PlainArray  {
        @Override
        public Rule arrayItem(JSONParser parser, Data data, int index) throws JSONException  {  return ALL_RECURSIVE;  }
    }

    public static final AllRecursive ALL_RECURSIVE = new AllRecursive();
    public static class AllRecursive extends Value  {
        @Override
        public Data objectBegin(JSONParser parser) throws JSONException  {  return null;  }  //do nothing
        @Override
        public void objectEnd(JSONParser parser, Data data) throws JSONException  {}  //do nothing
        @Override
        public Rule objectField(JSONParser parser, Data data, String name) throws JSONException  {  return this;  }
        @Override
        public Data arrayBegin(JSONParser parser) throws JSONException  {  return null;  }  //do nothing
        @Override
        public void arrayEnd(JSONParser parser, Data data) throws JSONException  {}  //do nothing
        @Override
        public Rule arrayItem(JSONParser parser, Data data, int index) throws JSONException  {  return this;  }
    }
}
