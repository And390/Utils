package utils.sql;

import utils.Util;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * User: And390
 * Date: 05.04.13
 * Time: 17:31
 */
public class SqlUtil
{
    public static Closeable closeable(final java.sql.Connection connection)  {
        return connection==null ? null : new Closeable ()  {
            public void close() throws IOException  {
                try  {  connection.close();  }
                catch (SQLException e)  {  throw new IOException (e);  }
            }
        };
    }

    public static UtilConnection getConnection(String driver, String url, String user, String password)
            throws SQLException, ClassNotFoundException
    {
        Class.forName(driver);
        return new UtilConnection(DriverManager.getConnection(url, user, password));
    }

    public static UtilConnection getConnection(java.util.Properties properties, String prefix) throws Exception
    {
        String driver = Util.getNotEmpty(properties, prefix+"driver");
        String url = Util.getNotEmpty(properties, prefix+"url");
        Util.get(properties, prefix+"user");
        Util.get(properties, prefix+"password");

        //StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        //String appName = stackTrace[stackTrace.length-1].getClassName();

        Class.forName(driver);

        java.util.Properties connectionProperties = new java.util.Properties ();
        for (Map.Entry entry : properties.entrySet())
            if (((String)entry.getKey()).startsWith(prefix))
                connectionProperties.put(((String)entry.getKey()).substring(prefix.length()), entry.getValue());

        return new UtilConnection(DriverManager.getConnection(url, connectionProperties));
    }

    public static ArrayList<Object[]> toList(ResultSet resultSet) throws SQLException
    {
        try  {
            ArrayList<Object[]> result = new ArrayList<Object[]> ();
            int count = resultSet.getMetaData().getColumnCount();
            while (resultSet.next())  {
                Object[] row = new Object [count];
                for (int i=0; i<count; i++)  row[i] = resultSet.getObject(i+1);
                result.add(row);
            }
            return result;
        }
        finally  {  resultSet.close();  }
    }

    public static PreparedStatement setParameters(PreparedStatement statement, Object... parameters) throws SQLException
    {
        int index=1;
        for (Object parameter : parameters)  {  statement.setObject(index, parameter);  index++;  }
        return statement;
    }

    public static int calculateParametersCount(String query)
    {
        int n = 0;
        for (int i=0; i<query.length(); )
        {
            char c = query.charAt(i);
            if (c=='/' && i+1<query.length()-1 && query.charAt(i+1)=='/'
                || c=='-' && i+1<query.length()-1 && query.charAt(i+1)=='-')
            {
                //    line comment
                i = query.indexOf('\n', i+2) + 1;
                if (i==0)  return n;
            }
            else if (c=='/' && i+1<query.length()-1 && query.charAt(i+1)=='*')
            {
                //    bracket comment
                for (i+=2; i<query.length(); )  {
                    i = query.indexOf('*', i) + 1;
                    if (i==0)  return n;
                    if (i<query.length() && query.charAt(i)=='/')  break;
                }
                i++;
            }
            else if (c=='\'')
            {
                //    string
                for (i++; i<query.length(); )  {
                    i = query.indexOf('\'', i) + 1;
                    if (i==0)  return n;
                    if (i<query.length() && query.charAt(i)=='\'')  i++;  //заэкранированная кавычка
                    else  break;  //закрывающая кавычка
                }
            }
            else if (c=='?')  {  n++;  i++;  }
            else i++;
        }
        return n;
    }

    public static class TestCalculateParametersCount
    {
        public static void main(String[] args)
        {
            System.out.println(calculateParametersCount("insert (?,?,?)"));
            System.out.println(calculateParametersCount("--insert (?,?,?)"));
            System.out.println(calculateParametersCount("//insert (?,?,?)"));
            System.out.println(calculateParametersCount("?//insert (?,?,?)\n?"));
            System.out.println(calculateParametersCount("?/*insert (?,?,?)*/?"));
            System.out.println(calculateParametersCount("?'?,?,?'?)"));
            System.out.println(calculateParametersCount("' ''?,'''?,?)'"));
        }
    }


}
