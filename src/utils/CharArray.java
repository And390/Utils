package utils;

/**
 * User: andreyzaharov
 * Date: 01.11.2011
 * Time: 15:43:35
 */
public class CharArray {

    public interface FindHandler {
        void targetFinded(char[] source, int begin, int end)  throws Exception;
        void sourceFinded(char[] source, int begin, int end)  throws Exception;
    }

    //в source ищет open и следующий за ним close, и для каждого найденного куска, заключенного в open и close,
    //  вызывает handleTarget, для оставшихся кусков вызывает handeSource
    public static void findPart(char[] source, int sourceOffset, int sourceEnd,
                                char[] open, int openOffset, int openEnd,
                                char[] close, int closeOffset, int closeEnd,
                                FindHandler handler)  throws Exception
    {
        int openLength = openEnd - openOffset;
        int closeLength = closeEnd - closeOffset;
        int i0=sourceOffset;
        while (true)
        {
            int i1 = indexOf(source, i0, sourceEnd, open, openOffset, openEnd);
            if (i1==-1)  break;
            int i2 = indexOf(source, i1+openLength, sourceEnd, close, closeOffset, closeEnd);
            if (i2==-1)  break;
            i2 += closeLength;
            handler.sourceFinded(source, i0, i1);
            handler.targetFinded(source, i1, i2);
            i0=i2;
        }
        handler.sourceFinded(source, i0, sourceEnd);
    }

    public static void findPart(char[] source, int sourceOffset, int sourceEnd,
                                char[] begin, char[] end, FindHandler handler) throws Exception {
        findPart(source, sourceOffset, sourceEnd, begin, 0, begin.length, end, 0, end.length, handler);
    }

    public static void findPart(char[] source, char[] begin, char[] end, FindHandler handler) throws Exception {
        findPart(source, 0, source.length, begin, 0, begin.length, end, 0, end.length, handler);
    }

    //----------------        аналог String.indexOf        ----------------

    //немного переделанная функция из jdk String.indexOf
    public static int indexOf(char[] source, int sourceOffset, int sourceEnd,
                              char[] target, int targetOffset, int targetCount)
    {
        char first  = target[targetOffset];
        int max = sourceEnd - targetCount;

        for (int i = sourceOffset; i <= max; i++) {
            //Look for first character.
            if (source[i] != first)  while (++i <= max && source[i] != first);
            //Found first character, now look at the rest of v2
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]==target[k]; j++, k++);

                if (j == end)  return i;  //Found whole string.
            }
        }
        return -1;
    }

    public static int indexOf(char[] source, int sourceOffset, int sourceEnd, char[] target) {
        return indexOf(source, sourceOffset, sourceEnd, target, 0, target.length);
    }

    public static int indexOf(char[] source, char[] target, int offset) {
        return indexOf(source, offset, source.length, target, 0, target.length);
    }

    public static int indexOf(char[] source, char[] target) {
        return indexOf(source, 0, source.length, target, 0, target.length);
    }

    //----------------        diff        ----------------

    public static char[] diff(char[] oldString, int oldStringOffset, int oldStringEnd,
                              char[] newString, int newStringOffset, int newStringEnd)
    {
        int l1 = oldStringEnd - oldStringOffset;
        int l2 = newStringEnd - newStringOffset;
        if (l1*l2==0)  return new char [0];

        int[] n = new int [l1 * l2];

        for (int i=0; i<l1; i++)
        {
            int lastnij = 0;
            int lastnj = 0;
            for (int j=0; j<l2; j++)
            {
                int lastni = i==0 ? 0 : n [i-1 + l1*j];

                int newn;

                if (oldString[i+oldStringOffset]==newString[j+newStringOffset])  newn = lastnij + 1;
                else
                {
                    newn = lastni>lastnj ? lastni : lastnj;
                }

                n [i + j*l1] = newn;

                lastnij = lastni;
                lastnj = newn;
            }
        }

        //print
//        System.out.print(" ");
//        for (int j=0; j<l2; j++)  System.out.print(" "+sourceB.charAt(j));
//        System.out.println();
//        for (int i=0; i<l1; i++)
//        {
//            System.out.print(sourceA.charAt(i));
//            for (int j=0; j<l2; j++)  System.out.print(" " + n [i + j*l1]);
//            System.out.println();
//        }
//        System.out.println();

        //result
        char result[] = new char [n[l1*l2-1]];
        int _n = result.length;

        for (int i=l1-1,j=l2-1; ; )
        {
            if (oldString[i+oldStringOffset]==newString[j+newStringOffset])
            {
                _n--;
                result[_n] = oldString[i+oldStringOffset];
                if (_n==0)  break;
                i--;  j--;
            }
            else
            {
                if (n [i-1 + j*l1] == _n && i!=0)  i--;
                else  j--;
            }
            if(_n!=n[i + j*l1])  throw new RuntimeException ("debug");
        }

        return result;
    }

    public static char[] diff(char[] oldString, char[] newString)
    {
        return diff(oldString, 0, oldString.length, newString, 0, newString.length);
    }

    //----------------        equals        ----------------

    public static boolean equals(char[] a, int aOffset, char[] b, int bOffset, int length)
    {
        for (; length>0; length--, aOffset++, bOffset++)  if (a[aOffset]!=b[bOffset])  return false;
        return true;
    }

    public static boolean equals(char[] a, int aOffset, int aEnd, char[] b, int bOffset, int bEnd)
    {
        int length = aEnd - aOffset;
        if (length!=bEnd-bOffset)  return false;
        return equals(a, aOffset, b, bOffset, length);
    }

    public static boolean equals(char[] a, int aOffset, int aEnd, char[] b)  {  return equals(a, aOffset, aEnd, b, 0, b.length);  }

    public static boolean equals(char[] a, char[] b)  {  return equals(a, 0, a.length, b, 0, b.length);  }
}
