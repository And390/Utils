package utils;

import java.util.ArrayList;

/**
 * And390 - 16.05.2015
 */
public class CharsList extends ArrayList<Chars> implements RuntimeAppendable
{
    public CharsList()  {}
    public CharsList(Chars chars)  {  add(chars);  }
    public CharsList(String string)  {  add(new Chars (string));  }

    @Override
    public CharsList append(CharSequence csq)  {
        add(new Chars(csq.toString()));
        return this;
    }

    @Override
    public CharsList append(CharSequence csq, int start, int end)  {
        add(new Chars(csq.subSequence(start, end).toString()));
        return this;
    }

    @Override
    public CharsList append(char c)  {
        add(new Chars(c));
        return this;
    }

    public CharsList append(String string)  {
        add(new Chars(string));
        return this;
    }

    public CharsList append(Object object)  {
        add(new Chars(object.toString()));
        return this;
    }

    public CharsList append(char[] chars)  {
        add(new Chars(chars));
        return this;
    }

    public CharsList append(char[] chars, int start, int end)  {
        add(new Chars(chars, start, end));
        return this;
    }

    @Override
    public String toString()
    {
        //    если элементов ноль или один, сразу вернуть результат
        if (size()==0)  return "";
        if (size()==1)  return get(0).toString();
        //    посчитать длину целиком
        int len = 0;
        for (Chars chars : this)  len += chars.end - chars.pos;
        //    выделить буфер и скопировать туда байты
        char[] buffer = new char [len];
        int destOffset = 0;
        for (Chars chars : this)  {
            System.arraycopy(chars.buffer, chars.pos, buffer, destOffset, chars.end-chars.pos);
            destOffset += chars.end-chars.pos;
        }
        return new String (buffer);
    }
}
