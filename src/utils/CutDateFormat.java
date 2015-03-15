package utils;

import java.text.*;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * And390 - 15.03.15.
 */
@SuppressWarnings("unused")
public class CutDateFormat extends SimpleDateFormat
{
    public CutDateFormat next;

    //public CutDateFormat(String pattern)  {  this(pattern, 0, MAX_DATE_FORMAT_CHAR_INDEX);  }
    public CutDateFormat(String pattern, char c)  {  this(pattern, getDateFormatCharIndex(c));  }
    public CutDateFormat(String pattern, int cutLevelTo)  {  this(pattern, 0, cutLevelTo);  }
    public CutDateFormat(String pattern, int cutLevelFrom, int cutLevelTo)  {
        super(pattern);
        super.setLenient(false);
        for (int level=cutLevelFrom; level<=cutLevelTo; level++)  {
            String cutPattern = cutFromDateFormat(pattern, level);
            if (!cutPattern.equals(pattern))  {
                if (cutPattern.length()!=0)  next = new CutDateFormat (cutPattern, level+1, cutLevelTo);
                break;
            }
        }
    }

    public static CutDateFormat create(String pattern)  {  return new CutDateFormat (pattern, getDateFormatCharIndex('d'));  }
    public static CutDateFormat createTime(String pattern)  {  return new CutDateFormat (pattern, getDateFormatCharIndex('s'));  }

    public static ComboDateFormat createCombo(String... patterns)  {
        SimpleDateFormat[] formats = new SimpleDateFormat [patterns.length];
        for (int i=0; i<patterns.length; i++)  formats[i] = create(patterns[i]);
        return new ComboDateFormat (formats);
    }
    public static ComboDateFormat createTimeCombo(String... patterns)  {
        SimpleDateFormat[] formats = new SimpleDateFormat [patterns.length];
        for (int i=0; i<patterns.length; i++)  formats[i] = createTime(patterns[i]);
        return new ComboDateFormat (formats);
    }


    //                --------    SimpleDateFormat override    --------

    @Override
    public String toString()
    {
        return super.toPattern();
    }

    public java.util.Date parse(String source, ParsePosition pos)  {
        int savePos = pos.getIndex();
        java.util.Date result = super.parse(source, pos);
        if (result!=null && pos.getIndex()!=source.length())  {  // если распарсено, то в конце не должно ничего остаться
            pos.setErrorIndex(pos.getIndex());
            pos.setIndex(savePos);
            return null;
        }
        if (result==null)  if (next!=null)  result = next.parse(source, pos);
        return result;
    }

    @Override
    public void set2DigitYearStart(java.util.Date startDate) {
        super.set2DigitYearStart(startDate);
        if (next!=null)  next.set2DigitYearStart(startDate);
    }

    @Override
    public void setDateFormatSymbols(DateFormatSymbols newFormatSymbols) {
        super.setDateFormatSymbols(newFormatSymbols);
        if (next!=null)  next.setDateFormatSymbols(newFormatSymbols);
    }

    @Override
    public void setCalendar(Calendar newCalendar) {
        super.setCalendar(newCalendar);
        if (next!=null)  next.setCalendar(newCalendar);
    }

    @Override
    public void setNumberFormat(NumberFormat newNumberFormat) {
        super.setNumberFormat(newNumberFormat);
        if (next!=null)  next.setNumberFormat(newNumberFormat);
    }

    @Override
    public void setTimeZone(TimeZone zone) {
        super.setTimeZone(zone);
        if (next!=null)  next.setTimeZone(zone);
    }

    @Override
    public void setLenient(boolean lenient) {
        super.setLenient(lenient);
        if (next!=null)  next.setLenient(lenient);
    }

    @Override
    public void applyPattern(String pattern)  {  throw new UnsupportedOperationException ();  }
    @Override
    public void applyLocalizedPattern(String pattern)  {  throw new UnsupportedOperationException ();  }



    //                --------    utils    --------

