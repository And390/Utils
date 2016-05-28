package utils;

import java.util.*;

/**
 * And390 - 23.01.2016
 */
public class Trie<T extends Comparable<T>>
{
    private static class Node<T extends Comparable<T>>
    {
        // Node is "terminal", i.e. it is a leaf, i.e. represent sequnce end,
        //  if it childs == null or childs[0] == TERMINAL
        // if node has no childs then childs == null
        // childs are always sorted

        T value;
        ArrayList<Node<T>> childs;
        Node(T aItem, boolean terminal)  {  value=aItem;  if (!terminal)  childs = new ArrayList<Node<T>>();  }

        // binary search elementin childs
        int find(T child)  {
            if (childs==null)  return -2;
            if (childs.size()==0)  return -1;  //now, this must not occur
            int low = childs.get(0)==TERMINAL ? 1 : 0, high = childs.size();
            while (low < high) {
                int mid = (low + high) >>> 1;
                int cmp = child.compareTo(childs.get(mid).value);
                if (cmp==0)  return mid;
                else if (cmp < 0)  high = mid;
                else  low = mid + 1;
            }
            return -1-high;
        }

        @SuppressWarnings("unchecked")
        void insert(int index, Node<T> child)  {
            if (childs==null)  {  childs = new ArrayList<Node<T>>();  childs.add(TERMINAL);  }
            childs.add(index, child);
        }

        boolean isTerminal()  {
            return childs==null || childs.size()>0 && childs.get(0)==TERMINAL;
        }

        @SuppressWarnings("unchecked")
        void addTerminal()  {
            if (isTerminal())  return;
            childs.add(0, TERMINAL);
        }

        static final Node TERMINAL = null;
    }

    private HashSet<T> units = new HashSet<T>();
    private HashMap<T, Node<T>> nodes = new HashMap<T, Node<T>>();

    public void add(T[] seq)
    {
        if (seq.length==0)  throw new IllegalArgumentException(Arrays.toString(seq));
        if (seq.length==1)  units.add(seq[0]);

        Node<T> node = nodes.get(seq[0]);
        if (node==null)  {  node = new Node<T>(seq[0], false);  nodes.put(seq[0], node);  }
        for (int i=1; i<seq.length; i++)  {
            int index = node.find(seq[i]);
            if (index>=0)  node = node.childs.get(index);
            else  {
                Node<T> child = new Node<T>(seq[i], i==seq.length-1);
                node.insert(-index-1, child);
                node = child;
            }
        }
        node.addTerminal();
    }

    // find the longest sequence at this position, return the sequence length (zero if nothing is match)
    public int find(T[] source, int pos)
    {
        if (pos == source.length)  return 0;
        int result = findNotUnit(source, pos);
        return result > 1 ? result : units.contains(source[pos]) ? 1 : 0;
    }

    private int findNotUnit(T[] source, int pos)
    {
        Node<T> node = nodes.get(source[pos]);
        if (node==null)  return 0;

        int result = 0;  //find max, first node can't be terminal
        for (int p=pos+1; p<source.length; p++)  {
            int i = node.find(source[p]);
            if (i < 0)  break;
            node = node.childs.get(i);
            if (node.isTerminal())  result = p+1-pos;
        }
        return result;
    }


    public static class Test
    {
        private static void check(int result, int expected)  {
            if (result != expected)  throw new AssertionError("Wrong result " + result + ", expected " + expected);
        };

        public static void main(String[] args)  {
            Trie<String> trie = new Trie<String>();
            check(trie.find(new String[]{}, 0), 0);
            check(trie.find(new String[]{""}, 0), 0);

            trie = new Trie<String>();
            trie.add(new String[]{ "a" });
            check(trie.find(new String[]{}, 0), 0);
            check(trie.find(new String[]{"x", "a"}, 0), 0);
            check(trie.find(new String[]{"a", "x"}, 0), 1);

            trie = new Trie<String>();
            trie.add(new String[]{ "a", "b" });
            check(trie.find(new String[]{"x", "a", "b"}, 0), 0);
            check(trie.find(new String[]{"a", "x"}, 0), 0);
            check(trie.find(new String[]{"b", "x"}, 0), 0);
            check(trie.find(new String[]{"a", "b", "x"}, 0), 2);

            trie = new Trie<String>();
            trie.add(new String[]{"b", "a" });
            trie.add(new String[]{"b" });
            check(trie.find(new String[]{"b", "x"}, 0), 1);
            check(trie.find(new String[]{"b", "a"}, 0), 2);
            check(trie.find(new String[]{"a", "b"}, 0), 0);

            trie = new Trie<String>();
            trie.add(new String[]{"a", "b", "c" });
            trie.add(new String[]{"a", "b" });
            trie.add(new String[]{"a", "b", "d", "e"});
            trie.add(new String[]{"a", "b", "c"});
            check(trie.find(new String[]{"a", "b", "d"}, 0), 2);
            check(trie.find(new String[]{"a", "b", "c", "d"}, 0), 3);
            check(trie.find(new String[]{"a", "b", "d", "e"}, 0), 4);
        }
    }
}
