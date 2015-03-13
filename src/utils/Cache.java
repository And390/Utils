package utils;

import java.util.HashMap;

/**
 * User: And390
 * Date: 28.04.14
 * Time: 17:43
 */
// лучшее название было бы что-то типа ResourceLoadManager
public class Cache<Item> {

    public static interface Handler<Item>
    {
        Item load(String id)  throws Exception;
        void unload(String id, Item item)  throws Exception;
    }

    public Handler<Item> handler;
    private double halfLife;  //in milliseconds
    private Resource<Item>[] list;
    private HashMap<String, Resource<Item>> map = new HashMap<String, Resource<Item>> ();

    public Cache(long _halfLife, int _capacity, Handler<Item> _handler)
    {  handler=_handler;  halfLife=_halfLife;  list = new Resource [_capacity];  }


    private static class Resource<Item> implements Comparable<Resource<Item>>
    {
        public Item item;
        public String id;
        public int index;
        public double score;
        public long lastTime;

        public int compareTo(Resource<Item> other) {
            return (int) Math.signum(other.score - score);
        }

        public void calcScore(long time, double halfLife)
        {
            score /= Math.pow(2, (time-lastTime)/halfLife);
            lastTime = time;
        }
    }

    public Item get(String id)  throws Exception
    {
        LOG_GET_COUNTER++;
        Resource<Item> resource = map.get(id);  //get
        if (resource==null)
        {
            if (map.size()==list.length)
            {
                //unload
                LOG_UNLOAD_COUNTER++;
                resource = list[list.length-1];
                list[list.length-1] = null;
                map.remove(resource.id);
                handler.unload(resource.id, resource.item);
            }
            //load
            LOG_LOAD_COUNTER++;
            resource = new Resource<Item> ();
            resource.index = map.size();
            list[resource.index] = resource;
            resource.id = id;
            map.put(id, resource);
            resource.item = handler.load(id);
            resource.score = 1;
            resource.lastTime = System.currentTimeMillis();
        }
        else
        {
            //score
            long time = System.currentTimeMillis();
            resource.calcScore(time, halfLife);
            resource.score += 1;
        }
        //up
        {
            int i=resource.index;
            while (i>0)
            {
                int j=i-1;  list[j].calcScore(resource.lastTime, halfLife);
                if (list[j].score>=resource.score)  break;
                list[i]=list[j];
                list[j].index=i;
                i=j;
                LOG_UP_COUNTER++;
            }
            list[i] = resource;
            resource.index = i;
        }
//        checkState();  //test
        return resource.item;
    }

//    private void checkState()  throws Exception
//    {
//        long time = System.currentTimeMillis();
//        double last = Double.MAX_VALUE;
//        for (int i=0; i<map.size(); i++)
//        {
//            double score = list[i].score / Math.pow(2, (time-list[i].lastTime)/halfLife);
//            if (score>last)  throw new Exception ("fuck");
//            last = score;
//        }
//    }

    public static int LOG_GET_COUNTER = 0;
    public static int LOG_UP_COUNTER = 0;
    public static int LOG_LOAD_COUNTER = 0;
    public static int LOG_UNLOAD_COUNTER = 0;

    //(n) time cost, where n is cache capacity; bad operation
    public boolean remove(String id)
    {
        Resource<Item> resource = map.get(id);
        if (resource==null)  return false;
        map.remove(id);
        for (int i=resource.index; i<map.size(); i++)  {  list[i] = list[i+1];  list[i].index=i;  }
        list[map.size()] = null;
        return true;
    }

    //cando setCapacity


    public static class Test {
        public static void main(String[] args)
        {
            try
            {
                Cache<Integer> cache = new Cache <Integer> (10000, 10, new Cache.Handler<Integer> () {
                    public Integer load(String id) throws Exception {
                        return new Integer (id);
                    }
                    public void unload(String id, Integer integer) throws Exception {
                        System.out.println("unload "+id);
                        if (!id.equals(integer.toString()))  throw new Exception ("not equal");
                    }
                });

                for (int i=0; i<10000; i++)
                {
                    Thread.sleep(10);
                    int n = (int)(1.0/(1.0-Math.random()));
                    cache.get(Integer.toString(n));
                    //
                    String dump = "";
                    for (int j=0; j<cache.map.size(); j++)  dump += " " + cache.list[j].item.toString();
                    System.out.println(n+"\t"+dump);
                }

                System.out.println();
                System.out.println("get:\t"+Cache.LOG_GET_COUNTER);
                System.out.println("up:\t"+Cache.LOG_UP_COUNTER);
                System.out.println("load:\t"+Cache.LOG_LOAD_COUNTER);
                System.out.println("unload:\t"+Cache.LOG_UNLOAD_COUNTER);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
