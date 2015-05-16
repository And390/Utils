import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * And390 - 20.04.2015
 */
public class TestConcurrentNonBlocking
{
    // 0 - ���������� ����������, -1 - ���������� �� ������, ������������� ����� - ���������� ���������� �� ������
    static AtomicInteger lock = new AtomicInteger (0);
    // �������������� ���� ��� �������� ������� write ���������� (����� ��������� ����� �� ������)
    static volatile boolean writeRequest = false;

    static void read(int id) throws InterruptedException
    {
        //    ������� ������������ write �����������, �������� read ����������
        //    writeRequest, ��� �������������� �� ������� ������� (���������, ���� ��� ��������� ���� �����)
        for (;;) {
            int current;
            if (!writeRequest && (current=lock.get()) != -1) {
                int next = current + 1;
                if (lock.compareAndSet(current, next))  break;
            }
//            //    ���� ���� ������, �� ������ ����� ������� ������� ��������, ������ ��������� �����
//            //    � ���� ������� ������ �� ������ ���� �����, ��� �������� ��� �������� ������������������?
//            else {
//                synchronized (lock) {
//                    lock.incrementAndGet();
//                }
//                break;
//            }
        }
        //    read
        System.out.println("[ "+id);
        Thread.sleep((int)(Math.random()*300));
        System.out.println("] "+id);
        //    ��������� read ����������
        lock.decrementAndGet();
    }

    static void write(int id) throws InterruptedException
    {
        synchronized (lock) {
            //    ���������� ���� ������� write ����������
            writeRequest = true;
            //    ������� ������������ read ����������, ���������� write ����������
            while (!lock.compareAndSet(0, -1))  ;
            //    write
            System.out.println("( "+id);
            Thread.sleep((int)(Math.random()*300));
            System.out.println(") "+id);
            //    ��������� write ����������
            lock.set(0);
            writeRequest = false;
        }
    }


    public static void main(String[] args) throws Exception
    {
        final AtomicInteger idGen = new AtomicInteger (0);
        Thread[] thread = new Thread [4];
        for (int i=0; i<thread.length; i++)  thread[i] = new Thread () {
            @Override
            public void run() {
                while (true) {
                    if (idGen.get()==100)  break;
                    boolean write = Math.random()<0.10;
                    int id = idGen.incrementAndGet();
                    try  {  if (write)  write(id);  else  read(id);  }
                    catch (InterruptedException e)  {  throw new RuntimeException (e);  }
                }
            }
        };
        for (int i=0; i<thread.length; i++)  thread[i].start();
        for (int i=0; i<thread.length; i++)  thread[i].join();
    }
}
