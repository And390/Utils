package utils.log;

import utils.RuntimeAppendable;
import utils.StringList;

import java.io.IOException;
import java.util.ArrayList;

/**
 * TODO здесь лучше использовать StringBuffer для быстрого создания строк примерно одинаковой длины
 * User: And390
 * Date: 25.11.14
 * Time: 23:35
 */
public class ColumnsMaker
{
    public int separate = 2;
    public ColumnsMaker()  {}
    public ColumnsMaker(int separate_)  {  separate=separate_;  }

    public ArrayList<ColumnMaker> columns = new ArrayList<ColumnMaker> ();

    public ColumnMaker column(int index)  {
        while (index>=columns.size())  {
            ColumnMaker columnMaker = new ColumnMaker ();
            columns.add(columnMaker);
            columnMaker.append = separate;
        }
        return columns.get(index);
    }

    public Appendable get(Appendable output, String... strings) throws IOException
    {
        if (strings.length==0)  return output;
        int lineLenght = 0;
        for (int i=0; i<strings.length-1; i++)  {
            ColumnMaker column = column(i);
            column.get(output, strings[i], lineLenght);
            lineLenght += column.size + column.append;
        }
        return output.append(strings[strings.length-1]);
    }

    public RuntimeAppendable get(RuntimeAppendable output, String... strings)  {
        try  {  return (RuntimeAppendable)get((Appendable)output, strings);  }
        catch (IOException e)  {  throw new RuntimeException (e);  }
    }

    public String get(String... strings)  {  return strings.length==0 ? "" : get(new StringList(), strings).toString();  }
}
