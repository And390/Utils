import java.io.*;
import java.util.ArrayList;

/**
 * User: And390
 * Date: 19.01.13
 * Time: 2:48
 */
public class TestBufferedOutput
{
    public static void main(String[] args) throws Exception
    {
        int N=3;  //количество попыток (плюс одна первая не в счет)
        int maxFileSize =  64*1024*1024;
        int minFileSize =  32*1024*1024;
        int minBufferSize =     1024;
        byte[] input = new byte [64];  //порция записываемых данных, размер должен быть делителем предыдущего
        for (int i=0; i<input.length; i++)  input[i] = (byte)(Math.random()*256);
        ArrayList<ArrayList<Long>> allAverageTimes1 = new ArrayList<ArrayList<Long>> ();
        ArrayList<ArrayList<Long>> allAverageTimes2 = new ArrayList<ArrayList<Long>> ();

        for (int fileSize=maxFileSize; fileSize>=minFileSize; fileSize=fileSize/2)
        {
            System.out.println("\n\tfileSize="+fileSize+"\n");

            ArrayList<Long> averageTimes1 = new ArrayList<Long> ();
            ArrayList<Long> averageTimes2 = new ArrayList<Long> ();

            for (int bufferSize=0; bufferSize!=minBufferSize/2; bufferSize = bufferSize!=0 ? bufferSize/2 : fileSize)
            {
                System.out.println("bufferSize="+bufferSize);
                long totalTime1 = 0;
                long totalTime2 = 0;
                for (int n=0; n<=N; n++)
                {
                    File file = new File ("temp");
                    file.delete();
                    long time2;
                    long time1 = System.currentTimeMillis();
                    OutputStream output = new FileOutputStream (file);
                    if (bufferSize!=0)  output = new BufferedOutputStream (output, bufferSize);
                    try  {
                        time2 = System.currentTimeMillis();
                        for (int i=0; i<fileSize; i+=input.length)  //output.write(input);
                            for (byte c : input)  output.write(c);
                        time2 = System.currentTimeMillis() - time2;
                    }
                    finally  {  output.close();  }
                    time1 = System.currentTimeMillis() - time1;

                    if (n!=0)  totalTime1 += time1;
                    if (n!=0)  totalTime2 += time2;
                    System.out.println(time1+" ms\t"+time2+" ms");

                    System.gc();
                }

                totalTime1 = Math.round (totalTime1 / (double) N);
                totalTime2 = Math.round (totalTime2 / (double) N);
                averageTimes1.add(totalTime1);
                averageTimes2.add(totalTime2);
                System.out.println(totalTime1+" ms\t"+totalTime2+" ms\taverage");
                System.out.println();
            }

            allAverageTimes1.add(averageTimes1);
            allAverageTimes2.add(averageTimes2);
            for (int i=0, bufferSize=0; bufferSize!=minBufferSize/2; i++, bufferSize = bufferSize!=0 ? bufferSize/2 : fileSize)
                System.out.println(averageTimes1.get(i)+" ms\t"+averageTimes2.get(i)+" ms\tbufferSize="+bufferSize);

            System.out.println();
        }

        System.out.println();
        for (int fileSize=maxFileSize, j=0; fileSize>=minFileSize; fileSize=fileSize/2, j++)
            for (int i=0, bufferSize=0; bufferSize!=minBufferSize/2; i++, bufferSize = bufferSize!=0 ? bufferSize/2 : fileSize)
                System.out.println(allAverageTimes1.get(j).get(i)+" ms\t"+allAverageTimes1.get(j).get (i)+" ms\tbufferSize="+bufferSize+"\tfileSize="+fileSize);
    }
}
