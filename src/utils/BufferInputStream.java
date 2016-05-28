package utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: And390
 * Date: 22.12.14
 * Time: 20:22
 */
public abstract class BufferInputStream extends InputStream
{
    protected byte[] buffer;
    protected int position;
    protected int count;

    public abstract boolean readBuffer() throws IOException;

    public int read() throws IOException
    {
        //    � ������ ��� ������ - ����������� ������� ����� �����
        if (position>=count)  {
            if (!readBuffer())  return -1;
        }
        //    ��������� ������ �� ������ � �������
        int result = buffer[position++] & 0xff;
        return result;
    }

    public int read(byte[] bytes) throws IOException
    {
        return read(bytes, 0, bytes.length);
    }

    public int read(byte[] bytes, int offset, int length) throws IOException
    {
        int readed = 0;
        while (true)  {
            //    � ������ ��� ������ - ����������� ������� ����� �����
            if (position>=count)  {
                if (!readBuffer())  return readed==0 ? -1 : readed;
            }
            //    ���� � ������ ������ ����, ��� ���������, ����������� ������ ��������� � ������� ���������
            int rest = count;
            if (rest>=length)  {
                System.arraycopy(buffer, position, bytes, offset, length);
                count -= length;
                position += length;
                readed += length;
                return readed;
            }
            //    ���� ������, ����������� ������� ������
            else  {
                System.arraycopy(buffer, position, bytes, offset, rest);
                offset += rest;
                length -= rest;
                count = 0;
                position += rest;
                readed += rest;
            }
        }
    }

    public void close() throws IOException
    {
        buffer = null;
        position = 0;
        count = 0;
    }

    public int available() throws IOException
    {
        return count;
    }

    @Override
    public long skip(long n) throws IOException {
        throw new UnsupportedOperationException();
    }
}
