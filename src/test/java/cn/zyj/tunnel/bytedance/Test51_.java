package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

@Slf4j
@RunWith(JUnit4.class)
public class Test51_AllOne {

    class AllOne {

        public class Node {
            public Node pre;
            public Node next;
            public int val;
            public HashSet<String> keys = new HashSet<>();

            public Node(int val) {
                this.val = val;
            }
        }

        private Node head;
        private Node tail;
        private HashMap<String, Node> key2Node;
        private HashMap<Integer, Node> val2Node;

        /**
         * Initialize your data structure here.
         */
        public AllOne() {
            head = new Node(0);
            tail = new Node(0);
            head.next = tail;
            tail.pre = head;
            key2Node = new HashMap<>();
            val2Node = new HashMap<>();
        }

        private void insertNode(String key, int addVal) {
            Node node = key2Node.get(key);
            if (node != null) {
                int v1 = node.val;
                int v2 = v1 + addVal;
                if (v2 <= 0) {
                    key2Node.remove(key);
                    node.keys.remove(key);
                    if (node.keys.isEmpty()) {
                        val2Node.remove(v1);
                    }
                } else {
                    Node node2 = val2Node.get(v2);
                    if (node2 == null) {
                        node2 = new Node(v2);
                        val2Node.put(v2, node2);
                    }
                    key2Node.put(key, node2);
                    node2.keys.add(key);
                }
            } else if (addVal <= 0) {
                return;
            } else {
                node = new Node(addVal);
            }

        }

        /**
         * Inserts a new key <Key> with value 1. Or increments an existing key by 1.
         */
        public void inc(String key) {
            Node node = key2Node.get(key);
            if (node == null) {
                node = new Node(1);
                node.keys.add(key);
            }
        }

        /**
         * Decrements an existing key by 1. If Key's value is 1, remove it from the data structure.
         */
        public void dec(String key) {

        }

        /**
         * Returns one of the keys with maximal value.
         */
        public String getMaxKey() {
            return null; // todo
        }

        /**
         * Returns one of the keys with Minimal value.
         */
        public String getMinKey() {
            return null; // todo
        }
    }

    /**
     * Your AllOne object will be instantiated and called as such:
     * AllOne obj = new AllOne();
     * obj.inc(key);
     * obj.dec(key);
     * String param_3 = obj.getMaxKey();
     * String param_4 = obj.getMinKey();
     */

    @Test
    public void test() {
        AllOne solution = new AllOne();
        Assert.assertEquals(0, solution);
    }
}
