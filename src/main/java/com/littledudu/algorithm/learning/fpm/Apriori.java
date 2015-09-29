package com.littledudu.algorithm.learning.fpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hujinjun
 * @date 2015-9-29
 */
public class Apriori {

    private final long minSupport;
    private List<Set<Integer>> freqItem;
    private Map<Set<Integer>, Long> supportData;

    public Apriori(List<Set<Integer>> data, long minSupport) {
        this.minSupport = minSupport;
        freqItem = new ArrayList<Set<Integer>>();
        supportData = new HashMap<Set<Integer>, Long>();

        apriori(data);
    }

    private Set<Set<Integer>> createC1(List<Set<Integer>> data) {
        Set<Set<Integer>> c1 = new HashSet<Set<Integer>>();
        Set<Integer> set = new HashSet<Integer>();
        for (Set<Integer> trans : data) {
            for (int attribute : trans) {
                set.add(attribute);
            }
        }
        List<Integer> lst = new ArrayList<Integer>(set);
        Collections.sort(lst);
        for (int attribute : lst) {
            c1.add(Collections.singleton(attribute));
        }
        return c1;
    }

    private List<Set<Integer>> scanD(List<Set<Integer>> data, Collection<Set<Integer>> ck) {
        Map<Set<Integer>, Long> sup = new HashMap<Set<Integer>, Long>();
        List<Set<Integer>> retList = new ArrayList<Set<Integer>>();
        for (Set<Integer> trans : data) {
            for (Set<Integer> can : ck) {
                if (trans.containsAll(can)) {
                    Long cnt = sup.get(can);
                    if (cnt == null) {
                        sup.put(can, 1L);
                    } else {
                        sup.put(can, cnt + 1L);
                    }
                }
            }
        }

        for (Set<Integer> can : sup.keySet()) {
            long cnt = sup.get(can);
            if (cnt >= minSupport) {
                supportData.put(can, cnt);
                retList.add(can);
            }
        }
        return retList;
    }

    private List<Set<Integer>> aprioriGen(List<Set<Integer>> lk, int k) {
        int lenLk = lk.size();
        List<Set<Integer>> retList = new ArrayList<Set<Integer>>();
        for (int i = 0; i < lenLk; i++) {
            for (int j = i + 1; j < lenLk; j++) {
                Set<Integer> s1 = lk.get(i);
                Set<Integer> s2 = lk.get(j);
                if (k == 2) {
                    Set<Integer> s = new HashSet<Integer>();
                    s.addAll(s1);
                    s.addAll(s2);
                    retList.add(s);
                } else {
                    List<Integer> lst1 = new ArrayList<Integer>(s1);
                    List<Integer> lst2 = new ArrayList<Integer>(s2);
                    Collections.sort(lst1);
                    Collections.sort(lst2);
                    if (lst1.subList(0, k - 2).equals(lst2.subList(0, k - 2))) {
                        Set<Integer> s = new HashSet<Integer>();
                        s.addAll(s1);
                        s.addAll(s2);
                        retList.add(s);
                    }
                }
            }
        }
        return retList;
    }

    private void apriori(List<Set<Integer>> data) {
        Set<Set<Integer>> c1 = createC1(data);
        List<Set<Integer>> L1 = scanD(data, c1);
        List<Set<Integer>> Lk = L1;
        freqItem.addAll(L1);
        int k = 2;
        while (Lk.size() > 0) {
            List<Set<Integer>> ck = aprioriGen(Lk, k);
            Lk = scanD(data, ck);
            freqItem.addAll(Lk);
            k++;
        }
    }

    public List<Set<Integer>> getfreqItem() {
        return freqItem;
    }

    public Map<Set<Integer>, Long> getSupportData() {
        return supportData;
    }
}
