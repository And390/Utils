import utils.StringList;

import java.io.IOException;
import java.util.Random;

/**
 * —равнение производительности StringBuilder и моего StringList
 * StringList выигрывает процентов на 15% при таких усредненных параметрах (7 символов строка, 2ћб размер буфера).
 * And390 - 17.04.15.
 */
public class TestStringList
{
    private static final int BUFFER_SIZE = 4 * 1024  * 1024;
    private static final int STRING_SIZE_MULT = 7;
    private static final int STRINGS = 20000;
    private static final int LOOPS = 1000;
    private static final int ATTEMPTS = 5;

    private static Random random = new Random ();

    private static int rand(int limit)  {  return random.nextInt(limit);  }
    private static int rand(int min, int max)  {  return rand(max-min+1)+min;  }
    private static double rande()  {  return 1 / (1 - random.nextDouble()) - 1;  }

    private static long div(long a, long b)  {  return Math.round(a/(double)b);  }

    public static void main(String[] args) throws Exception
    {
        System.out.println("builder");
        long time1 = test(new StringBuilderFactory());
        System.out.println("list");
        long time2 = test(new StringListFactory ());
        System.out.println("relative speed: " + div(time1 * 100, time2) + "%");
    }

    private interface AppendableFactory  {
        Appendable createAppendable();
    }

    private static class StringBuilderFactory implements AppendableFactory  {
        public StringBuilder createAppendable()  {  return new StringBuilder ();  }
    }

    private static class StringListFactory implements AppendableFactory  {
        public StringList createAppendable()  {  return new StringList ();  }
    }

    private static char[] buffer = new char [BUFFER_SIZE];
    static  {
        for (int i=0; i<buffer.length; i++)  buffer[i] = (char) rand('a', 'z');
    }

    private static long test(AppendableFactory factory) throws IOException
    {
        long total = 0;
        for (int a=0; a<ATTEMPTS; a++)  {
            System.gc();
            long time = System.currentTimeMillis();
            int hashCode = 0;
            long totalBufferLength = 0;
            for (int l=0; l<LOOPS; l++)  {
                Appendable appendable = factory.createAppendable();
                for (int n=0; n<STRINGS; n++)  {
                    int len = (int) Math.min(Math.round(rande() * STRING_SIZE_MULT), BUFFER_SIZE);
                    String string = new String (buffer, len==BUFFER_SIZE ? 0 : rand(BUFFER_SIZE-len), len);
                    appendable.append(string);
                }
                String result = appendable.toString();
                hashCode += result.charAt(rand(result.length()));
                totalBufferLength += result.length();
            }
            time = System.currentTimeMillis() - time;
            System.out.println(time+" ms, avg buf len="+div(totalBufferLength, LOOPS)+", hash="+hashCode);
            total += time;
        }
        long time = div(total, ATTEMPTS);
        System.out.println(time+" ms, average");
        return time;
    }
}
