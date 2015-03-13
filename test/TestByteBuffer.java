import sun.nio.ch.DirectBuffer;
import utils.ByteArray;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * User: And390
 * Date: 03.02.13
 * Time: 1:26
 */
public class TestByteBuffer
{
    public static void main(String[] args) throws Exception
    {
        int N=3;  //количество попыток (плюс одна первая не в счет)
        int maxFileSize = 256*1024*1024;
        int minFileSize =  32*1024*1024;
        int minBufferSize =     1024;

        for (int fileSize=maxFileSize; fileSize>=minFileSize; fileSize=fileSize/2)
        {
            System.out.println("\n\tfileSize="+fileSize+"\n");
            File file = new File ("temp");

            //    write temp file
            OutputStream output = new BufferedOutputStream (new FileOutputStream (file), 1024*1024);
            try  {  for (int i=0; i<fileSize; i++)  output.write((byte)(Math.random()*256));  }
            finally  {  output.close();  }

            //    read
            for (int n=0; n<=N; n++)
            {
                System.gc();

                //    read with InputStream
                long time = System.currentTimeMillis ();
                ByteArray.read (file);
                time = System.currentTimeMillis() - time;
                System.out.print (time+" ms\t");

                //    read with bytebuffer
                time = System.currentTimeMillis ();
                RandomAccessFile input = new RandomAccessFile (file, "r");
                try
                {
                    FileChannel channel = input.getChannel();
                    try  {
                        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                        byte[] data = new byte [(int)buffer.remaining()];
                        buffer.get(data);
                        ((DirectBuffer) buffer).cleaner().clean();
                    }
                    finally  {  channel.close();  }
                }
                finally  {  input.close();  }
                time = System.currentTimeMillis() - time;
                System.out.println(time+" ms");
            }
        }
    }

}
