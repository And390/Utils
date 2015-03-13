package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;


public class Properties_ extends HashMap<String, String>
{
    public void load(InputStream in, String charset)  throws IOException
    {
        byte buf[] = new byte [in.available()];
        in.read(buf);
        String data = new String (buf, charset);
        in.close();

        if (data.length()==0)  return;

        for (int i=0; i<data.length(); )
        {
            int iend = data.indexOf('\n', i);  if (iend==-1)  iend=data.length();
            if (iend<i+2)  {  i=iend+1;  continue;  }
            String row = data.substring(i, data.charAt(iend-1)=='\r' ? iend-1 : iend);
            iend++;

            int i0=row.indexOf("#");
            if (i0!=-1)  {  i=iend;  continue;  }

            int i1=row.indexOf("=");
            if (i1==-1)  throw new IOException ("error row in properties: "+row);  //{  i=iend;  continue;  }

            put(row.substring(0,i1), row.substring(i1+1));
            i = iend;
        }
    }

    public void load(InputStream in)  throws IOException  {  load (in, Charset.defaultCharset().name());  }
    public void load(String fileName)  throws IOException  {  load (new FileInputStream(fileName));  }

    public Properties_(InputStream in, String charset)  throws IOException  {  load(in, charset);  }
    public Properties_(InputStream in)  throws IOException  {  load(in);  }
    public Properties_(String fileName)  throws IOException  {  load(fileName);  }
    public Properties_()  throws IOException  {}

    public static Properties_ properties;
    public static void init() throws IOException  {  properties = new Properties_ ("properties.properties");  }
    public static String value(String property)  {  return properties.get(property);  } 
}
