package utils;

import utils.iterators.ArrayIterable;
import utils.objects.*;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * User: And390
 * Date: 26.04.14
 * Time: 20:20
 */
@SuppressWarnings("unused")
public class Util
{
    private Util()  {}

    //----------------        system        ----------------

    public static boolean isWindows()  {  return System.getProperty("os.name").contains("Windows");  }

	public static boolean isMac()  {  return System.getProperty("os.name").contains("Mac OS");  }

	public static boolean isLinux()  {  return System.getProperty("os.name").contains("Linux");  }

    public static String getRunningName() throws UnsupportedEncodingException
    {
        return new File(Util.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
    }


    //----------------        strings        ----------------

    // � ������� �� ������������ indexOf ���������� ����� ������, ���� ������ �� ������
    public static int indexOf(String string, char target, int offset)  {
        int result = string.indexOf(target, offset);
        return result==-1 ? string.length() : result;
    }

    public static int indexOf(String string, String target, int offset)  {
        int result = string.indexOf(target, offset);
        return result==-1 ? string.length() : result;
    }

    public static int indexOfWord(String source, String target, int offset)  {
        for (int i2=offset;;)  {
            int i1 = source.indexOf(target, i2);
            if (i1==-1)  return -1;
            i2 = i1 + target.length();
            if ((i1==0 || source.charAt(i1-1)<=' ') && (i2==source.length() || source.charAt(i2)<=' '))  return i1;
        }
    }
    public static int indexOfWord(String source, String target)  {  return indexOfWord(source, target, 0);  }

    // ������� ���������� ��������� �������� � ������
    public static int count(String string, char target)
    {
        int n=0;
        for (int i=0; i!=string.length(); i++)  {
            i = string.indexOf(target, i);
            if (i==-1)  break;
            n++;
        }
        return n;
    }

    // ������� ���������� �������� � ������
    public static int count(String string, String target)
    {
        int n=0;
        for (int i=0; i!=string.length(); i+=target.length())  {
            i = string.indexOf(target, i);
            if (i==-1)  break;
            n++;
        }
        return n;
    }

    //    slice
    //��������� ������ �� ��������� �������-�����������;
    // ���� ������ �� �����������, �� ����������� ����� ����� ������, ��� ������� ������� - ������;
    // ���� ����������� ���� ������, ����� ���� ����� ������ �����, ����������, ���� ����������� ����� � �����;
    //������ ������������ split, �� ��� ���������� ����������� � �����

    public static <T extends Throwable> void slice(String string, char separator, Consumer<String, T> handler) throws T
    {
        for (int i=0; ; i++)  {
            int i0=i;
            i = indexOf(string, separator, i);
            handler.process(string.substring(i0, i));
            if (i==string.length())  break;
        }
    }

    public static String[] slice(String string, char separator)
    {
        //    ��������� ����������
        int count = count(string, separator) + 1;
        //    ���������� �������� � �������
        final String[] result = new String [count];
        slice(string, separator, new StringConsumer ()  {
            int i = 0;
            public void process(String string)  {  result[i++] = string;  }
        });
        return result;
    }

    public static <T extends Throwable> void slice(String string, String separator, Consumer<String, T> consumer) throws T
    {
        for (int i=0; ; i+=separator.length())  {
            int i0=i;
            i = indexOf(string, separator, i);
            consumer.process(string.substring(i0, i));
            if (i==string.length())  break;
        }
    }

    public static String[] slice(String string, String separator)
    {
        //    ��������� ����������
        int count = count(string, separator) + 1;
        //    ���������� �������� � �������
        final String[] result = new String [count];
        slice(string, separator, new StringConsumer ()  {
            int i = 0;
            public void process(String string)  {  result[i++] = string;  }
        });
        return result;
    }

    public static <T extends Collection<String>> T slice(String string, String separator, final T result)
    {
        slice(string, separator, new StringConsumer ()  {
            public void process(String string)  {  result.add(string);  }
        });
        return result;
    }

    public static String[] slice(String string, String separator, boolean ignoreLast)  {
        if (ignoreLast)  string = cutIfEnds(string, separator);
        return slice(string, separator);
    }

    public static class TestSlice  {
        public static void main(String[] args)  {
            if (!Arrays.equals(slice("", '\t'), new String[] { "" }))  throw new RuntimeException ("not match");
            if (!Arrays.equals(slice("\t", '\t'), new String [] { "", "" }))  throw new RuntimeException ("not match");
            if (!Arrays.equals(slice("abc", '\t'), new String [] { "abc" }))  throw new RuntimeException ("not match");
            if (!Arrays.equals(slice("\tabc", '\t'), new String [] { "", "abc"}))  throw new RuntimeException ("not match");
            if (!Arrays.equals(slice("abc\t", '\t'), new String [] { "abc", "" }))  throw new RuntimeException ("not match");
            if (!Arrays.equals(slice("\tabc\t", '\t'), new String [] { "", "abc", "" }))  throw new RuntimeException ("not match");
            if (!Arrays.equals(slice("x\tyyy", '\t'), new String [] { "x", "yyy" }))  throw new RuntimeException ("not match");
            if (!Arrays.equals(slice("x\t\tyyy", '\t'), new String [] { "x", "", "yyy" }))  throw new RuntimeException ("not match");
            if (!Arrays.equals(slice("\tx\t\tyyy\tz\t", '\t'), new String [] { "", "x", "", "yyy", "z", "" }))  throw new RuntimeException ("not match");
        }
    }

    //    sliceWords ����� �� ���������� ��������; ����� ������ �� ������

    public static <T extends Throwable> void sliceWords(String string, Consumer<String, T> consumer) throws T
    {
        for (int i=0; ; )  {
            for (;;)  {  if (i==string.length())  return;  if (string.charAt(i)>' ')  break;  i++;  }
            int i0=i;
            for (;;)  {  i++;  if (i==string.length())  break;  if (string.charAt(i)<=' ')  break;  }
            consumer.process (string.substring(i0, i));
        }
    }

    public static String[] sliceWords(String string)
    {
        //    ��������� ����������
        Consumer.Counter<String> counter = new Consumer.Counter<String> ();
        sliceWords(string, counter);
        //    ���������� �������� � �������
        final String[] result = new String [counter.count];
        sliceWords(string, new StringConsumer ()  {
            int i = 0;
            public void process(String string)  {  result[i++] = string;  }
        });
        return result;
    }

    public static class TestSliceWords  {
        public static void main(String[] args)  {
            if (!Arrays.equals(sliceWords(""), new String [] {}))  throw new RuntimeException ("not match");
            if (!Arrays.equals(sliceWords("  "), new String [] {}))  throw new RuntimeException ("not match");
            if (!Arrays.equals(sliceWords("abc"), new String [] {"abc"}))  throw new RuntimeException ("not match");
            if (!Arrays.equals(sliceWords(" abc"), new String [] {"abc"}))  throw new RuntimeException ("not match");
            if (!Arrays.equals(sliceWords("abc "), new String [] {"abc"}))  throw new RuntimeException ("not match");
            if (!Arrays.equals(sliceWords("   abc  "), new String [] {"abc"}))  throw new RuntimeException ("not match");
            if (!Arrays.equals(sliceWords("x yyy"), new String [] {"x", "yyy"}))  throw new RuntimeException ("not match");
            if (!Arrays.equals(sliceWords(" x  yyy   z "), new String [] {"x", "yyy", "z"}))  throw new RuntimeException ("not match");
        }
    }

    //    sliceRows
    //����� �� ������������ �����, ��������� ������������ ����������� ����� '\r', '\n' � '\r\n\'

    public static <T extends Throwable> void sliceRows(String string, Consumer<String, T> consumer) throws T
    {
        if (string.length()==0)  {
            consumer.process(string);
            return;
        }
        for (int in=-1, ir=-1; ; )  {
            int i0=ir+1;  //������ ����� ������, ����� ����������� � Min(ir+1, in+1) ��� � in+1 ��� ������ '\r\n'
            if (ir<=in)  {  ir = indexOf(string, '\r', ir+1);  }            //��� ir<in � �� ������ �������� (ir=in) ���� ����������� ir
            if (in<=i0)  {  i0=in+1;  in = indexOf(string, '\n', in+1);  }  //��� in<ir, in=ir+1 (������ '\r\n') � ������ �������� ���� ����������� in
            consumer.process(string.substring(i0, ir < in ? ir : in));
            if (ir==string.length() && in==string.length())  break;
        }
    }

    public static <T extends Throwable> void sliceRows(String string, boolean ignoreLast, Consumer<String, T> consumer) throws T
    {
        if (ignoreLast)  string = cutLastNL(string);
        sliceRows(string, consumer);
    }

    public static String[] sliceRows(String string)
    {
        //    calculate count
        Consumer.Counter<String> counter = new Consumer.Counter<String> ();
        sliceRows(string, counter);
        //    slice values and return
        final String[] result = new String [counter.count];
        sliceRows(string, new StringConsumer ()  {
            int i=0;
            public void process(String string)  {  result[i++] = string;  }
        });
        return result;
    }

    public static String[] sliceRows(String string, boolean ignoreLast)
    {
        if (ignoreLast)  string = cutLastNL(string);
        return sliceRows(string);
    }


    //    ������ javascript join

    public static String toString(Iterable<String> strings, String separator)
    {
        //    ��������� ����� ������� � ���������� ���������, ��������� null-��������
        int len = 0;
        int size = 0;
        for (String string : strings)
            if (string!=null)  {  len += string.length() + separator.length();  size++;  }
            else  throw new NullPointerException ("toString is not support nulls");
        //    ���� ��������� ���� ��� ����, ����������� ����� ������� ���������
        if (size==0)  return "";
        if (size==1)  return strings.iterator().next();
        len -= separator.length();  //������ ����� ������� ��������� �����������
        //    �������� ����� � ����������� ���� �����
        char[] buffer = new char [len];
        int destOffset = 0;
        for (String string : strings)  {
            string.getChars(0, string.length(), buffer, destOffset);
            destOffset += string.length();
            if (separator.length()!=0 && destOffset!=len)  {
                separator.getChars(0, separator.length(), buffer, destOffset);
                destOffset += separator.length();
            }
        }
        return new String (buffer);
    }

    public static String toString(Iterable<String> strings)  {
        return toString(strings, "");
    }

    public static String toString(String[] strings, String separator)  {
        return toString(new ArrayIterable<String>(strings), separator);
    }

    public static String toString(String[] strings)  {
        return toString(new ArrayIterable<String>(strings), "");
    }

    public static <T> String toString(T[] objects, String separator)  {
        String[] strings = new String [objects.length];
        for (int i=0; i<objects.length; i++)  strings[i] = objects[i].toString();
        return toString(new ArrayIterable<String>(strings), separator);
    }

    public static <T> String toString(T[] objects)  {
        return toString(objects, "");
    }

    public static class TestToString  {
        public static void main(String[] args)  {
            System.out.println(Util.toString(new String[]{}));
            System.out.println(Util.toString(new String[]{"xxx"}));
            System.out.println(Util.toString(new String[]{"abc", "", "def"}));
        }
    }


    //    ������ ��������������� ������� �� ��������

    public static String notNull(String string)  {  return string!=null ? string : "";  }

    public static boolean isEmpty(String value)  {  if (value==null)  return true;  return value.length()==0;  }
    public static boolean isNotEmpty(String value)  {  if (value==null)  return false;  return value.length()!=0;  }

    public static boolean isTrimEmpty(String value)  {  if (value==null)  return true;  return value.trim().length()==0;  }
    public static boolean isNotTrimEmpty(String value)  {  if (value==null)  return false;  return value.trim().length()!=0;  }

    public static String notEmpty(String value1, String value2)  {
        if (value1!=null)  if (value1.length()!=0)  return value1;
        return value2;
    }
    public static String notEmpty(String value1, String value2, String value3)  {
        if (value1!=null)  if (value1.length()!=0)  return value1;
        if (value2!=null)  if (value2.length()!=0)  return value2;
        return value3;
    }

    public static String cut(String string, int count)  {
        return string.substring(0, string.length()-count);
    }

    public static String cutIfStarts(String string, String prefix)  {
        return string.startsWith(prefix) ? string.substring(prefix.length()) : string;
    }

    public static String cutIfStartsOrNull(String string, String prefix)  {
        return string.startsWith(prefix) ? string.substring(prefix.length()) : null;
    }

    public static String cutIfEnds(String string, String suffix)  {
        return string.endsWith(suffix) ? string.substring(0, string.length()-suffix.length()) : string;
    }

    public static String cutIfSurroundedOrNull(String string, String prefix, String suffix)  {
        return string.length() >= prefix.length() + suffix.length() && string.startsWith(prefix) && string.endsWith(suffix) ? string.substring(prefix.length(), string.length()-suffix.length()) : null;
    }

    public static String cutIfSurrounded(String string, String prefix, String suffix)  {
        String result = cutIfSurroundedOrNull(string, prefix, suffix);
        return result != null ? result : string;
    }

    @Deprecated  // use sliceBefore
    public static String cutAfter(String string, char target)  {
        int i = string.indexOf(target);
        return i==-1 ? string : string.substring(0, i);
    }

    @Deprecated  // use sliceBefore
    public static String cutAfter(String string, String target)  {
        int i = string.indexOf(target);
        return i==-1 ? string : string.substring(0, i);
    }

    @Deprecated  // use sliceAfter
    public static String cutBefore(String string, char target)  {
        int i = string.lastIndexOf(target);
        return i==-1 ? string : string.substring(i+1);
    }

    @Deprecated  // use sliceAfter
    public static String cutBefore(String string, String target)  {
        int i = string.lastIndexOf(target);
        return i==-1 ? string : string.substring(i+target.length());
    }

    public static String cutLastNL(String string)  {
        if (string.endsWith("\r\n"))  string = Util.cut(string, 2);
        else if (string.endsWith("\n"))  string = Util.cut(string, 1);
        else if (string.endsWith("\r"))  string = Util.cut(string, 1);
        return string;
    }

    public static String cutLastLine(String string)  {
        int i = string.lastIndexOf('\n');
        int i2 = string.indexOf('\r', i+1);
        if (i2!=-1)  i = i2;
        else if (i>0 && string.charAt(i-1)=='\r')  i--;
        return i==-1 ? string : string.substring(0, i);
    }

    public static String sliceBefore(String string, char target)  {
        int i = string.indexOf(target);
        return i==-1 ? string : string.substring(0, i);
    }
    public static String sliceBefore(String string, String target)  {
        int i = string.indexOf(target);
        return i==-1 ? string : string.substring(0, i);
    }

    public static String sliceBeforeLast(String string, char target)  {
        int i = string.lastIndexOf(target);
        return i==-1 ? string : string.substring(0, i);
    }
    public static String sliceBeforeLast(String string, String target)  {
        int i = string.lastIndexOf(target);
        return i==-1 ? string : string.substring(0, i);
    }

    public static String sliceAfter(String string, char target)  {
        int i = string.indexOf(target);
        return i==-1 ? string : string.substring(i+1);
    }
    public static String sliceAfter(String string, String target)  {
        int i = string.indexOf(target);
        return i==-1 ? string : string.substring(i+target.length());
    }

    public static String sliceAfterLast(String string, char target)  {
        int i = string.lastIndexOf(target);
        return i==-1 ? string : string.substring(i+1);
    }
    public static String sliceAfterLast(String string, String target)  {
        int i = string.lastIndexOf(target);
        return i==-1 ? string : string.substring(i+target.length());
    }

    public static String ellipsis(String source, int n)  {
        return source.length()>n ? source.substring(0, n-3)+"..." : source;
    }

    public static String numEnding(int num, String nominativ, String genetiv, String plural)  {
        num = Math.abs(num);
        if (num%10==0 || num%10>=5 || num%100 >= 11 && num%100 <= 14)  return plural;
        else if (num%10==1)  return nominativ;
        else  return genetiv;
    }

    public static String num(int num, String nominativ, String genetiv, String plural)  {
        return num + numEnding(num, nominativ, genetiv, plural);
    }

    public static String quote(String string)  {
        return quote(string, new StringList()).toString();
    }
    public static RuntimeAppendable quote(String string, RuntimeAppendable result)  {
        return escape(string, result.append('"'), new char[]{'\\', '"', '\t', '\r', '\n'},
                new String[]{"\\\\", "\\\"", "\\t", "\\r", "\\n"}).append('"');
    }

    public static String escape(String string)  {
        return escape(string, new StringList ()).toString();
    }
    public static RuntimeAppendable escape(String string, RuntimeAppendable result)  {
        return escape(string, result, new char[] { '\\', '\t', '\r', '\n' }, new String[] { "\\\\", "\\t", "\\r", "\\n" });
    }

    public static String escape(String string, char... chars)  {
        return escape(string, new StringList (), chars).toString();
    }
    public static RuntimeAppendable escape(String string, RuntimeAppendable result, char... chars)  {
        chars = Arrays.copyOf(chars, chars.length+1);
        chars[chars.length-1] = '\\';
        return escape(string, result, "\\", chars);
    }
    public static RuntimeAppendable escape(String string, RuntimeAppendable result, String escapeChar, char... chars)  {
        String[] replacers = new String [chars.length];
        for (int i=0; i<chars.length; i++)  replacers[i] = escapeChar+chars[i];
        return escape(string, result, chars, replacers);
    }

    public static RuntimeAppendable escape(String string, RuntimeAppendable result, char[] targets, String[] replacers)  {
        escape(string, result, targets, replacers, 0);
        return result;
    }
    private static void escape(String string, RuntimeAppendable result, char[] targets, String[] replacers, int index)  {
        if (index==targets.length)  {
            result.append(string);
            return;
        }
        for (int i=0;; i++)  {
            //    find next occurence of char that need to be escaped
            int i0 = i;
            i = string.indexOf(targets[index], i);
            if (i==-1)  {
                //    if end, add last part and exit
                if (i0!=0)  string = string.substring(i0);
                escape(string, result, targets, replacers, index+1);
                break;
            }
            //    add part before finded char
            if (i0!=i)  {
                if (i0+1==i)  {
                    //    escape rest specail chars in this one-char part
                    char c = string.charAt(i0);
                    for (int t=index; ; t++)
                        if (t==targets.length)  {  result.append(c);  break;  }
                        else if (c==targets[t])  {  result.append(replacers[t]);  break;  }
                }
                else  {
                    //    recursively escape rest specail chars in this part
                    String part = string.substring(i0, i);
                    escape(part, result, targets, replacers, index+1);
                }
            }
            //    add escaped char
            result.append(replacers[index]);
        }
    }

    private static class Test  {
        public static void check(String source, String expect) throws Exception  {
            String result = escape(source);
            System.out.println(result);
            if (!result.equals(expect))  throw new Exception ("is not equal to: "+expect);
        }
        public static void main(String[] args) throws Exception  {
            check("", "");
            check(" abc\f", " abc\f");
            check("\\", "\\\\");
            check("a\tb", "a\\tb");
            check("xxx\rx\nyyy\\zzz", "xxx\\rx\\nyyy\\\\zzz");
        }
    }

    public static String unescape(String string)  {
        int n = count(string, '\\') - count(string, "\\\\");
        if (n!=0)  for (int i=string.length()-1, c=-1; i>=0 && string.charAt(i)=='\\'; i--, c=-c)  n+=c;
        if (n==0)  return string;
        final char[] result = new char [string.length()-n];
        slice(string, '\\', new StringConsumer ()
        {
            int offset = 0;
            boolean last = false;
            public void process(String string)
            {
                if (string.length()==0)  {
                    if (last)  result[offset++] = '\\';
                    last = !last;
                }
                else  {
                    string.getChars(0, string.length(), result, offset);
                    if (last)  {
                        if (result[offset]=='n')  result[offset]='\n';
                        else if (result[offset]=='r')  result[offset]='\r';
                        else if (result[offset]=='t')  result[offset]='\t';
                    }
                    offset += string.length();
                    last = true;
                }

            }
        });
        return new String (result);
    }

    public static class TestUnescape
    {
        public static void check(String source, String expect) throws Exception  {
            String result = unescape(source);
            System.out.println(result);
            if (!result.equals(expect))  throw new Exception ("is not equal to: "+expect);
        }
        public static void main(String[] args) throws Exception  {
            check("", "");
            check("a", "a");
            check("\\", "\\");
            check("\\\\", "\\");
            check("\\\\\\", "\\\\");
            check("\\\\\\\\", "\\\\");
            check("\\xxx", "xxx");
            check("xxx\\", "xxx\\");
            check("\\xxx\\", "xxx\\");
            check("\\\\xxx\\", "\\xxx\\");
            check("\\xxx\\\\", "xxx\\");
            check("xxx\\\\\\\\", "xxx\\\\");
            check("xxx\\\\\\\\\\\\", "xxx\\\\\\");
            check("xxx\\xxx", "xxxxxx");
            check("xxx\\\\xxx", "xxx\\xxx");
            check("xxx\\\\\\xxx", "xxx\\xxx");
            check("xxx\\\\\\\\xxx", "xxx\\\\xxx");
            check("\\xxx\\\\\\xxx\\", "xxx\\xxx\\");
            check("\\txxx", "\txxx");
            check("xxx\\t", "xxx\t");
            check("xxx\\txxx", "xxx\txxx");
            check("\\\\txxx", "\\txxx");
            check("\\\\\\txxx", "\\\txxx");
            check("xxx\\\\t", "xxx\\t");
            check("xxx\\\\\\t", "xxx\\\t");
            check("xxx\\\\txxx", "xxx\\txxx");
            check("xxx\\t\\", "xxx\t\\");
            check("xxx\\t\\xxx", "xxx\txxx");
            check("xxx\\t\\\\", "xxx\t\\");
        }
    }

    private final static char[] HEX_CHARS_L = "0123456789abcdef".toCharArray();
    private final static char[] HEX_CHARS_U = "0123456789ABCDEF".toCharArray();
    public static String toHexString(byte[] bytes, boolean upperCase)  {
        char[] hexChars = upperCase ? HEX_CHARS_U : HEX_CHARS_L;
        char[] result = new char [bytes.length*2];
        for (int i=0; i<bytes.length; i++)  {
            int v = bytes[i] & 0xFF;
            result[i * 2] = hexChars[v >>> 4];
            result[i * 2 + 1] = hexChars[v & 0x0F];
        }
        return new String (result);
    }


    public static <T extends Appendable> T replace(T result, String source, String target, String replacement) throws IOException
    {
        if (target.length()==0)  throw new IllegalArgumentException ("Empty target");

        for (int i=0; i!=source.length();)  {
            int i0 = i;
            i = source.indexOf(target, i);
            if (i==-1)  {
                result.append(i0==0 ? source : source.substring(i0));
                break;
            }
            if (i0!=i)  result.append(source.substring(i0, i));
            result.append(replacement);
            i += target.length();
        }
        return result;
    }

    public static class TestReplace
    {
        public static void check(String source, String traget, String replacement, String expect) throws Exception  {
            String result = replace(new StringBuilder (), source, traget, replacement).toString();
            System.out.println(result);
            if (!result.equals(expect))  throw new Exception ("is not equal to: "+expect);
        }
        public static void main(String[] args) throws Exception  {
            check("", "xxx", "", "");
            check("xxx", "xxx", "", "");
            check("xxx", "xxx", "yy", "yy");
            check("xxx", "xxxx", "yy", "xxx");
            check("xxxxxx", "xxx", "yy", "yyyy");
            check("axxxbbxxxc", "xxx", "yy", "ayybbyyc");
        }
    }

    public static String fillChars(char ch, int count)
    {
        char[] buffer = new char [count];
        Arrays.fill(buffer, ch);
        return new String (buffer);
    }

    //        ----    ������ � ������    ----

    public static boolean startsWithPath(String path, String prefix, int offset)
    {
        return path.startsWith(prefix, offset) && (path.length()==prefix.length()+offset || path.charAt(prefix.length()+offset)=='/');
    }
    public static boolean startsWithPath(String path, String prefix)  {  return startsWithPath(path, prefix, 0);  }

    public static String cutStartPath(String path)
    {
        boolean absolute = path.startsWith("/");
        return path.substring(indexOf(path, '/', absolute ? 1 : 0) + (absolute ? 0 : 1));
    }

    public static String parentPath(String path)
    {
        return path.substring(0, path.lastIndexOf('/') + 1);
    }

    // ���������� �������� ���� ������������ ��������, ���� ��� ������ ����
    // ���� ������������ �������� ���� (������� . � ..)
    public static String subFilePath(File baseFile, File childFile) throws IOException  {
        String base = baseFile.getCanonicalPath();
        String child = childFile.getCanonicalPath();
        return startsWithPath(child, base) ? child.substring(base.length()) : null;
    }
    public static String subFilePath(String base, String child) throws IOException  {
        return subFilePath(new File(base), new File(child));
    }


    //----------------        �������������� �������        ----------------

    // ����������� ������� �������� ������ ���������� ��������. �������� ������� ������ �������.
    // ������� ���������� � ���� �������, ����������� �������� � ����������� ������� � �����.
    // ������� ����������� �� ����� ������������ ������� (��������� ������� ����� ��� �� �����, ����� �� ����� � ��������).
    // ����������� ������ ���������� ������, ����� ������������ null. ���������� ������� ����� ���� ������ � �������.
    //   colEndLeft    ����������� ������� (c ��������� ������ ������������), � �������������� ������� ����� ������� �� �������
    //   colEndRight   c ��������� ������� ������������, ������� ����� ������ ����� �� ������
    //   colEndMiddle  c ��������� ������������ �� ������, ������� ����� ������ � ����� ������ (��� ����� ����� �������������� ������ �������� �������)
    //   lineEnd       ����������� �����, � �������������� ������� �������� ��� ����
    //   strings       �������������� ������ �����
    // ���� colEndMiddle �/��� colEndRight ����� colEndLeft, �� ����� �������������� ����� ������������
    public static void format(String colEndLeft, String colEndRight, String colEndMiddle, String lineEnd, String[] strings)
    {
        //    ��������� ������������ ���������� �������
        int maxColCount = 0;
        int colCount = 0;
        for (String string : strings)
            if (string==colEndLeft || string==colEndRight || string==colEndMiddle)  {
                colCount++;
                if (colCount>maxColCount)  maxColCount = colCount;
            }
            else if (string==lineEnd)  {
                colCount=0;
            }

        //    ��������� ������������ ������� �������
        int[] maxSize = new int [maxColCount];
        int colIndex=0;
        int colSize=0;
        for (String string : strings)
            if (string==lineEnd)  {
                colIndex=0;
                colSize=0;
            }
            else if (string==colEndLeft || string==colEndRight || string==colEndMiddle)  {
                colIndex++;
                colSize=0;
            }
            else if (colIndex<maxSize.length)  {
                colSize += string.length();
                if (colSize>maxSize[colIndex])  maxSize[colIndex] = colSize;
            }

        //    ����� ��� ��������
        int maxMaxSize = 0;
        for (int size : maxSize)  if (maxMaxSize<size)  maxMaxSize = size;
        char[] spaces = new char [maxMaxSize];
        for (int i=0; i<maxMaxSize; i++)  spaces[i] = ' ';

        //    �������� ������ ���������� ��������
        colIndex=0;
        colSize=0;
        int colStart=0;
        for (int i=0; i<strings.length; i++)  {
            String string = strings[i];
            if (string==lineEnd)  {
                colIndex=0;
                colSize=0;
                colStart=i+1;
            }
            else if (string==colEndLeft || string==colEndRight || string==colEndMiddle)  {
                int size = maxSize[colIndex]-colSize;
                if (string==colEndLeft)  {
                    strings[i] = new String (spaces, 0, size);
                }
                else if (string==colEndRight)  {
                    for (int j=i; j>colStart; j--)  strings[j] = strings[j-1];
                    strings[colStart] = new String (spaces, 0, size);
                }
                else  {
                    strings[colStart] = new String (spaces, 0, size/2) + strings[colStart];
                    strings[i] = new String (spaces, 0, size-size/2);
                }
                colIndex++;
                colSize=0;
                colStart=i+1;
            }
            else if (colIndex<maxSize.length)  {
                colSize += string.length();
                if (colSize>maxSize[colIndex])  maxSize[colIndex] = colSize;
            }
        }
    }

    //     ��������� ������ format ����� ���������� ����������� ������ (formatToArray ���������� ������)

    // �������� ������ � �������������:
    //   colEndLeft    null
    //   colEndRight   "\r"
    //   colEndMiddle  "\f"
    //   linEnd        "\n"
    public static String[] formatToArray(String... strings)
    {
        format("\t", "\r", "\f", "\n", strings);
        return strings;
    }
    public static String format(String... strings)
    {
        return toString(formatToArray(strings));
    }
    public static String format(Collection<String> strings)  {  return format(strings.toArray(new String [strings.size()]));  }

    // ������ �������, ����������� ��������� ������ � ������ �������� ��� ������������ �������
    //   colSep   ����������� ������� (������ �������������� ��������), ����������� ����� ������ �������, ����� ��������� � ������
    //   lineEnd  ����������� �����, ����������� � ����� ������ ������
    //   aligns   ��� ����������� �������� � ������ �������, -1 - �� ������ ����, 0 - �� ������, 1 - �� �������
    //            ���� ����� null, ������������ ������ �� ������ ����
    //   rows     ������� �������� � ���� ���������� �������
    public static String format(String colSep, String lineEnd, int[] aligns, Iterable<String[]> rows)
    {
        //    ��������� ����������
        int count=0;
        for (String[] row : rows)  count += row.length==0 ? 1 : row.length*3 - 1 + 1;
        //    ��������� ������
        String[] strings = new String [count];
        count=0;
        for (String[] row : rows)  {
            for (int i=0; i<row.length; i++)  {
                strings[count++] = row[i];
                if (i!=row.length-1)  strings[count++] = colSep;
                strings[count++] = aligns==null || i>aligns.length || aligns[i]<0 ? null : aligns[i]>0 ? "\r" : "\f";
            }
            strings[count++] = lineEnd;
        }
        //    ��������� � ������� ���������
        format(null, aligns==null ? null : "\r", aligns==null ? null : "\f", lineEnd, strings);
        return toString(strings);
    }

    public static String format(String colSep, String lineEnd, int[] aligns, String[][] rows)  {
        return format(colSep, lineEnd, aligns, new ArrayIterable<String[]>(rows));
    }

    public static String format(String colSep, String lineEnd, Iterable<String[]> rows)  {
        return format(colSep, lineEnd, null, rows);
    }

    // �� �� �����, �� ���������� ������, ���������� ������� � ������ ������������ ���������� aligns
    public static String format(String colSep, String lineEnd, int[] aligns, String... values)
    {
        //    ��������� ������
        String[] strings = new String [values.length*3];
        for (int s=0, v=0; v<values.length; )  {
            for (int c=0; c<aligns.length && v<values.length; c++)  {
                strings[s++] = values[v++];
                if (c!=aligns.length-1 && v!=values.length)  strings[s++] = colSep;
                strings[s++] = aligns[c]<0 ? null : aligns[c]>0 ? "\r" : "\f";
            }
            strings[s++] = lineEnd;
        }
        //    ��������� � ������� ���������
        format(null, "\r", "\f", lineEnd, strings);
        return toString(strings);
    }

    public static class TestFormatTable
    {
        public static void main(String[] args) throws Exception
        {
            System.out.println(format(
                    "h1", "  ", "\f", "h2", "\f", "\n",
                    "xxx:", "  ", "\t", "123", "\r", "\n",
                    "aaa bbb ccc:", "  ", "\t", "12345", "\r", "\n"));

            System.out.println(format("  ", "\n", new int[] { -1, 1 }, new String[][] {
                    new String [] { "h1", "h2", },
                    new String [] { "xxx:", "123", },
                    new String [] { "aaa bbb ccc:", "12345", },
            }));

            System.out.println(format("  ", "\n", new int[] { -1, 1 }, new String[] {
                    "h1", "h2",
                    "xxx:", "123",
                    "aaa bbb ccc:", "12345"
            }));
        }
    }


    //----------------        file        ----------------

    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final Charset UTF16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF32LE = Charset.forName("UTF-32LE");
    public static final Charset UTF32BE = Charset.forName("UTF-32BE");

    public static final byte[] UTF8_BOM = new byte [] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
    public static final byte[] UTF16LE_BOM = new byte [] { (byte)0xFF, (byte)0xFE };
    public static final byte[] UTF16BE_BOM = new byte [] { (byte)0xFE, (byte)0xFF };
    public static final byte[] UTF32LE_BOM = new byte [] { (byte)0xFF, (byte)0xFE, (byte)0x00, (byte)0x00 };
    public static final byte[] UTF32BE_BOM = new byte [] { (byte)0x00, (byte)0x00, (byte)0xFE, (byte)0xFF };

    public static Charset charsetByBOM(byte[] bytes)  {
        if (ByteArray.startsWith(bytes, UTF8_BOM))  return Charset.forName("UTF-8");
        else if (ByteArray.startsWith(bytes, UTF16LE_BOM))  return Charset.forName("UTF-16LE");
        else if (ByteArray.startsWith(bytes, UTF16BE_BOM))  return Charset.forName("UTF-16BE");
        else if (ByteArray.startsWith(bytes, UTF32LE_BOM))  return Charset.forName("UTF-32LE");
        else if (ByteArray.startsWith(bytes, UTF32BE_BOM))  return Charset.forName("UTF-32BE");
        else  return null;
    }

    public static int cutBOM(byte[] bytes, String encoding)  {
        if (Charset.forName(encoding).equals(UTF8))  {  if (ByteArray.startsWith(bytes, UTF8_BOM))  return UTF8_BOM.length;  }
        else if (Charset.forName(encoding).equals(UTF16LE))  {  if (ByteArray.startsWith(bytes, UTF16LE_BOM))  return UTF16LE_BOM.length;  }
        else if (Charset.forName(encoding).equals(UTF16BE))  {  if (ByteArray.startsWith(bytes, UTF16BE_BOM))  return UTF16BE_BOM.length;  }
        else if (Charset.forName(encoding).equals(UTF32LE))  {  if (ByteArray.startsWith(bytes, UTF32LE_BOM))  return UTF32LE_BOM.length;  }
        else if (Charset.forName(encoding).equals(UTF32BE))  {  if (ByteArray.startsWith(bytes, UTF32BE_BOM))  return UTF32BE_BOM.length;  }
        return 0;
    }

    public static String read(InputStream input, String encoding) throws IOException  {
        ByteArray bytes = new ByteArray(input);
        return bytes.toString(cutBOM(bytes.data, encoding), encoding);
    }
    public static String read(RandomAccessFile file, String encoding) throws IOException  {
        byte[] bytes = ByteArray.read(file);
        int offset = cutBOM(bytes, encoding);
        return new String (bytes, offset, bytes.length-offset, encoding);
    }
    public static String read(File file, String encoding) throws IOException  {
        byte[] bytes = ByteArray.read(file);
        int offset = cutBOM(bytes, encoding);
        return new String (bytes, offset, bytes.length-offset, encoding);
    }
    public static String read(String fileName, String encoding) throws IOException  {
        return read(new File(fileName), encoding);
    }

    public static void write(String fileName, String content, String encoding, boolean append) throws IOException  {
        ByteArray.write(fileName, content.getBytes(encoding), append);
    }

    public static void write(String fileName, String content, String encoding) throws IOException  {
        ByteArray.write(fileName, content.getBytes(encoding));
    }

    public static void write(File file, String content, String encoding, boolean append) throws IOException  {
        ByteArray.write(file, content.getBytes(encoding), append);
    }

    public static void write(File file, String content, String encoding) throws IOException  {
        ByteArray.write(file, content.getBytes(encoding));
    }

    public static <E extends Throwable> void listFiles(File dir, Consumer<File, E> handler) throws E
    {
        File[] files = dir.listFiles();
        if (files==null) return;
        for (File file : files)
            if (file.isDirectory())  listFiles(file, handler);
            else  handler.process(file);
    }

    public static ArrayList<File> listFiles(File dir)
    {
        final ArrayList<File> result = new ArrayList<File> ();
        listFiles(dir, new Consumer.R<File> () {
            public void process(File file)  {
                result.add(file);
            }
        });
        return result;
    }

    public static <E extends Throwable> void listFiles(String filename, Consumer<File, E> handler) throws E  {  listFiles(new File (filename), handler);  }
    public static ArrayList<File> listFiles(String filename)  {  return listFiles(new File (filename));  }

    private static int fileExtPos(String fileName, boolean lastExtension)  {
        //    ���������� ������� ����� ����� (��������� ����)
        int i0 = fileName.lastIndexOf('/') + 1;
        if (!File.separator.equals("/"))  {
            int i02 = fileName.lastIndexOf(File.separator) + 1;
            if (i02>i0)  i0 = i02;
        }
        //    ���������� ������� ���������� ����� (������ ����� � ����� �����)
        if (lastExtension)  {  int i = fileName.lastIndexOf('.');  return i<i0 ? -1 : i;  }
        else  return fileName.indexOf('.', i0);
    }

    public static String fileExt(String fileName, boolean lastExtension)  {
        int i = fileExtPos(fileName, lastExtension);
        return i == -1 ? null : fileName.substring(i+1);
    }

    // �������� ���������� ����� (lastExtension - ���������� ������������ �� ��������� ����� ��� �� ������)
    public static String setFileExt(String fileName, String ext, boolean lastExtension)  {
        int i = fileExtPos(fileName, lastExtension);
        return fileName.substring(0, i) + ext;
    }
    public static String setFileExt(String fileName, String ext)  {  return setFileExt(fileName, ext, false);  }

    // ��������� suffix � ����� ����� ����� ����������� (lastExtension - ���������� ������������ �� ��������� ����� ��� �� ������)
    //  ��� �������� ������ old, ���� ������ != null
    public static String replaceFileNameSuffix(String fileName, String old, String suffix, boolean lastExtension)  {
        //    ���������� ������� ����� ����� (��������� ����)
        int i0 = fileName.lastIndexOf('/') + 1;
        if (!File.separator.equals("/"))  {
            int i02 = fileName.lastIndexOf(File.separator) + 1;
            if (i02>i0)  i0 = i02;
        }
        //    ���������� ������� ���������� ����� (������ ��� ��������� ����� � ����� �����)
        int i;
        if (lastExtension)  {  i = fileName.lastIndexOf('.');  if (i<i0)  i = fileName.length();  }
        else  i = indexOf(fileName, '.', i0);
        //    �������� ������� � �����
        if (old != null && fileName.startsWith(old, i - old.length()))
            return fileName.substring(0, i - old.length()) + suffix + fileName.substring(i);
        else
            return fileName.substring(0, i) + suffix + fileName.substring(i);
    }
    // ��������� suffix � ����� ����� ����� ����������� (lastExtension - ���������� ������������ �� ��������� ����� ��� �� ������)
    public static String addFileName(String fileName, String suffix, boolean lastExtension)  {
        return replaceFileNameSuffix(fileName, null, suffix, lastExtension);
    }
    public static String addFileName(String fileName, String suffix)  {  return addFileName(fileName, suffix, false);  }

    // ���� ���� ����������, ��������� � ����� ����� (2) ����� �����������, ���� ����� ���� ����, ����������� � �� ���� � ��� �����
    public static String incFileNameWhileExists(String fileName, boolean lastExtension, String numPrefix, String numSuffix)  {
        String suffix=null;
        for (int i=2; new File (fileName).exists(); i++)  {
            String old = suffix;
            suffix = numPrefix + i + numSuffix;
            fileName = replaceFileNameSuffix(fileName, old, suffix, lastExtension);
        }
        return fileName;
    }
    public static String incFileNameWhileExists(String fileName, boolean lastExtension)  {
        return incFileNameWhileExists(fileName, lastExtension, " (", ")");
    }
    public static String incFileNameWhileExists(String fileName)  {  return incFileNameWhileExists(fileName, false);  }
    public static String incFileNameWhileExistsSimple(String fileName, boolean lastExtension)  {
        return incFileNameWhileExists(fileName, lastExtension, "_", "");
    }
    public static String incFileNameWhileExistsSimple(String fileName)  {  return incFileNameWhileExistsSimple(fileName, false);  }

    public static class TestIncFileNameWhileExists
    {
        public static void main(String[] agrs) throws Exception {
            if (!"some/unexisted file.txt".equals(incFileNameWhileExists("some/unexisted file.txt")))  throw new RuntimeException();
            if (!"src/utils/Util (2).java".equals(incFileNameWhileExists("src/utils/Util.java")))  throw new RuntimeException();
            File file = new File("test.em.txt");
            boolean needDelete = false;
            if (!file.exists())  {  createNew(file);  needDelete = true;  }
            try {
                if (!"test (2).em.txt".equals(incFileNameWhileExists("test.em.txt")))  throw new RuntimeException();

                File file2 = new File("test (2).em.txt");
                boolean needDelete2 = false;
                if (!file2.exists())  {  createNew(file2);  needDelete2 = true;  }
                try  {
                    if (!"test (3).em.txt".equals(incFileNameWhileExists("test.em.txt")))  throw new RuntimeException();
                    if (!"test.em (2).txt".equals(incFileNameWhileExists("test.em.txt", true)))  throw new RuntimeException();
                }
                finally  {  if (needDelete2)  delete(file2);  }
            }
            finally  {  if (needDelete)  delete(file);  }
        }
    }

    public static void mkdir(File file) throws IOException  {
        if (!file.mkdir())  throw new IOException ("Can't make a new directory "+file);
    }

    // ����� ���� ������� ��������� ����� delete ��� �������� ����� �� �������, � ���� �������������?
    // ���-���� ���� ����� ������� ��������� �����, � ����������������� �������� �������� ����� ���� ������
    public static void delete(File file) throws IOException  {
        if (file.isDirectory())  clearDir(file);
        if (!file.delete())  throw new IOException ("Can't delete a file or directory "+file);
    }

    public static void clearDir(File file) throws IOException  {
        if (!file.isDirectory())  throw new IOException("File isn't a directory");
        File[] childs = file.listFiles();
        if (childs==null)  throw new IOException ("Can't list files of "+file);
        for (File child : childs)  delete(child);
    }

    public static void renameTo(File src, File dst) throws IOException  {
        if (!src.renameTo(dst))  throw new IOException ("Can't rename "+src+" to "+dst);
    }

    public static void createNew(File file) throws IOException  {
        if (!file.createNewFile())  throw new RuntimeException("Can't create new file: "+file);
    }

    public static void createDir(File file) throws IOException  {
        if (!file.mkdirs())  throw new RuntimeException("Can't create new directory: "+file);
    }


    //----------------        get abstract values functions        ----------------

    public static <T> T checkNotNull(T value, String name) throws ConfigException {
        if (value==null)  throw new ConfigException ("No "+name);
        return value;
    }

    public static String checkNotEmpty(String value, String name) throws ConfigException
    {
        checkNotNull(value, name);
        if (value.length()==0)  throw new ConfigException ("Empty "+name);
        return value;
    }

    public static int getInt(String value, String name, int minValue, int maxValue, String errorEnding) throws ConfigException
    {
        checkNotEmpty(value, name);
        try  {  int result = Integer.parseInt(value);
                if (result<minValue || result>maxValue)  throw new ConfigException ();
                return result;  }
        catch (NumberFormatException e)  {  throw new ConfigException ("Wrong "+name+errorEnding+": "+value);  }
    }

    public static int getInt(String value, String name, int minValue, int maxValue) throws ConfigException  {
        return getInt(value, name, minValue, maxValue, " (integer in range "+minValue+".."+maxValue+" expected)");
    }

    public static int getInt(String value, String name) throws ConfigException  {
        return getInt(value, name, Integer.MIN_VALUE, Integer.MAX_VALUE, " (integer expected)");
    }

    public static int getUInt(String value, String name) throws ConfigException  {
        return getInt(value, name, 0, Integer.MAX_VALUE, " (non-negative integer expected)");
    }

    public static int getUInt(String value, String name, int limit) throws ConfigException  {
        return getInt(value, name, 0, limit-1);
    }

    public static int getPInt(String value, String name) throws ConfigException  {
        return getInt(value, name, 1, Integer.MAX_VALUE, " (positive integer expected)");
    }

    public static Number getNumber(String value, String name, DecimalFormat format) throws ConfigException
    {
        checkNotEmpty(value, name);
        try  {  return format.parse(value);  }
        catch (ParseException e)  {  throw new ConfigException (
                "Wrong "+name+" (number in '"+format.toPattern()+"' expected): "+value);  }
    }

    public static double getDouble(String value, String name, DecimalFormat format) throws ConfigException
    {
        return getNumber(value, name, format).doubleValue();
    }

    public static boolean getBool(String value, String name) throws ConfigException
    {
        checkNotEmpty(value, name);
        if (value.equals("false") || value.equals("no") || value.equals("off"))  return false;
        else if (value.equals("true") || value.equals("yes") || value.equals("on"))  return true;
        else  throw new ConfigException ("Wrong "+name+" ('true', 'false', 'yes', 'no', 'on', 'off' allowed): "+value);
    }

    public static boolean getBool(String value, String name, boolean defaultValue) throws ConfigException
    {
        if (value==null || value.length()==0)  return defaultValue;
        return getBool(value, name);
    }

    public static boolean getBool(String value, String name, String trueValue, String falseValue) throws ConfigException
    {
        checkNotEmpty(value, name);
        if (trueValue.equals(value))  return true;
        else if (falseValue.equals(value))  return false;
        else  {
            throw new ConfigException (new StringList("Wrong ", name, " ('").escape(trueValue).append("' or '")
                    .escape(falseValue).append("' allowed): ", value).toString());
        }
    }

    public static <T extends Enum<T>> T getEnum(String value, String name, Class<T> class_) throws ConfigException
    {
        checkNotEmpty(value, name);
        try  {  return Enum.valueOf(class_, value);  }
        catch (IllegalArgumentException e) {  throw new ConfigException("Wrong '" + name + "' value: " + value
                + " (expected one of: " + Util.toString(class_.getEnumConstants(), ", ") + ")");  }
    }

    public static <T> T get(String value, String name, Map<String, T> map) throws ConfigException
    {
        checkNotEmpty(value, name);
        T result = map.get(value);
        if (result == null)  throw new ConfigException("Wrong '" + name + "' value: " + value
                + " (expected one of: " + Util.toString(map.keySet(), ", ") + ")");
        return result;
    }


    //----------------        properties        ----------------

    public static Properties readProperties(Properties properties, InputStream input, String encoding) throws IOException
    {
        try  {  properties.load(input);  }
        finally  {  input.close();  }
        //    ��������� ���������
        for (Map.Entry<Object, Object> property : properties.entrySet())
            property.setValue(new String(property.getValue().toString().getBytes("ISO-8859-1"), encoding));
        //    ����������� ������� ��������� ����� ��������� ���������
        for (Object key : System.getProperties().keySet())  if (((String)key).startsWith("property."))
            properties.put(((String)key).substring("property.".length()), System.getProperty((String)key));
        return properties;
    }
    public static Properties readProperties(InputStream input, String encoding) throws IOException  {
        return readProperties(new Properties(), input, encoding);
    }
    public static Properties readProperties(InputStream input) throws IOException  {
        return readProperties(new Properties(), input, System.getProperty("file.encoding"));
    }
    public static Properties readProperties(String fileName, String encoding) throws IOException  {
        return readProperties(new Properties(), new FileInputStream(fileName), encoding);
    }
    public static Properties readProperties(String fileName) throws IOException  {
        return readProperties(new Properties(), new FileInputStream(fileName), System.getProperty("file.encoding"));
    }
    public static Properties readProperties() throws IOException  {
        File file = new File("config.properties");
        FileInputStream input = new FileInputStream (file.exists() ? file : new File("properties.properties"));
        return readProperties(new Properties(), input, System.getProperty("file.encoding"));
    }

    public static Properties subProperties(Properties source, final String prefix) throws IOException
    {
        Properties result = new Properties ();
        for (Object key : source.keySet())  if (((String)key).startsWith(prefix))
            result.put(((String)key).substring(prefix.length()), source.get(key));
        return result;
    }

    private static String propName(String name) {
        return "config property '"+name+"'";
    }

    public static String get(Properties properties, String name) throws Exception  {
        return checkNotNull(properties.getProperty(name), propName(name));
    }

    public static String get(Properties properties, String name, String defaultValue)
    {
        String value = properties.getProperty(name);
        if (value==null || value.length()==0)  return defaultValue;
        return value;
    }

    public static String getNotEmpty(Properties properties, String name) throws ConfigException  {
        return checkNotEmpty(properties.getProperty(name), propName(name));
    }

    public static int getInt(Properties properties, String name) throws ConfigException  {
        return getInt(properties.getProperty(name), propName(name));
    }

    public static int getInt(Properties properties, String name, int minValue, int maxValue) throws ConfigException  {
        return getInt(properties.getProperty(name), propName(name), minValue, maxValue);
    }

    public static int getUInt(Properties properties, String name) throws ConfigException  {
        return getUInt(properties.getProperty(name), propName(name));
    }

    public static int getUInt(Properties properties, String name, int limit) throws ConfigException  {
        return getUInt(properties.getProperty(name), propName(name), limit);
    }

    public static int getPInt(Properties properties, String name) throws ConfigException  {
        return getUInt(properties.getProperty(name), propName(name));
    }

    public static boolean getBool(Properties properties, String name) throws ConfigException  {
        return getBool(properties.getProperty(name), propName(name));
    }

    public static boolean getBool(Properties properties, String name, boolean defaultValue) throws ConfigException  {
        return getBool(properties.getProperty(name), propName(name), defaultValue);
    }

    public static String[] getList(Properties properties, String name, String separator) throws ConfigException {
        String value = getNotEmpty(properties, name);
        return slice(value, separator);
    }

    public static String[] getList(Properties properties, String name, String separator, String[] defaultValue) {
        String value = get(properties, name, "");
        if (value.length() == 0)  return defaultValue;
        return slice(value, separator);
    }

    public static <T extends Collection<String>> T getList(Properties properties, String name, String separator, T result) {
        String value = get(properties, name, "");
        if (value.length() == 0)  return result;
        return slice(value, separator, result);
    }

    public static <T extends Exception> void getAll(Properties properties, String prefix, KeyValueConsumer<String, String, T> handler) throws T  {
        for (Object key : properties.keySet())  if (((String)key).startsWith(prefix))
            handler.process(((String)key).substring(prefix.length()), properties.getProperty((String)key));
    }

    public static KeyValueFeed.R<String, String> getAll(final Properties properties, final String prefix)
    {
        return new KeyValueFeed.R<String, String> ()
        {
            Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
            String key;
            String value;

            public boolean next()  {
                while (iterator.hasNext())  {
                    Map.Entry<Object, Object> entry = iterator.next();
                    if (((String)entry.getKey()).startsWith(prefix))  {
                        key = ((String)entry.getKey()).substring(prefix.length());
                        value = (String)entry.getValue();
                        return true;
                    }
                }
                return false;
            }

            public String getKey()  {
                return key;
            }

            public String getValue()  {
                return value;
            }
        };
    }


    //----------------        ������        ----------------

    public static byte[] digest(String algorithm, byte[] data) throws NoSuchAlgorithmException
    {
        MessageDigest hash = MessageDigest.getInstance(algorithm);
        hash.update(data);
        return hash.digest();
    }

    public static byte[] digest(String algorithm, String string, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
        return digest(algorithm, string.getBytes(encoding));
    }


    //----------------        ������ �����        ----------------

    @SuppressWarnings("unchecked")
    public static <Type> ArrayList<Type> asList (Object... items)  throws NoSuchFieldException, IllegalAccessException
    {
        ArrayList<Type> list = new ArrayList<Type> ();
        for (int i=0; i<items.length; i++)
        {
            if (items[i].getClass().isArray()) {
                Type[] _items = (Type[])items[i];
                for (int j=0; j<_items.length; j++)  list.add(_items[j]);
            }
            else  list.add((Type)items[i]);
        }
        return list;
    }

    //    ���������� ����� totalCount �� �����, ������ �� ������� �� ������ processCount, ��� ������ ���������� action
    public static <E extends Throwable> void divide(int totalCount, int processCount, DivideHandler<E> action)  throws E
    {
        int r = totalCount / processCount;
        int q = totalCount % processCount;
        if (q!=0) {
            r++;
            processCount = totalCount / r;
            q = totalCount % r;
        }
        for (int i=0; r>0; )  {
            int i2 = i + processCount;
            if (q!=0)  {  i2++;  q--;  }
            action.processDivide(i, i2);
            i = i2;
            r--;
        }
    }
    public interface DivideHandler<E extends Throwable>
    {
        void processDivide(int begin, int end) throws E;
    }

    public static class TestDivide {
        public static void main(String[] args)
        {
            try
            {
                divide(101, 50, new DivideHandler <RuntimeException> () {
                    public void processDivide(int begin, int end) {
                        System.out.println(begin+".."+end);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //    print error to OutputStream
    public static void printStackTrace(Throwable error, OutputStream out, String encoding)  throws IOException  {
        out.write((error+"\n").getBytes(encoding));
        StackTraceElement[] trace = error.getStackTrace();
        for (int i=0; i < trace.length; i++)  out.write(("\tat "+trace[i]+"\n").getBytes(encoding));

        Throwable cause = error.getCause();
        if (cause != null)  printStackTraceAsCause(cause, trace, out, encoding);
    }

    private static void printStackTraceAsCause(Throwable error, StackTraceElement[] causedTrace,
                                               OutputStream out, String encoding)  throws IOException  {
        // Compute number of frames in common between this and caused
        StackTraceElement[] trace = error.getStackTrace();
        int m = trace.length-1, n = causedTrace.length-1;
        while (m >= 0 && n >=0 && trace[m].equals(causedTrace[n]))  {  m--; n--;  }
        int framesInCommon = trace.length - 1 - m;

        out.write(("Caused by: " + error + "\n").getBytes(encoding));
        for (int i=0; i <= m; i++)  out.write(("\tat " + trace[i] + "\n").getBytes(encoding));
        if (framesInCommon != 0)  out.write(("\t... " + framesInCommon + " more\n").getBytes(encoding));

        Throwable ourCause = error.getCause();
        if (ourCause != null)  printStackTraceAsCause(ourCause, trace, out, encoding);
    }
}


