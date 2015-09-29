package com.littledudu.algorithm.learning.fpm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hujinjun
 * @date 2015-9-29
 */
public final class FPTree {

    private FPNode root;
    private Map<Integer, FPNode> nodeLists;
    private Map<Integer, Long> supports;
    private final long minSupport;

    private List<List<Integer>> freqItem;

    public FPTree(long minSupport) {
        nodeLists = new HashMap<Integer, FPNode>();
        supports = new HashMap<Integer, Long>();
        root = new FPNode(null, Integer.MIN_VALUE);
        this.minSupport = minSupport;
    }

    public FPTree(List<List<Integer>> data, long minSupport) {
        nodeLists = new HashMap<Integer, FPNode>();
        supports = new HashMap<Integer, Long>();
        root = new FPNode(null, Integer.MIN_VALUE);
        this.minSupport = minSupport;

        preprocess(data);
        for (List<Integer> trans : data) {
            updateTree(trans);
        }

        ArrayList<Integer> prefix = new ArrayList<Integer>();
        List<List<Integer>> freqItemList = new ArrayList<List<Integer>>();
        mineTree(prefix, freqItemList);
        // System.out.println(freqItemList);
        freqItem = freqItemList;
    }

    public List<List<Integer>> getfreqItem() {
        return freqItem;
    }

    private void preprocess(List<List<Integer>> data) {
        Map<Integer, Long> sup = new HashMap<Integer, Long>();
        for (List<Integer> trans : data) {
            for (int attr : trans) {
                Long cnt = sup.get(attr);
                if (cnt == null) {
                    sup.put(attr, 1L);
                } else {
                    sup.put(attr, cnt + 1L);
                }
            }
        }
        Iterator<List<Integer>> it = data.iterator();
        while (it.hasNext()) {
            List<Integer> trans = it.next();
            Iterator<Integer> i = trans.iterator();
            while (i.hasNext()) {
                int attr = i.next();
                if (sup.get(attr) < minSupport) {
                    i.remove();
                }
            }
            if (trans.isEmpty()) {
                it.remove();
            } else {
                Collections.sort(trans);
            }
        }
    }

    public boolean isBlank() {
        return root.children.isEmpty();
    }

    public void updateTree(List<Integer> sortedTransaction) {
        FPNode n = root;
        for (int attribute : sortedTransaction) {
            Long cnt = supports.get(attribute);
            if (cnt == null) {
                supports.put(attribute, 1L);
            } else {
                supports.put(attribute, cnt + 1L);
            }
            Map<Integer, FPNode> children = n.getChildren();
            FPNode child = children.get(attribute);
            if (child == null) {
                child = new FPNode(n, attribute);
                children.put(attribute, child);
                child.incr(1);
                FPNode nodeList = nodeLists.get(attribute);
                if (nodeList == null) {
                    nodeLists.put(attribute, child);
                } else {
                    while (nodeList.nodeLink != null)
                        nodeList = nodeList.nodeLink;
                    nodeList.nodeLink = child;
                }
            } else {
                child.incr(1);
            }
            n = child;
        }
        root.incr(1);
    }

    public void printTree(FPNode n, int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append('*');
        }
        sb.append(n.attribute);
        sb.append(' ');
        sb.append(n.count);
        System.out.println(sb.toString());
        for (FPNode node : n.getChildren().values()) {
            printTree(node, depth + 1);
        }
    }

    @SuppressWarnings("unchecked")
    public void mineTree(ArrayList<Integer> prefix, List<List<Integer>> freqItemList) {
        if (isBlank())
            return;

        for (int attribute : nodeLists.keySet()) {

            ArrayList<Integer> newFreqSet = (ArrayList<Integer>) prefix.clone();
            newFreqSet.add(attribute);
            freqItemList.add(newFreqSet);

            Set<PrefixPath> set = findPrefixPath(attribute);
            if (!set.isEmpty()) {
                List<List<Integer>> data = new ArrayList<List<Integer>>();
                for (PrefixPath pf : set) {
                    for (int i = 0; i < pf.count; i++) {
                        data.add(pf.prefix);
                    }
                }
                FPTree conditionalFPTree = new FPTree(data, minSupport);
                if (!conditionalFPTree.isBlank()) {
                    conditionalFPTree.mineTree(newFreqSet, freqItemList);
                }
            }
        }
    }

    public Map<Integer, Set<PrefixPath>> findPrefixPath() {
        Map<Integer, Set<PrefixPath>> map = new HashMap<Integer, Set<PrefixPath>>();
        for (int attribute : nodeLists.keySet()) {
            map.put(attribute, findPrefixPath(attribute));
        }
        return map;
    }

    private Set<PrefixPath> findPrefixPath(int attribute) {
        Set<PrefixPath> set = new HashSet<PrefixPath>();
        FPNode n = nodeLists.get(attribute);
        while (n != null) {
            FPNode node = n;
            long count = n.getCount();
            List<Integer> prefix = new LinkedList<Integer>();

            while ((node = node.parent) != root) {
                prefix.add(0, node.getAttribute());
            }
            set.add(new PrefixPath(attribute, prefix, count));
            n = n.getNodeLink();
        }
        return set;
    }

    public FPNode getRoot() {
        return root;
    }

    public static final class PrefixPath {
        private int attribute;
        private List<Integer> prefix;
        private long count;

        public PrefixPath(int attribute, List<Integer> prefix, long count) {
            super();
            this.attribute = attribute;
            this.prefix = prefix;
            this.count = count;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("PrefixPath [");
            builder.append(attribute);
            builder.append(", ");
            builder.append(prefix);
            builder.append(", ");
            builder.append(count);
            builder.append("]");
            return builder.toString();
        }
    }
    public static final class FPNode {
        private final FPNode parent;
        private final int attribute;
        private long count;
        private Map<Integer, FPNode> children = new HashMap<Integer, FPNode>();
        private FPNode nodeLink;

        private FPNode(FPNode parent, int attribute) {
            this.parent = parent;
            this.attribute = attribute;
        }

        public void incr(int delta) {
            count += delta;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public Map<Integer, FPNode> getChildren() {
            return children;
        }

        public void setChildren(Map<Integer, FPNode> children) {
            this.children = children;
        }

        public FPNode getNodeLink() {
            return nodeLink;
        }

        public void setNodeLink(FPNode nodeLink) {
            this.nodeLink = nodeLink;
        }

        public FPNode getParent() {
            return parent;
        }

        public int getAttribute() {
            return attribute;
        }
    }
}
