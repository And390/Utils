package utils;

import java.util.ArrayList;

/**
 * User: And390
 * Date: 08.08.14
 * Time: 22:43
 */
public class CSVParser
{
    // ... возможно стоит добавить опцию игнорирования пробелов и опции для Excel-CSV ...

    // Парсинг CSV. Если strict=true и separator=',', то работает в соответствие с RFC 4180,
    //  за исключением того, что допускает разное количество колонок в строках.
    // В strict=false не вызывает ошибок на неправильно расставленных кавычках
    public static void parse(String content, char separator, boolean strict, TableHandler<?> handler) throws Exception
    {
        char quote = '\"';
        char[] chars = content.toCharArray();
        for (int i=0, r=0, c=0, sr=1, ss=-1; ;)
        {
            //    ignore last empty row
            if (i==chars.length && c==0)  break;
            //    parse next value
            String value;
            if (i!=chars.length && chars[i]==quote)
            {
                //    escaped value
                int i0=i+1;
                int duplicated = 0;
                for (;;)  {
                    i++;
                    if (i==chars.length)  if (strict)  throw new Exception (sr+":"+(i0-1-ss)+": кавычки не закрыты");
                                          else  {  i0--;  break;  }
                    if (chars[i]==quote)  {
                        if (i+1==chars.length || chars[i+1]==separator || chars[i+1]=='\n' || chars[i+1]=='\r')  break;
                        else if (chars[i+1]==quote)  {  duplicated++;  i++;  }  //skip quote
                        else if (strict)  throw new Exception (sr+":"+(i0-1-ss)+": неправильно закавыченное значение");
                        else  {
                            //    switch to unescaped mode
                            i0--;
                            i++;
                            do  i++;  while (i!=chars.length && chars[i]!=separator && chars[i]!='\n' && chars[i]!='\r');
                            break;
                        }
                    }
                    else if (chars[i]=='\r' || chars[i]=='\n')  {
                        if (chars[i]=='\r' && i+1!=chars.length && chars[i+1]=='\n')  i++;
                        sr++;
                        ss=i;  //ss указывает на начало строки - 1
                    }
                }
                //    unescape duplicated quotes if need
                if (duplicated!=0)  {
                    char[] unescaped = new char [i-i0-duplicated];
                    unescaped[0] = chars[i0];
                    for (int s=i0+1, d=1; s!=i; s++)  if (chars[s]!=quote || chars[s-1]!=quote)  unescaped[d++] = chars[s];
                    value = new String (unescaped);
                }
                else
                    value = new String (chars, i0, i-i0);
                //
                if (i!=chars.length && chars[i]==quote)  i++;  //skip quote
            }
            else
            {
                //    non-escaped value
                int i0=i;
                for (;; i++)  {
                    if (i==chars.length || chars[i]==separator || chars[i]=='\n' || chars[i]=='\r')  break;
                    else if (strict && chars[i]==quote)  throw new Exception (sr+":"+(i-ss)+": кавычки внутри незакавыченного значения");
                }
                value = new String (chars, i0, i-i0);
            }
            //    process value
            handler.process(r, c, value);
            c++;
            //    if end of line process accumulated values
            if (i==chars.length || chars[i]=='\r' || chars[i]=='\n')  {
                handler.processRow(r);
                if (i==chars.length)  break;  //if end of file finish
                else if (chars[i]=='\r' && (i+1!=chars.length && chars[i+1]=='\n'))  {  i+=2;  }  //skip CRLF
                else  i++;  //skip CR or LF
                r++;
                c=0;
                sr++;
                ss=i-1;  //ss указывает на начало строки - 1
            }
            else  i++;  //skip separator
        }
    }

    public static ArrayList<String[]> parse(String content, char separator, boolean strict) throws Exception
    {
        final ArrayList<String[]> result = new ArrayList<String[]> ();
        parse(content, separator, strict, new TableHandler.R () {
            ArrayList<String> row = new ArrayList<String> ();
            public void processRow(int rowIndex) throws RuntimeException  {
                result.add(row.toArray(new String [0]));
                row.clear();
            }
            public void process(int rowIndex, int colIndex, String value) throws RuntimeException  {
                row.add(value);
            }
        });
        return result;
    }

    // RFC 4180
    public static void parseCSV(String content, TableHandler<?> handler) throws Exception  {
        parse(content, ',', true, handler);
    }

    // RFC 4180
    public static ArrayList<String[]> parseCSV(String content) throws Exception  {
        return parse(content, ',', true);
    }

    public static void parseSCSV(String content, TableHandler<?> handler) throws Exception  {
        parse(content, ';', false, handler);
    }

    public static ArrayList<String[]> parseSCSV(String content) throws Exception  {
        return parse(content, ';', false);
    }


    //                --------    test    --------

    public static class Test
    {
        public static void test(String content, String[][] values) throws Exception
        {
            ArrayList<String[]> result = parse(content, ',', false);
            //System.out.print(Util.cut("|"+Util.toString("|", "|\n|", result), 1));
            for (String[] row : result)  System.out.println("|"+Util.toString(row, "|")+"|");
            Tester.check(result.toArray(new String [0][]), values);
        }

        public static void main(String[] args) throws Exception
        {
            test("", new String[][] {});  // last empty row ignored
            test(" ", new String[][] { new String [] { " " } });
            test(",", new String[][] { new String [] { "", "" } });
            test("x,", new String[][] { new String [] { "x", "" } });
            test(",x", new String[][] { new String [] { "", "x" } });
            test(",x,", new String[][] { new String [] { "", "x", "" } });
            test(",,x,,", new String[][] { new String [] { "", "", "x", "", "" } });
            test("\"x\"", new String[][] { new String [] { "x" } });
            test("\"x\" ,", new String[][] { new String [] { "\"x\" ", "" } });
            test(" \"x\",y,\"z,w\"\"q\"\"\"", new String[][] { new String [] { " \"x\"", "y", "z,w\"q\"" } });
            test("\r,\r\nx,y\r\r\n", new String[][] { new String [] { "" }, new String [] { "", "" },
                    new String [] { "x", "y" }, new String [] { "" } });
            test("\"\"\r\n\"b\"\n\"c\"d\r\n\"e", new String[][] { new String [] { "" }, new String [] { "b" },
                    new String [] { "\"c\"d" }, new String [] { "\"e" } });
            test(" , \", x , ppp\r123,\"x\r\ny\",\"\"\r\n,\"x\" \n,", new String[][] {
                    new String [] { " ", " \"", " x ", " ppp" },
                    new String [] { "123", "x\r\ny", "" },
                    new String [] { "","\"x\" " },
                    new String [] { "", "" } });
        }
    }
}
