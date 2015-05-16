package utils;

/**
 * And390 - 16.05.2015
 */
public class Chars
{
    //----------------        object        ----------------

    public char[] buffer;
    public int pos;
    public int end;
    public Chars(char ch)  {  buffer = new char[] { ch };  pos=0;  end=1;  }
    public Chars(char[] buffer_)  {  buffer=buffer_;  pos=0;  end=buffer_.length;  }
    public Chars(char[] buffer_, int pos_, int end_)  {  buffer=buffer_;  pos=pos_;  end=end_;  }
    public Chars(String source)  {  pos=0;  end=source.length();  buffer=source.toCharArray();  }
    public Chars(String source, int pos_, int end_)  {  buffer=source.substring(pos_, end_).toCharArray();  pos=0;  end=buffer.length;  }

    public int length()  {  return end-pos;  }
    public boolean isEmpty()  {  return  pos==end;  }

    public int indexOf(char[] target, int pos)  {  return indexOf(buffer, pos, end, target, 0, target.length);  }
    public int indexOf(char[] target)  {  return indexOf(buffer, pos, end, target, 0, target.length);  }
    public int indexOf(String target, int pos)  {  return indexOf(target.toCharArray(), pos);  }
    public int indexOf(String target)  {  return indexOf(target.toCharArray());  }
    public int indexOf(char target, int pos)  {  return indexOf(buffer, pos, end, target);  }
    public int indexOf(char target)  {  return indexOf(buffer, pos, end, target);  }

    public boolean startsWith(char[] target, int pos)  {  return startsWith(buffer, pos, end, target, 0, target.length);  }
    public boolean startsWith(char[] target)  {  return startsWith(buffer, pos, end, target, 0, target.length);  }
    public boolean startsWith(String target, int pos)  {  return startsWith(target.toCharArray(), pos);  }
    public boolean startsWith(String target)  {  return startsWith(target.toCharArray());  }

    public boolean endsWith(char[] target, int pos)  {  return endsWith(buffer, pos, end, target, 0, target.length);  }
    public boolean endsWith(char[] target)  {  return endsWith(buffer, pos, end, target, 0, target.length);  }
    public boolean endsWith(String target, int pos)  {  return endsWith(target.toCharArray(), pos);  }
    public boolean endsWith(String target)  {  return endsWith(target.toCharArray());  }

    public Chars subchars(int pos, int end)  {  return new Chars (buffer, pos, end);  }
    public Chars subchars(int pos)  {  return new Chars (buffer, pos, end);  }
    public String substring(int pos, int end)  {  return new String (buffer, pos, end-pos);  }
    public String substring(int pos)  {  return new String (buffer, pos, end-pos);  }

    public String toString()  {  return new String (buffer, pos, end-pos);  }


    //----------------        аналог String.indexOf        ----------------

    //немного переделанная функция из jdk String.indexOf
    public static int indexOf(char[] source, int sourceOffset, int sourceEnd,
                              char[] target, int targetOffset, int targetEnd)
    {
        int targetCount = targetEnd - targetOffset;
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

    public static int indexOf(char[] source, int sourceOffset, int sourceEnd, char[] target)  {
        return indexOf(source, sourceOffset, sourceEnd, target, 0, target.length);
    }

    public static int indexOf(char[] source, char[] target, int offset)  {
        return indexOf(source, offset, source.length, target, 0, target.length);
    }

    public static int indexOf(char[] source, char[] target)  {
        return indexOf(source, 0, source.length, target, 0, target.length);
    }

    public static int indexOf(char[] source, int offset, int end, char target)
    {
        for (; offset<end; offset++)  if (source[offset]==target)  return offset;
        return -1;
    }

    public static int indexOf(char[] source, char target, int offset)  {
        return indexOf(source, offset, source.length, target);
    }
    public static int indexOf(char[] source, char target)  {
        return indexOf(source, 0, source.length, target);
    }


    //----------------        аналоги String.startsWith и String.endsWith        ----------------

    public static boolean startsWith(char[] source, int sourceOffset, int sourceEnd,
                                     char[] target, int targetOffset, int targetEnd)
    {
        int targetCount = targetEnd - targetOffset;
        if (targetCount > sourceEnd - sourceOffset)  return false;
        while (--targetCount >= 0)
            if (source[sourceOffset++] != target[targetOffset++])  return false;
        return true;
    }

    public static boolean startsWith(char[] source, int sourceOffset, int sourceEnd, char[] target)  {
        return startsWith(source, sourceOffset, sourceEnd, target, 0, target.length);
    }
    public static boolean startsWith(char[] source, char[] target, int offset)  {
        return startsWith(source, offset, source.length, target, 0, target.length);
    }
    public static boolean startsWith(char[] source, char[] target)  {
        return startsWith(source, 0, source.length, target, 0, target.length);
    }
    public static boolean startsWith(char[] source, String target, int offset)  {
        return startsWith(source, target.toCharArray(), offset);
    }
    public static boolean startsWith(char[] source, String target)  {
        return startsWith(source, target.toCharArray());
    }

    public static boolean endsWith(char[] source, int sourceOffset, int sourceEnd,
                                   char[] target, int targetOffset, int targetEnd)
    {
        int targetCount = targetEnd - targetOffset;
        if (targetCount > sourceEnd - sourceOffset)  return false;
        while (--targetCount >= 0)
            if (source[--sourceEnd] != target[--targetEnd])  return false;
        return true;
    }

    public static boolean endsWith(char[] source, int sourceOffset, int sourceEnd, char[] target)  {
        return endsWith(source, sourceOffset, sourceEnd, target, 0, target.length);
    }
    public static boolean endsWith(char[] source, char[] target, int offset)  {
        return endsWith(source, offset, source.length, target, 0, target.length);
    }
    public static boolean endsWith(char[] source, char[] target)  {
        return endsWith(source, 0, source.length, target, 0, target.length);
    }
    public static boolean endsWith(char[] source, String target, int offset)  {
        return endsWith(source, target.toCharArray(), offset);
    }
    public static boolean endsWith(char[] source, String target)  {
        return endsWith(source, target.toCharArray());
    }


    //----------------        equals        ----------------

    public static boolean equals(char[] a, int aOffset, int aEnd, char[] b, int bOffset, int bEnd)
    {
        int length = aEnd - aOffset;
        if (length!=bEnd-bOffset)  return false;
        return equals(a, aOffset, b, bOffset, length);
    }

    public static boolean equals(char[] a, int aOffset, char[] b, int bOffset, int length)
    {
        for (; length>0; length--, aOffset++, bOffset++)  if (a[aOffset]!=b[bOffset])  return false;
        return true;
    }

    public static boolean equals(char[] a, int aOffset, int aEnd, char[] b)  {  return equals(a, aOffset, aEnd, b, 0, b.length);  }

    public static boolean equals(char[] a, char[] b)  {  return equals(a, 0, a.length, b, 0, b.length);  }


    //----------------        other        ----------------

    public static String substring(char[] buffer, int pos, int end)  {  return new String (buffer, pos, end-pos);  }
    public static String substring(char[] buffer, int pos)  {  return new String (buffer, pos, buffer.length-pos);  }


    //----------------        find        ----------------

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
}
