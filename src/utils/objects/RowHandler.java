package utils.objects;

import java.util.ArrayList;

/**
 * User: And390
 * Date: 09.08.14
 * Time: 16:25
 */
public abstract class RowHandler implements TableHandler
{
    public final ArrayList<String> row = new ArrayList<String> ();

        @Override
        public void process(int rowIndex, int colIndex, String value)  {
            if (colIndex==0)  row.clear();
            row.add(value);
        }

        public abstract class RT extends List<RuntimeException>  {}
}
