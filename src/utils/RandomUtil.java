package utils;

import java.util.Random;

/**
 * And390 - 29.06.2015
 */
public class RandomUtil
{
    private RandomUtil()  {}

    public static final Random random = new Random ();  //thread safe

    public static int random(int limit)  {  return random.nextInt(limit);  }  //just another name of the method
    public static int random(int min, int max)  {
        if (max<min)  throw new IllegalArgumentException ("max < min");
        return random.nextInt(max-min+1) + min;
    }


}
