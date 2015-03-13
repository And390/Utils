/**
 * User: And390
 * Date: 11.10.14
 * Time: 3:08
 */
public class TestExceptionControlFlow
{
    // from http://stackoverflow.com/questions/299068/how-slow-are-java-exceptions

    // results:
    //method1 took 422 ms, result was 49999999
    //method2 took 734 ms, result was 49999999
    //method3 took 52328 ms, result was 49999999
    //method4 took 7172 ms, result was 49999999
    //method4 took 6891 ms, result was 49999999

    private static final long multiplier = 0x5DEECE66DL; // 25214903917
    private static final long addend = 0xBL; // 11
    private static final long mask = (1L << 48) - 1; // 281474976710655 = 2^48 – 1

    // Calculates without exception
    public static boolean method1(long value) {
        value = (value * multiplier + addend) & mask;
    	return (value & 0x1) == 1;
    }

    // Could in theory throw one, but never will
    public static boolean method2(long value) throws Exception {
    	value = (value * multiplier + addend) & mask;
    	// Will never be true
    	if ((value & (1L << 48)) != 0) {
    		throw new Exception();
    	}
        return (value & 0x1) == 1;
    }

    // This one will regularly throw one
    public static void method3(long value) throws Exception {
    	value = (value * multiplier + addend) & mask;
    	if ((value & 0x1) == 1) {
    		throw new Exception();
    	}
    }

    // Without creating stack trace for exception
    public static class NoStackTraceException extends Exception {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    public static void method4(long value) throws NoStackTraceException {
    	value = (value * multiplier + addend) & mask;
    	if ((value & 0x1) == 1) {
    		throw new NoStackTraceException();
    	}
    }

    // Without creating exception object
    public static Exception STATIC_EXCEPTION = new NoStackTraceException ();

    public static void method5(long value) throws Exception {
    	value = (value * multiplier + addend) & mask;
    	if ((value & 0x1) == 1) {
    		throw STATIC_EXCEPTION;
    	}
    }


    public static void main(String[] args)
    {
    	long l;
        int count;

        count = 0;
    	l = System.currentTimeMillis();
    	for (int i=1, v=1; i < 100000000; i++)
            if (method1(i))  count++;
    	l = System.currentTimeMillis() - l;
    	System.out.println("method1 took " + l + " ms, result was " + count);

        count = 0;
    	l = System.currentTimeMillis();
    	for (int i=1, v=1; i < 100000000; i++)
    		try  {  if (method2(i))  count++;  }
            catch (Exception e)  {  count++;  }
    	l = System.currentTimeMillis() - l;
    	System.out.println("method2 took " + l + " ms, result was " + count);

        count = 0;
    	l = System.currentTimeMillis();
    	for (int i=1, v=1; i < 100000000; i++)
            try  {  method3(i);  }
            catch (Exception e)  {  count++;  }  // Do nothing here, as we will get here
    	l = System.currentTimeMillis() - l;
    	System.out.println("method3 took " + l + " ms, result was " + count);

        count = 0;
        l = System.currentTimeMillis();
    	for (int i=1, v=1; i < 100000000; i++)
    		try  {  method4(i);  }
            catch (NoStackTraceException e)  {  count++;  }  // Do nothing here, as we will get here
    	l = System.currentTimeMillis() - l;
    	System.out.println("method4 took " + l + " ms, result was " + count);

        count = 0;
        l = System.currentTimeMillis();
    	for (int i=1, v=1; i < 100000000; i++)
    		try  {  method5(i);  }
            catch (Exception e)  {  count++;  }  // Do nothing here, as we will get here
    	l = System.currentTimeMillis() - l;
    	System.out.println("method4 took " + l + " ms, result was " + count);
    }
}
