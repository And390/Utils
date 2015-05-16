import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * And390 - 20.04.2015
 */
public class TestConcurrentNonBlocking
{
    // 0 - отсутствие блокировок, -1 - блокировка на запись, положительное число - количество блокировок на чтение
    static AtomicInteger lock = new AtomicInteger (0);
    // дополнительный флаг для хранения запроса write блокировки (иначе возникает голод на записи)
    static volatile boolean writeRequest = false;

    static void read(int id) throws InterruptedException
    {
        //    ожидать освобождения write блокировоки, добавить read блокировку
        //    writeRequest, это дополнительное не строгое условие (нормально, если оно изменится чуть позже)
        for (;;) {
            int current;
            if (!writeRequest && (current=lock.get()) != -1) {
                int next = current + 1;
                if (lock.compareAndSet(current, next))  break;
            }
//            //    если идет запись, то потоки будут ожидать захвата монитора, вместо холостого цикла
//            //    у меня большой вопрос по поводу этой ветки, она увеличит или уменьшит производительность?
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
        //    отпустить read блокировку
        lock.decrementAndGet();
    }

    static void write(int id) throws InterruptedException
    {
        synchronized (lock) {
            //    установить флаг запроса write блокировки
            writeRequest = true;
            //    ожидать освобождения read блокировок, установить write блокировку
            while (!lock.compareAndSet(0, -1))  ;
            //    write
            System.out.println("( "+id);
            Thread.sleep((int)(Math.random()*300));
            System.out.println(") "+id);
            //    отпустить write блокировку
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
