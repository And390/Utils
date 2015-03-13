package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Класс наследуется от BufferedWriter, некоторые внешние API в таких случаях не создают лишний раз свои буферы,
 *  но весь функционал BufferedWriter заглушается, все напрямую пишется в Appendable
 * User: And390
 * Date: 07.08.12
 * Time: 1:35
 */
public class AppendableBufferedWriter extends BufferedWriter
{
    private Appendable appendable;

    public AppendableBufferedWriter(Appendable appendable_)  {
        super(new Writer() {  //заглушка
            public void write(char[] cbuf, int off, int len)  {  throw new UnsupportedOperationException();  }
            public void flush()  {  throw new UnsupportedOperationException();  }
            public void close()  {  throw new UnsupportedOperationException();  }
        }, 1);
        appendable = appendable_;
    }

    @Override
    public void write(int c) throws IOException {
        appendable.append((char) c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        appendable.append(new String(cbuf, off, len));
    }

    @Override
    public void write(String s, int off, int len) throws IOException {
        appendable.append(s, off, off+len);
    }

    @Override
    public void newLine() throws IOException {
        appendable.append("\n");
    }

    @Override
    public void flush() throws IOException  {}

    @Override
    public void close() throws IOException  {}

    @Override
    public void write(char[] cbuf) throws IOException {
        appendable.append(new String(cbuf));
    }

    @Override
    public void write(String str) throws IOException {
        appendable.append(str);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        appendable.append(csq);
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        appendable.append(csq, start, end);
        return this;
    }

    @Override
    public Writer append(char c) throws IOException {
        appendable.append(c);
        return this;
    }
}
