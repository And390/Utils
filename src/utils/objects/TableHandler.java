package utils.objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: And390
 * Date: 09.08.14
 * Time: 0:22
 */
public interface TableHandler<Ex extends Exception>
{
    public void processRow(int rowIndex) throws Ex;
    public void process(int rowIndex, int colIndex, String value) throws Ex;

    public interface E extends TableHandler<Exception>  {}
    public interface R extends TableHandler<RuntimeException>  {}


    public static abstract class List<Ex extends Exception> implements TableHandler<Ex>
    {
        public final ArrayList<String> row = new ArrayList<String> ();

        @Override
        public void process(int rowIndex, int colIndex, String value) throws Ex  {
            if (colIndex==0)  row.clear();
            row.add(value);
        }
    }
    public abstract class ListE extends List<Exception>  {}
    public abstract class ListR extends List<RuntimeException>  {}


    public static abstract class Headed extends TableHandler.List<Exception>
    {
        public final boolean ignoreCase;
        public final HashMap<String, Integer> header = new HashMap<String, Integer> ();

        public Headed()  {  ignoreCase = true;  }
        public Headed(boolean ignoreCase_)  {  ignoreCase = ignoreCase_;  }

        @Override
        public void processRow(int rowIndex) throws Exception
        {
            if (rowIndex==0)  {
                //    перва€ строка заголовок
                for (String value : row)  if (header.put(ignoreCase ? value.toLowerCase() : value, header.size())!=null)
                    throw new Exception ("«аголовок повтор€етс€: "+value);
                return;
            };
            if (row.size()!=header.size())  throw new Exception (" оличество колонок в строке "+(rowIndex+1)+
                    " не соответствует заголовку: "+row.size()+"<>"+header.size());
        }

        public String get(String name) throws Exception  {
            Integer i = header.get(ignoreCase ? name.toLowerCase() : name);
            if (i==null)  throw new Exception ("«аголовок не найден: "+name);
            return row.get(i);
        }
    }
}
