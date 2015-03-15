package utils;

import java.text.*;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * And390 - 15.03.15.
 */
@SuppressWarnings("unused")
public class ComboDateFormat extends SimpleDateFormat
{
    private SimpleDateFormat[] formats;

    public ComboDateFormat(SimpleDateFormat... formats_)  {
        if (formats_.length==0)  throw new IllegalArgumentException ("no DateFormat's specified");
        formats = formats_;
    }

    @Override
    public StringBuffer format(java.util.Date date, StringBuffer toAppendTo, FieldPosition fieldPosition)
    {
        return formats[0].format(date, toAppendTo, fieldPosition);
    }

    @Override
    public java.util.Date parse(String source, ParsePosition pos)
    {
        int startPos = pos.getIndex();  //��������� ��������� �������
        int errorPos = -1;
        //    ���������� ���������, ��������� �� ������� ������ ������
        for (DateFormat format : formats)
        {
            //    ���������� ��������� ����
            java.util.Date result = format.parse(source, pos);
            //    ���� ��������� �� �� ����� - ��� ������, ���� ���������� errorIndex � �������� index
            if (result!=null && pos.getIndex()!=source.length())  {
                pos.setErrorIndex(pos.getIndex());
                pos.setIndex(startPos);
                result = null;
            }
            //    ���� ������� ��������� - ������� ��������� (� pos ����� ����������� ������ �������)
            if (result!=null)  return result;
            //    ��������� ������ ������ ������ (���������� ���)
            if (errorPos==-1)  errorPos = pos.getErrorIndex();
        }
        //    ������� ������
        pos.setErrorIndex(errorPos);
        return null;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder ();
        result.append(formats[0]);
        for (int i=1; i<formats.length-1; i++)  result.append(", ").append(formats[i]);
        result.append(" or ").append(formats[formats.length-1]);
        return result.toString();
    }

    @Override
    public boolean isLenient()  {  return formats[0].isLenient();  }
    @Override
    public void setLenient(boolean lenient)  {  for (SimpleDateFormat format : formats)  format.setLenient(lenient);  }

    @Override
    public Calendar getCalendar()  {  return formats[0].getCalendar();  }
    @Override
    public void setCalendar(Calendar newCalendar)  {  for (SimpleDateFormat format : formats)  format.setCalendar(newCalendar);  }

    @Override
    public NumberFormat getNumberFormat()  {  return formats[0].getNumberFormat();  }
    @Override
    public void setNumberFormat(NumberFormat newNumberFormat)  {  for (SimpleDateFormat format : formats)  format.setNumberFormat(newNumberFormat);  }

    @Override
    public TimeZone getTimeZone()  {  return formats[0].getTimeZone();  }
    @Override
    public void setTimeZone(TimeZone zone)  {  for (SimpleDateFormat format : formats)  format.setTimeZone(zone);  }

    @Override
    public void set2DigitYearStart(java.util.Date startDate)  {  for (SimpleDateFormat format : formats)  format.set2DigitYearStart(startDate);  }
    @Override
    public java.util.Date get2DigitYearStart()  {  return formats[0].get2DigitYearStart();  }

    @Override
    public void setDateFormatSymbols(DateFormatSymbols newFormatSymbols)  {  for (SimpleDateFormat format : formats)  format.setDateFormatSymbols(newFormatSymbols);  }
    @Override
    public DateFormatSymbols getDateFormatSymbols()  {  return formats[0].getDateFormatSymbols();  }

    @Override
    public void applyPattern(String pattern)  {  throw new UnsupportedOperationException ();  }
    @Override
    public void applyLocalizedPattern(String pattern)  {  throw new UnsupportedOperationException ();  }

    @Override
    public String toPattern()  {  return formats[0].toPattern();  }
    @Override
    public String toLocalizedPattern()  {  return formats[0].toLocalizedPattern();  }
}
