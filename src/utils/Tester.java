package utils;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * User: And390
 * Date: 25.09.12
 * Time: 21:54
 */
public class Tester
{
//    public static void check(Object received, Object expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !expected.equals(received))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(char[] received, char[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(boolean[] received, boolean[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(byte[] received, byte[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(short[] received, short[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(int[] received, int[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(long[] received, long[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(float[] received, float[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(double[] received, double[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+received);
//    }
//
//    public static void check(Object[] received, Object[] expected) throws Exception  {
//        if (expected==null && received!=null)  throw new Exception ("value must be null: "+received);
//        if (expected!=null && !Arrays.equals(received, expected))  throw new Exception ("value are not match: "+Arrays.toString(received));
//    }
//
//    public static void check(Object[][] received, Object[][] expected) throws Exception  {
//        if (received==null && expected==null)  return;
//        if (expected==null)  throw new Exception ("value must be null: "+received);
//        if (received==null)  throw new Exception ("null value received");
//        if (received.length!=expected.length)  throw new Exception (
//                "value length are not match ("+received.length+"<>"+expected.length+"): "+Arrays.toString(received));
//        for (int i=0; i<received.length; i++)
//            try  {  check(received[i], expected[i]);  }
//            catch (Exception e)  {  throw new Exception (i+": "+e.getMessage());  }
//            throw new Exception ("value are not match: "+Arrays.toString(received));
//    }

    public static String toString(Object object)
    {
        if (object==null)  return "null";
        if (!object.getClass().isArray())  return object.toString();
        int len = Array.getLength(object);
        if (len==0)  return "[]";
        StringList result = new StringList ("[");
        for (int i=0;;)  {
            result.append(toString(Array.get(object, i++)));
            if (i==len)  return result.append("]").toString();
            result.append(", ");
        }
    }

    public static void check(Object received, Object expected) throws Exception
    {
        if (received==null && expected==null)  return;
        if (expected==null)  throw new Exception ("value must be null: "+toString(received));
        if (received==null)  throw new Exception ("null value received, but expected: "+toString(expected));
        if (expected.getClass().isArray())  {
            if (!received.getClass().isArray())  throw new Exception ("array expected, but received: "+toString(received));
            int rlen = Array.getLength(received);
            int elen = Array.getLength(expected);
            if (rlen!=elen)  throw new Exception ("array length are not match ("+rlen+"<>"+elen+"): "+toString(received));
            for (int i=0; i<rlen; i++)
                try  {  check(Array.get(received, i), Array.get(expected, i));  }
                catch (Exception e)  {  if (e.getClass()!=Exception.class)  throw e;
                                        else  throw new Exception (i+": "+e.getMessage());  }
        }
        else if (!received.equals(expected))  throw new Exception ("value are not match: "+toString(received));
    }
}
