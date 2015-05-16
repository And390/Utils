import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * User: andreyzaharov
 * Date: 01.11.2011
 * Time: 16:06:32
 */
public class Test {

    public static boolean a()  {
        System.out.println("a");
        return true;
    }

    public static boolean b()  {
        System.out.println("b");
        return false;
    }

    public static void main(String[] args)
    {
        System.out.println(a() | b());
        System.out.println(a() || b());
    }
}
