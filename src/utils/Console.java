package utils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * User: andreyzaharov
 * Date: 01.11.2011
 * Time: 13:46:34
 */
public class Console {

    //----------------        application        ----------------

    public static String encoding = System.getProperty("file.encoding");
    public static Charset charset = Charset.forName(encoding);

    public static void setEncoding()  throws UnsupportedEncodingException
    {
        //    set console encoding for windows
        String consoleEncoding = System.getProperty("console.encoding");
        String fileEncoding = System.getProperty("file.encoding");
        if (consoleEncoding==null)
            if (System.getProperty("java.class.path").contains("\\idea_rt.jar"))  consoleEncoding = "Cp1251";
            else if (Util.isWindows() && Charset.forName(fileEncoding).equals(Charset.forName("windows-1251")))  consoleEncoding = "Cp866";
            else  consoleEncoding = fileEncoding;
        if (!Charset.forName(consoleEncoding).equals(Charset.forName(fileEncoding)))  {
            System.setOut(new PrintStream (System.out, true, consoleEncoding));
        }
        System.setErr(System.out);
        encoding = consoleEncoding;
        charset = Charset.forName(consoleEncoding);
        System.setProperty("console.encoding", consoleEncoding);

        //    если используется log4j можно еще добавить
//        for (Enumeration loggers= LogManager.getCurrentLoggers (); loggers.hasMoreElements(); )
//            for (Enumeration appenders=((Logger) loggers.nextElement()).getAllAppenders(); appenders.hasMoreElements(); )  {
//                Appender appender = (Appender) appenders.nextElement();
//                if (appender instanceof ConsoleAppender)  if (((ConsoleAppender)appender).getEncoding()==null)  {
//                    ((ConsoleAppender)appender).setEncoding(consoleEncoding);
//                    ((ConsoleAppender)appender).activateOptions();
//                }
//            }
    }

    private static byte[] readBuffer = new byte [256];
    private static int readBufferOffset = 0;

    public static synchronized String readLine()  throws Exception
    {
        while (true)
        {
            //find line separator
            String readed = new String (readBuffer, 0, readBufferOffset, charset);
            int i = readed.indexOf('\n');
            if (i!=-1)
            {
                //copy last (if exists)
                int dst=0;
                for (int src=i+1; src!=readBufferOffset; src++, dst++)  readBuffer[dst] = readBuffer[src];
                readBufferOffset = dst;
                //return
                if (i!=0)  if (readed.charAt(i-1)=='\r')  i--;
                return readed.substring(0, i);
            }
            //read available
            int n = System.in.read(readBuffer, readBufferOffset, readBuffer.length-readBufferOffset);
            readBufferOffset += n;
            //if (readBufferOffset==readBuffer.length && System.in.available()>0)
            //    readBuffer = Arrays.copyOf(readBuffer, readBuffer.length*2);
        }
    }

    public static synchronized String read()  throws Exception
    {
        do
        {
            if (readBufferOffset==readBuffer.length)  readBuffer = Arrays.copyOf(readBuffer, readBuffer.length*2);
            readBufferOffset += System.in.read(readBuffer, readBufferOffset, readBuffer.length-readBufferOffset);
        }
        while (System.in.available()>0);

        String readed = new String (readBuffer, 0, readBufferOffset, charset);
        readBufferOffset = 0;
        return readed;
    }

    public static class ArgsParser
    {
        public final String[] args;
        public final String[] sortedArgs;
        public final boolean[] options;
        private int index = 0;
        private String errors = "";
        private StringList help = new StringList ();

        public ArgsParser(String[] args_) throws Exception  {
            args = args_;
            sortedArgs = args_.clone();
            Arrays.sort(sortedArgs);
            options = new boolean [args_.length];
        }

        public boolean getOption(String name)  {
            int resultIndex = Arrays.binarySearch(sortedArgs, index, sortedArgs.length, name);
            if (resultIndex>=0)  {  options[resultIndex] = true;  help.append(" [").append(name).append("]");  }
            return resultIndex>=0;
        }

        public String get(String name)  {
            for (; index!=options.length; index++)  if (!options[index])  {
                help.append(" <").append(name).append(">");
                return args[index++];
            }
            errors += " отсутствует аргумент "+name+"\n";
            return null;  //not found
        }

        public void check() throws Exception  {
            for (; index!=options.length; index++)  if (!options[index])  errors += " неизвестный аргумент "+args[index]+"\n";
            if (errors.length()==0)
                throw new Exception (help.size()==0 ? "Программа не имеет параметров командной строки" :
                        help.attach("Неправильные параметры командной строки:"+errors+"\nИспользуйте:\n   ").toString());
        }
    }

}
