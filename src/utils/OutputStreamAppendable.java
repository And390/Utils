package utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * User: And390
 * Date: 20.02.2012
 * Time: 0:14:19
 */
public class OutputStreamAppendable implements Appendable 
{
    private OutputStream output;
    private Charset charset;

    public OutputStreamAppendable(OutputStream _output, Charset _charset)  {  output=_output;  charset=_charset;  }
    public OutputStreamAppendable(OutputStream _output, String _charset)  {  output=_output;  charset=Charset.forName(_charset);  }

    public Appendable append(CharSequence string) throws IOException {
        output.write(string.toString().getBytes(charset));
        return this;
    }

    public Appendable append(CharSequence string, int start, int end) throws IOException {
        output.write(string.toString().substring(start, end).getBytes(charset));
        return this;
    }

    public Appendable append(char c) throws IOException {
        output.write((c+"").getBytes(charset));
        return this;
    }

    public void flush() throws IOException {
        output.flush();
    }

    public void close() throws IOException {
        output.close();
    }
}
