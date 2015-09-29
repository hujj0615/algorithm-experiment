package com.littledudu.algorithm.learning.fpm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class AprioriTest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void testGetfreqItem() {

        List<Set<Integer>> data = new ArrayList<Set<Integer>>();
        data.add(new HashSet<Integer>(Arrays.asList(1, 2)));
        data.add(new HashSet<Integer>(Arrays.asList(2, 3, 4)));
        data.add(new HashSet<Integer>(Arrays.asList(1, 3, 4, 5)));
        data.add(new HashSet<Integer>(Arrays.asList(1, 4, 5)));
        data.add(new HashSet<Integer>(Arrays.asList(1, 2, 3)));
        data.add(new HashSet<Integer>(Arrays.asList(1, 2, 3, 4)));
        data.add(new HashSet<Integer>(Arrays.asList(1)));
        data.add(new HashSet<Integer>(Arrays.asList(1, 2, 3)));
        data.add(new HashSet<Integer>(Arrays.asList(1, 4, 5)));
        data.add(new HashSet<Integer>(Arrays.asList(2, 3, 5)));

        Apriori t = new Apriori(data, 4);

        List<Set<Integer>> items = t.getfreqItem();
        for (Set<Integer> fi : items) {
            System.out.println(fi);
        }
        System.out.println(t.getSupportData());
        assertNotNull(t.getSupportData());
    }

}
