package utils;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;


//парсит и генерирует различные представления бина - ява-класс, xml, таблица в БД, документация в виде html-таблицы

/**
 * не рабочий
 * User: andreyzaharov
 * Date: 16.11.2011
 * Time: 14:19:22
 */
public class Bean
{
    //индексы формы-представлений
    private static int FORM_COUNTER = 0;
    public static final int JAVA = FORM_COUNTER++;
    public static final int XML = FORM_COUNTER++;
    public static final int DATABASE = FORM_COUNTER++;
    //public static final int HTML = FORM_COUNTER++;

    public static class Field
    {
        public String[] names;
        public String[] types;
    }

    public String[] names;
    public ArrayList<Field> fields;

    public static final Properties DEFAULT_COMMON_PROPERTIES;
    public static final Properties DEFAULT_JAVA_PROPERTIES;

    static
    {
        DEFAULT_COMMON_PROPERTIES = new Properties ();
        DEFAULT_COMMON_PROPERTIES.setProperty("tab", "    ");
        DEFAULT_COMMON_PROPERTIES.setProperty("lineEnd", "\n");

        DEFAULT_JAVA_PROPERTIES = new Properties (DEFAULT_COMMON_PROPERTIES);
        DEFAULT_JAVA_PROPERTIES.setProperty("classPrefix", "public class ");
        DEFAULT_JAVA_PROPERTIES.setProperty("classSuffix", "\n{\n");
    }

    public static Writer fieldsToJava(Bean bean, Writer output, Properties properties) throws IOException
    {
        String prefix = properties.getProperty("tab")+"private ";
        String suffix = ';'+properties.getProperty("lineEnd");
        for (Field field : bean.fields)
            output.append(prefix).append(field.types[JAVA]).append(" ").append(field.names[JAVA]).append(suffix);
        return output;
    }
    public static Writer toJava(Bean bean, Writer output, Properties properties) throws IOException
    {
        String prefix = properties.getProperty("classPrefix");
        String suffix = properties.getProperty("classSuffix");
        output.append(prefix).append(bean.names[JAVA]).append(suffix);
        return fieldsToJava(bean, output, properties);
    }
    public static Writer fieldsToDatabase(Bean bean, Writer output, Properties properties) throws IOException
    {
        String prefix = properties.getProperty("tab")+"private ";
        String suffix = ';'+properties.getProperty("lineEnd");
        for (Field field : bean.fields)
            output.append(prefix).append(field.types[JAVA]).append(" ").append(field.names[JAVA]).append(suffix);
        return output;
    }


    public static Writer fieldsToJava(Bean bean, Writer output) throws IOException  {  return fieldsToJava(bean, output, DEFAULT_JAVA_PROPERTIES);  }
    public static String fieldsToJava(Bean bean, Properties properties) throws IOException  {  return fieldsToJava(bean, new CharArrayWriter (), properties).toString();  }
    public static String fieldsToJava(Bean bean) throws IOException  {  return fieldsToJava(bean, new CharArrayWriter (), DEFAULT_JAVA_PROPERTIES).toString();  }

    public static Writer toJava(Bean bean, Writer output) throws IOException  {  return toJava(bean, output, DEFAULT_JAVA_PROPERTIES);  }
    public static String toJava(Bean bean, Properties properties) throws IOException  {  return toJava(bean, new CharArrayWriter (), properties).toString();  }
    public static String toJava(Bean bean) throws IOException  {  return toJava(bean, new CharArrayWriter (), DEFAULT_JAVA_PROPERTIES).toString();  }
}