    //    получить формат даты из вормата дата-время
    public static String getDateFormatFromTimestamp(String pattern)
    {
        String datePattern = "";
        boolean save = false;
        for (int i=0, i0=0, i1=0;; i++)  {
            if (i==pattern.length())  {
                if (save)  datePattern += pattern.substring(i0);
                break;
            }
            //char c = pattern.charAt(i);
            //if (c=='G' || c=='Y' || c=='y' || c=='M' || c=='W' || c=='w' || c=='D' || c=='d' || c=='F' || c=='E' || c=='u' || c=='Z' || c=='z' || c=='X')  {
            int ci = getDateFormatCharIndex(pattern.charAt(i));
            if (ci>=DATE_DATE_FORMAT_CHAR_INDEX || ci==TIME_ZONE_DATE_FORMAT_CHAR_INDEX)  {
                if (i0==-1)  i0 = i;
                i1 = i;
                save = true;
            }
            //else if (c=='a' || c=='H' || c=='h' || c=='K' || c=='k' || c=='m' || c=='s' || c=='S')  {
            else if (ci>=0 && ci<DATE_DATE_FORMAT_CHAR_INDEX)  {
                if (save)  datePattern += pattern.substring(i0, i1+1);
                save = false;
                i0 = -1;
            }
        }
        return datePattern;
    }

    //    получить формат времени из формата дата-время
    public static String getTimeFormatFromTimestamp(String pattern)
    {
        String timePattern = "";
        boolean save = false;
        for (int i=0, i0=0, i1=0;; i++)  {
            if (i==pattern.length())  {
                if (save)  timePattern += pattern.substring(i0);
                break;
            }
            //char c = pattern.charAt(i);
            //if (c=='a' || c=='H' || c=='h' || c=='K' || c=='k' || c=='m' || c=='s' || c=='S' || c=='Z' || c=='z' || c=='X')  {
            int ci = getDateFormatCharIndex(pattern.charAt(i));
            if (ci>=0 && ci<DATE_DATE_FORMAT_CHAR_INDEX || ci==TIME_ZONE_DATE_FORMAT_CHAR_INDEX)  {  //последнее условие не обязательно с учетом того, что у time zone индекс 0
                if (i0==-1)  i0 = i;
                i1 = i;
                save = true;
            }
            //else if (c=='G' || c=='Y' || c=='y' || c=='M' || c=='W' || c=='w' || c=='D' || c=='d' || c=='F' || c=='E' || c=='u')  {
            else if (ci>=DATE_DATE_FORMAT_CHAR_INDEX) {
                if (save)  timePattern += pattern.substring(i0, i1+1);
                save = false;
                i0 = -1;
            }
        }
        return timePattern;
    }

    // вырезает символы вместе с рядом стоящими незначащими слева или справа (определяя более похожий значащий символ)
    public static String cutFromDateFormat(String pattern, int tv)
    {
//        int tv = getDateFormatCharIndex(tc);
        for (int i=0, i1=-1, i2=-1, i0=0, v0=Integer.MAX_VALUE; ; i++)  {
            if (i==pattern.length())  {
                //    конец строки - если остались необработанные целевые символы, вырезать их вместе с незначащими символами слева
                //    если других значащих символов не было, вырезать все
                if (i1!=-1)  pattern = i0==0 ? "" : pattern.substring(0, i0) + pattern.substring(i2);
                break;
            }
            char c = pattern.charAt(i);
//            if (c==tc)  {
//                //    обнаружен целевой символ - сохранить начало, если это первый, и конец
//                if (i1==-1)  i1 = i;
//                i2 = i+1;
//            }
//            else  {
                //    посчитать индекс "значимости" символа и для значимых
                int v = getDateFormatCharIndex(c);
                if (v!=-1)  {
                    //    обнаружен целевой символ - сохранить начало, если это первый, и конец
                    if (v==tv)  {
                        if (i1==-1)  i1 = i;
                        i2 = i+1;
                    }
                    //    если целевые символы еще не встречены, то сохранить значащий символ слева от целевых
                    else if (i1==-1)  {  v0=v;  i0=i+1;  }
                    //    если встречены, то это символ справа от целевых, значит можно вырезать целевые сиволы
                    else  {
                        //    найти ближайшее по значимости значение
                        //    если ближайшее слева, то вырезать целевые символы с незначащими символами слева
                        //    если справа, то справа, если слева и справа одинаковы, то вырезать левые
                        boolean left = Math.abs(v0 - tv) <= Math.abs(v - tv);
                        pattern = left ? pattern.substring(0, i0) + pattern.substring(i2) :
                                         pattern.substring(0, i1) + pattern.substring(i);
                        i -= left ? i2-i0 : i-i1;
                        i1 = -1;
                        v0 = v;
                        i0 = i;
                    }
                }
//            }
        }
        return pattern;
    }

    public static String cutFromDateFormat(String pattern, char c)  {  return cutFromDateFormat(pattern, getDateFormatCharIndex(c));  }

