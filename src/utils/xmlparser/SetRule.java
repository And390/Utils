package utils.xmlparser;

import java.lang.reflect.Field;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 15:39
 */
public class SetRule extends ValueRule
{
    public final String name;
    public SetRule(Rule inner)  {  super(inner);  this.name=null;  }
    public SetRule(String name, Rule inner)  {  super(inner);  this.name=name;  }

    public Object value(String text)  {  return text;  }

    @Override
    public void end(XMLParser parser, String text)
    {
        super.end(parser, text);
        set(parser, name, value(text));
    }


    public static void set(XMLParser parser, String name, Object value)
    {
        Object object = parser.top();
        try  {  field(parser, object, name).set(object, value);  }
        catch (RuntimeException e)  {
            throw new RuntimeException ("can't write field "+name+"\nvalue ("+value.getClass().getCanonicalName()+"): "+value+
                    "\nto object ("+object.getClass().getCanonicalName()+"): "+object, e);
        }
        catch (Exception e)  {
            throw new RuntimeException ("can't write field "+name+"\nvalue ("+value.getClass().getCanonicalName()+"): "+value+
                    "\nto object ("+object.getClass().getCanonicalName()+"): "+object, e);
        }
    }

    public static Field field(XMLParser parser, Object object, String name) throws NoSuchFieldException
    {
        if (name==null)  name = SetRule.transformName(parser.currentName());
        return object.getClass().getField(name);
    }

    public static String transformName(String name)
    {
        StringBuilder result=null;
        int i0=0;
        for (int i=0; i!=name.length(); i++)
            if (name.charAt(i)=='-' || name.charAt(i)=='.')
            {
                if (result==null)  result = new StringBuilder();
                if (i0!=0 && i!=i0)  {  result.append(Character.toUpperCase(name.charAt(i0)));  i0++;  }
                result.append(name.substring(i0, i));
                i0 = i+1;
            }
        if (i0!=0 && i0!=name.length())  {  result.append(Character.toUpperCase(name.charAt(i0)));  i0++;  }
        if (result!=null)  {  result.append(name.substring(i0));  return result.toString();  }
        else  return name;
    }



    public static class Test
    {
        public static void test(String name, String expected)
        {
            String result = transformName(name);
            System.out.println(result);
            if (!result.equals(expected))  throw new RuntimeException ("value are not match, must be "+expected);
        }

        public static void main(String[] args)
        {
            test("sortname", "sortname");
            test("sort-name", "sortName");
            test("a-sort-name-x", "aSortName");
            test("sort--name", "sortName");
            test("sort-name-", "sortName");
            test("-sort-name", "sortName");
            test("--sort-name--", "sortName");
            test(".a--sort.name..x.", "aSortNameX");
        }
    }
}
