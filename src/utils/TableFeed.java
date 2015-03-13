package utils;

import java.io.IOException;

/**
 * User: And390
 * Date: 08.08.14
 * Time: 22:26
 */
public interface TableFeed<E extends Exception>
{
    public String[] nextRow(int rowIndex) throws E;  //must return null if end of table
    public boolean isRightAligned(int rowIndex, int colIndex) throws E;

    public interface RT extends TableFeed<RuntimeException> {}
}
