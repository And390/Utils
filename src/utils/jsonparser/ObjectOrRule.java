package utils.jsonparser;

import java.util.Arrays;

/**
 * ������� "���" ��� ���������� ������ ������� ��������.
 * ������ ������ ������, ��� ������� ���������� ���������� ������� �������, ������������� ������ ���� (���������� ��-null).
 * User: And390
 * Date: 17.11.14
 * Time: 0:27
 */
public class ObjectOrRule extends WrapRule
{
    private Rule[] rules;


    public ObjectOrRule(Rule next, Rule... rules_)
    {
        super(next);
        rules = rules_;
    }

    public ObjectOrRule add(String fieldName, Rule childRule)
    {
        //    expand array
        rules = Arrays.copyOf(rules, rules.length+1);
        //    set new value
        rules[rules.length-1] = childRule;
        return this;
    }


    @Override
    public Rule.Data objectBegin(JSONParser parser) throws JSONException
    {
        //    init inner first
        Rule.Data data = next==null ? null : next.objectBegin(parser);
        //    create our data and return
        return new ObjectData (data);
    }

    @Override
    public void objectEnd(JSONParser parser, Rule.Data data) throws JSONException
    {
        //    end rule of selected branch
        int index = ((ObjectData)data).index;
        if (index>=0 && index<rules.length)  rules[index].objectEnd(parser, ((ObjectData)data).childData);
        //    end inner
        if (next!=null)  next.objectEnd(parser, ((ObjectData)data).next);
    }

    @Override
    public Rule objectField(JSONParser parser, Rule.Data data_, String name) throws JSONException
    {
        ObjectData data = (ObjectData)data_;
        //    ���� ����� ��� �� ������������, ����������� ����� �����
        if (data.index==-1)  {
            for (int i=0; i<rules.length; i++)  {
                data.childData = rules[i].objectBegin(parser);
                Rule child = rules[i].objectField(parser, data.childData, name);
                if (child!=null)  {  data.index=i;  return child;  }
            }
            data.index = rules.length;
            data.childData = null;
        }
        //    ���� ����� ��� �������, �������� ���������� ��, ���� ��� ���������� ����, �� ������� ���������
        else if (data.index<rules.length)  {
            Rule child = rules[data.index].objectField(parser, data.childData, name);
            if (child!=null)  return child;
        }
        //    ���� �� ������� � ���������� �������, �������� ��������� ������� next, ��� ������� null, ���� ��� ���
        return next==null ? null : next.objectField(parser, data.next, name);
    }


    public static class ObjectData extends WrapRule.WrapData
    {
        public int index = -1;
        public Data childData = null;
        public ObjectData(Rule.Data next_)  {  super(next_);  }
    }
}
