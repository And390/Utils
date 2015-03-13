package utils;

import java.util.ArrayList;

/**
 * User: And390
 * Date: 25.12.13
 * Time: 14:45
 */
public class StringList extends ArrayList<String> implements RuntimeAppendable
{
    public StringList()  {}
    public StringList(String string)  {  add(string);  }

    // throw IndexOutOfBoundsException if newSize>size
    public void setSize(int newSize)
    {
        removeRange(newSize, size());
    }

    public StringList append(String string)  {
        add(string);
        return this;
    }

    public StringList append(Object object)  {
        add(object.toString());
        return this;
    }

    @Override
    public StringList append(CharSequence csq)  {
        add(csq.toString());
        return this;
    }

    @Override
    public StringList append(CharSequence csq, int start, int end)  {
        add(csq.subSequence(start, end).toString());
        return this;
    }

    @Override
    public StringList append(char c)  {
        add(""+c);
        return this;
    }

    public StringList append(char[] chars, int start, int len)  {
        add(new String (chars, start, len));
        return this;
    }

    public StringList append(StringList array)  {
        this.addAll(array);
        return this;
    }

    public StringList appendSep(String separator, String string)  {
        if (size()!=0)  add(separator);
        add(string);
        return this;
    }

    public StringList attach(String value)  {
        this.add(0, value);
        return this;
    }

    //    аналог javascript: Array.join
//    @Override
//    public String toString()
//    {
//        if (size()==0)  return "";
//        if (size()==1)  return get(0);
//        //    calculate capacity
//        int len = length();
//        //    write all items to buffer
//        char[] buffer = new char [len];
//        getChars(buffer, 0);
//        return new String(buffer);
//    }
//
//    //    длина всех символов
//    public int length()  {
//        int len = 0;
//        for (String string : this)  len += string.length();
//        return len;
//    }
//
//    //    скопировать все символы подряд в буфер
//    public void getChars(char dest[], int destOffset) {
//        for (String string : this)  {
//            string.getChars(0, string.length(), dest, destOffset);
//            destOffset += string.length();
//        }
//    }

    @Override
    public String toString()
    {
        return Util.toString(this);
    }
}
