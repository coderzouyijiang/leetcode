package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.stream.IntStream;

@Slf4j
@RunWith(JUnit4.class)
public class 旋转链表 {

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return this.val + "->" + this.next;
        }
    }

    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k <= 0) {
            return head;
        }
        ListNode node = head;
        ListNode pre = null;
        ListNode last = null;
        for (int step = 0; last == null || step < k; step++) {
            pre = node;
            if (node.next != null) {
                node = node.next;
            } else {
                last = node;
                node = head;
//                k = (k % step) + step;
                int len = step + 1;
                k = k % len;
                if (k != 0) {
                    k = (len - k) + len;
                }
            }
        }
        if (node == head) {
            return head;
        }
        pre.next = null;
        last.next = head;
        return node;
    }

    public ListNode createList(int start, int end) {
        ListNode next = null;
        for (int i = end; i >= start; i--) {
            next = new ListNode(i, next);
        }
        return next;
    }

    @Test
    public void test() {
        ListNode list1 = createList(1, 5);
        log.info("list1:" + list1);
        list1 = rotateRight(list1, 2);
        log.info("list1:" + list1);
        list1 = rotateRight(list1, 5);
        log.info("list1:" + list1);


        ListNode list2 = createList(1, 2);
        log.info("list2:" + list2);
        list2 = rotateRight(list2, 2);
        log.info("list2:" + list2);
    }

}