    public static int getDateFormatCharIndex(char c)  {
        switch (c)  {
            case 'z':
            case 'Z':
            case 'X':  return 0;  //Time zone
            case 'a':  return 1;  //Am/pm marker
            case 'S':  return 2;  //Millisecond
            case 's':  return 3;  //Second
            case 'm':  return 4;  //Minute
            case 'h':
            case 'H':
            case 'k':
            case 'K':  return 5;  //Hour
            case 'G':  return 6;  //Era designator
            case 'u':
            case 'E':  return 7;  //Days of the Week
            case 'F':
            case 'w':
            case 'W':  return 8;  //Week in month, Week in Year
            case 'd':
            case 'D':  return 9;  //Day in month, Day in Year
            case 'M':  return 10;  //Month in year
            case 'y':
            case 'Y':  return 11;  //Year
            default:  return -1;
        }
    }
    public final static int MAX_DATE_FORMAT_CHAR_INDEX = getDateFormatCharIndex('Y');
    public final static int TIME_ZONE_DATE_FORMAT_CHAR_INDEX = getDateFormatCharIndex('Z');
    public final static int DATE_DATE_FORMAT_CHAR_INDEX = getDateFormatCharIndex('G');


    public static class TestCutFromDateFormat
    {
        public static void check(String received, String expected) throws Exception  {
            System.out.println(received);
            if (!received.equals(expected))  throw new Exception ("is not equal to "+expected);
        }
        public static void main(String[] args) throws Exception
        {
            check(cutFromDateFormat("", 's'), "");
            check(cutFromDateFormat("ss", 's'), "");
            check(cutFromDateFormat(".ss:", 's'), "");
            check(cutFromDateFormat("ss:ss", 's'), "");
            check(cutFromDateFormat("ss:yy", 's'), "yy");
            check(cutFromDateFormat("yy:ss", 's'), "yy");
            check(cutFromDateFormat("mm.ss:mm", 's'), "mm:mm");
            check(cutFromDateFormat("mm.ss:hh", 's'), "mm:hh");
            check(cutFromDateFormat("hh.ss:mm", 's'), "hh.mm");
            check(cutFromDateFormat("a ss.mm:hh Z", 'm'), "a ss:hh Z");
        }
    }

    public static class TestDateFormat
    {
        public static void main(String[] args) throws Exception
        {
            DateFormat format = new CutDateFormat ("hh-mm:ss.SSS a", 's');
            System.out.println(format.format(format.parse("11-05:48.768 pm")));
            System.out.println(format.format(format.parse("11-05:48.768")));
            System.out.println(format.format(format.parse("11-05:48")));
            System.out.println(format.format(format.parse("11-05")));

            format = new CutDateFormat("HH-mm:ss.SSS yyyy/dd_MM", 'd');
            System.out.println(format.format(format.parse("11-05:48.768 2014/17_07")));
            System.out.println(format.format(format.parse("11-05:48.768 2014/17_07")));
            System.out.println(format.format(format.parse("11-05:48.768 2014/17_07")));
            System.out.println(format.format(format.parse("11-05:48.768 2014/17_07")));
            System.out.println(format.format(format.parse("11-05:48 2014/17_07")));
            System.out.println(format.format(format.parse("11-05 2014/17_07")));
            System.out.println(format.format(format.parse("11 2014/17_07")));
            System.out.println(format.format(format.parse("2014/17_07")));
            System.out.println(format.format(format.parse("2014/07")));

            format = new CutDateFormat("dd.MM.yyyy HH:mm:ss", 'd');
            System.out.println(format.format(format.parse("30.01.2014 21:52:45")));
            System.out.println(format.format(format.parse("30.01.2014 21:52")));
            System.out.println(format.format(format.parse("30.01.2014 21")));
            System.out.println(format.format(format.parse("30.01.2014")));
            System.out.println(format.format(format.parse("01.2014")));
            //System.out.println(format.format(format.parse("2014")));

            format = createCombo("dd.MM.yyyy HH:mm:ss", "yyyy.MM.dd HH:mm:ss");
            System.out.println(format.format(format.parse("30.01.2014 21:52:45")));
            System.out.println(format.format(format.parse("2014.01.30 21:52:45")));
            System.out.println(format.format(format.parse("30.01.2014 21:52")));
            System.out.println(format.format(format.parse("2014.01.30 21:52")));
            System.out.println(format.format(format.parse("30.01.2014 21")));
            System.out.println(format.format(format.parse("2014.01.30 21")));
            System.out.println(format.format(format.parse("30.01.2014")));
            System.out.println(format.format(format.parse("2014.01.30")));
            System.out.println(format.format(format.parse("01.2014")));
            System.out.println(format.format(format.parse("2014.01")));
        }
    }
}
