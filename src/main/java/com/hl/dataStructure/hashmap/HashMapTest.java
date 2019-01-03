package com.hl.dataStructure.hashmap;

import java.util.*;

public class HashMapTest {
    public void testHashTable() {
        Hashtable<String,String> ht = new Hashtable();
        ht.put("a","fdf");
        String v = ht.get("a");
        System.out.println(v);
        v = ht.get(334);
        System.out.println(v);
    }

    public void testLinkedHashMap() {
        Map<Integer, String> lk = new LruMap<>(4,2,true, 2);

        //Map<Integer, String> lk = new HashMap<>();
        lk.put(6,"a");  // a
        lk.put(9,"b");  // a b
        lk.put(3,"c");  // a b c
        //lk.put(6,"a");  //
        String v  = lk.get(6); // b c a  //最近使用的在尾部，最近最少使用的（LRU-least recently used）在头部
        System.out.println(v);

        System.out.println(Integer.MAX_VALUE);
        int x = Integer.MAX_VALUE;
        x++;

        System.out.println(x);
        for (int i=0;i<Integer.MAX_VALUE;i++) {
            lk.put(3, "a");  // a b c
        }

        for (int i=0;i<Integer.MAX_VALUE;i++) {
            lk.put(3, "a");  // a b c
        }

        Collection<String> val = lk.values();
//        Set<Map.Entry<Integer, String>> entries = lk.entrySet();
//        for (Map.Entry<Integer,String> entry : entries) {
//            System.out.println(entry);
//        }
        System.out.println(val);
    }

    static class LruMap<K,V> extends LinkedHashMap<K,V> {
        private int maxSize = 100;

        public LruMap(int initCapicity, float loadFactor, boolean accessOrder, int maxSize){
            super(initCapicity, loadFactor, accessOrder);
            this.maxSize = maxSize;
        }

        protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
            if (size() > maxSize) {
                return true;
            }
            return false;
        }
    }


    public void testHashMap() {
        // initCapacity = 1 >> 4; // 16
        // default loadFactor = 0.75;
        // when buck link length >= 8, transfer to tree. because avg search time of node, in tree is O(log(n)), link = O(n/2)
        // log(8)=3, 8/2 = 4.  when n>=8, log(n) < n/2
        // put(key,value):
        //    h = key.hashcode ^ (key.hashcode >>> 16) // spread higher bits into hash.
        //    i = h & (length-1) // = h % length, because length = power(2,n)
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(3,3);
        hm.put(5,5);
        hm.put(7,7);
    }
    // TreeMap

    // Red-Black Tree

    public static void main(String[] args) {
        HashMapTest test = new HashMapTest();
        //test.testHashTable();
        test.testLinkedHashMap();


    }
}
