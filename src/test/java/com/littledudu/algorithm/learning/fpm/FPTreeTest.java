package com.littledudu.algorithm.learning.fpm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.littledudu.algorithm.learning.fpm.FPTree.PrefixPath;

public class FPTreeTest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void testGetfreqItem() {
        List<List<Integer>> data = new ArrayList<List<Integer>>();
        data.add(new ArrayList<Integer>(Arrays.asList(1, 2)));
        data.add(new ArrayList<Integer>(Arrays.asList(2, 3, 4)));
        data.add(new ArrayList<Integer>(Arrays.asList(1, 3, 4, 5)));
        data.add(new ArrayList<Integer>(Arrays.asList(1, 4, 5)));
        data.add(new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
        data.add(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4)));
        data.add(new ArrayList<Integer>(Arrays.asList(1)));
        data.add(new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
        data.add(new ArrayList<Integer>(Arrays.asList(1, 4, 5)));
        data.add(new ArrayList<Integer>(Arrays.asList(2, 3, 5)));

        FPTree t = new FPTree(data, 7);
        t.printTree(t.getRoot(), 0);

        for (Set<PrefixPath> set : t.findPrefixPath().values()) {
            System.out.println(set);
        }

        List<List<Integer>> items = t.getfreqItem();
        assertNotNull(items);
        for (List<Integer> fi : items) {
            System.out.println(fi);
        }
    }

}
