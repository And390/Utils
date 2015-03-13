package utils.jsonparser;

import java.util.Arrays;

/**
 * ѕравило дл€ "последовательного" соединени€ правил разбора объектов.
 * ’ранит список правил, при разборе полей передает управление первому, когда оно вернет null (поле не найдено),
 *  то дл€ следующих полей будет передавать управление следующему элементу списка, и так далее.
 * “аким образом, можно соедин€ть ObjectRule и ObjectOrdererRule и, например, реализовать обработка объекта,
 * в котором по€вл€ютс€ сначала пол€ A или B, затем C или D
 * User: And390
 * Date: 16.11.14
 * Time: 0:19
 */
public class ObjectSeqRule extends WrapRule.WithData
{
    private Rule[] rules;
    private int requiredCount;


    public ObjectSeqRule(int optionalCount, Rule... values)
    {
        this(null, optionalCount, values);
    }

    public ObjectSeqRule(Rule next, int optionalCount, Rule... rules_)
    {
        super(next);

        rules = rules_;
        if (optionalCount<0 || optionalCount>=rules_.length)  throw new IllegalArgumentException ("optionalCount must be positive value less than childs count: "+optionalCount);
        requiredCount = rules_.length - optionalCount;
    }

    public ObjectSeqRule add(String fieldName, boolean isRequired, Rule childRule)
    {
        if (isRequired && requiredCount!=rules.length)  throw new IllegalStateException ("Can not add required rule because last rule is not required");
        //    expand array
        rules = Arrays.copyOf(rules, rules.length+1);
        //    set new value
        rules[rules.length-1] = childRule;
        if (isRequired)  requiredCount++;
        return this;
    }


    @Override
    public Data objectBegin(JSONParser parser) throws JSONException
    {
        //    init inner first
        Data data = next==null ? null : next.objectBegin(parser);
        //    create our data and return
        return new ObjectData (data);
    }

    @Override
    public void objectEnd(JSONParser parser, Data data) throws JSONException
    {
        int index = ((ObjectData)data).index;
        if (index>=0 && index<rules.length)  rules[index].objectEnd(parser, ((ObjectData)data).childData);
        //    check required fields
        if (index+1<requiredCount)  throw new JSONException (parser, JSONParser.NO_FIELD);  //can not provide more detailed message
        //    end inner
        if (next!=null)  next.objectEnd(parser, ((ObjectData)data).next);
    }

    @Override
    public Rule objectField(JSONParser parser, Data data_, String name) throws JSONException
    {
        ObjectData data = (ObjectData)data_;
        for (; data.index!=rules.length; )  {
            if (data.index!=-1)  {
                Rule child = rules[data.index].objectField(parser, data.childData, name);
                if (child!=null)  return child;
                rules[data.index].objectEnd(parser, data.childData);
            }
            data.index++;
            if (data.index==rules.length)  break;
            data.childData = rules[data.index].objectBegin(parser);
        }
        return next==null ? null : next.objectField(parser, data.next, name);
    }


    public static class ObjectData extends WrapData {
        public int index = -1;
        public Data childData = null;
        public ObjectData(Data next_)  {  super(next_);  }
    }
}
