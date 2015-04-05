package utils.log;

import java.io.IOException;

/**
 * User: And390
 * Date: 23.10.14
 * Time: 21:58
 */
public class ColumnMaker
{
    public int size = 10;  //column size
    public int append = 0;  //separator spaces count after column
    public int max = Integer.MAX_VALUE;  //maximum column size
    public ColumnMaker()  {}  //default size
    public ColumnMaker(int size_)  {  size=size_;  }
    public ColumnMaker(int size_, int separatorSize)  {  size=size_;  append=separatorSize;  }

    private static volatile char[] spaces = new char [50];
    static  {  for (int i=0; i<spaces.length; i++)  spaces[i] = ' ';  }

    public static char[] getSpaces(int length)
    {
        if (spaces.length<length)
            synchronized (ColumnMaker.class)  {
                if (spaces.length<length)  {
                    spaces = new char [Math.max((spaces.length * 3)/2 + 1, length)];
                    for (int i=0; i<spaces.length; i++)  spaces[i] = ' ';
                }
            }
        return spaces;
    }

    public void setMax(int max_)  {
        if (max_<append)  throw new IllegalArgumentException ("'max' column size is less than 'append' spaces");
        max = max_-append;
    }

    public String get(String string)
    {
        acquire(string.length());
        int spaces = Math.max(size - string.length(), 0) + append;
        return string + new String (getSpaces(spaces), 0, spaces);
    }

    public Appendable get(Appendable output, String string, int wrapMax) throws IOException
    {
        acquire(string.length());
        if (string.length()>max && wrapMax!=-1)  {
            int spaces = wrapMax + max + append;
            output.append(string).append("\n").append(new String (getSpaces(spaces), 0, spaces));
        }
        else  {
            int spaces = size - string.length() + append;
            output.append(string).append(new String (getSpaces(spaces), 0, spaces));
        }
        return output;
    }

    public void prepare(int size_)  {  if (size_>size)  size = size_>max ? max : size_;  }

    public void acquire(int size_)  {  if (size_>size)  size = size_+8>max ? max : size_+8;  }
}
