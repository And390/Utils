package utils.objects;

/**
 * User: And390
 * Date: 08.08.14
 * Time: 22:26
 */
public interface TableFeed<Ex extends Exception>
{
    public String[] nextRow(int rowIndex) throws Ex;  //must return null if end of table
    public boolean isRightAligned(int rowIndex, int colIndex) throws Ex;

    public interface RT extends TableFeed<RuntimeException> {}
}
