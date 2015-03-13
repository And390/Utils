package utils.jsonparser;

/**
 * User: And390
 * Date: 11.10.14
 * Time: 13:55
 */
public class WrapRule extends Rule
{
    public Rule next;
    public WrapRule()  {  next=null;  }
    public WrapRule(Rule next_)  {  next=next_;  }

    @Override
    public Data objectBegin(JSONParser parser) throws JSONException  {
        return next==null ? super.objectBegin(parser) : next.objectBegin(parser);
    }

    @Override
    public void objectEnd(JSONParser parser, Data data) throws JSONException  {
        if (next!=null)  next.objectEnd(parser, data);
        else  super.objectEnd(parser, data);
    }

    @Override
    public Rule objectField(JSONParser parser, Data data, String name) throws JSONException  {
        return next==null ? super.objectField(parser, data, name) : next.objectField(parser, data, name);
    }

    @Override
    public Data arrayBegin(JSONParser parser) throws JSONException  {
        return next==null ? super.arrayBegin(parser) : next.arrayBegin(parser);
    }

    @Override
    public void arrayEnd(JSONParser parser, Data data) throws JSONException  {
        if (next!=null)  next.arrayEnd(parser, data);
        else  super.arrayEnd(parser, data);
    }

    @Override
    public Rule arrayItem(JSONParser parser, Data data, int index) throws JSONException  {
        return next==null ? super.arrayItem(parser, data, index) : next.arrayItem(parser, data, index);
    }

    @Override
    public void nullValue(JSONParser parser) throws JSONException
    {
        if (next!=null)  next.nullValue(parser);
        else  super.nullValue(parser);
    }

    @Override
    public void booleanValue(JSONParser parser, boolean value) throws JSONException
    {
        if (next!=null)  next.booleanValue(parser, value);
        else  super.booleanValue(parser, value);
    }

    @Override
    public void stringValue(JSONParser parser, JSONParser.StringToken value) throws JSONException
    {
        if (next!=null)  next.stringValue(parser, value);
        else  super.stringValue(parser, value);
    }

    @Override
    public void numberValue(JSONParser parser, JSONParser.NumberToken value) throws JSONException
    {
        if (next!=null)  next.numberValue(parser, value);
        else  super.numberValue(parser, value);
    }

    public static class WrapData extends Data  {
        public Data next;
        public WrapData()  {  next=null;  }
        public WrapData(Data next_)  {  next=next_;  }
    }

    public static class WithData extends WrapRule
    {
        public WithData()  {  super();  }
        public WithData(Rule next_)  {  super(next_);  }

        // Этот метод имеет мало практической ценности, так как класс WrapData бесполезен сам по себе,
        // потомки должны перезаписывать его
        @Override
        public Data objectBegin(JSONParser parser) throws JSONException  {
            return new WrapData (super.objectBegin(parser));
        }

        @Override
        public void objectEnd(JSONParser parser, Data data) throws JSONException  {
            super.objectEnd(parser, ((WrapData)data).next);
        }

        @Override
        public Rule objectField(JSONParser parser, Data data, String name) throws JSONException  {
            return super.objectField(parser, ((WrapData)data).next, name);
        }

        // Этот метод имеет мало практической ценности, так как класс WrapData бесполезен сам по себе,
        // потомки должны перезаписывать его
        @Override
        public Data arrayBegin(JSONParser parser) throws JSONException  {
            return new WrapData (super.arrayBegin(parser));
        }

        @Override
        public void arrayEnd(JSONParser parser, Data data) throws JSONException  {
            super.arrayEnd(parser, ((WrapData)data).next);
        }

        @Override
        public Rule arrayItem(JSONParser parser, Data data, int index) throws JSONException  {
            return super.arrayItem(parser, ((WrapData)data).next, index);
        }
    }
}
