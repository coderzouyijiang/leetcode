package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Comparator;

@Slf4j
@RunWith(JUnit4.class)
public class Test41_mergeTwoLists {

    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    class Solution {
        public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
            if (l1 == null) return l2;
            if (l2 == null) return l1;
            if (l1.val <= l2.val) {
                l1.next = mergeTwoLists(l1.next, l2);
                return l1;
            } else {
                l2.next = mergeTwoLists(l1, l2.next);
                return l2;
            }
        }

    }

    @Test
    public void test() {
        Solution solution = new Solution();
        // [[5,4],[6,4],[6,7],[2,3]]
        ListNode listNode1 = solution.mergeTwoLists(new ListNode(2), new ListNode(1));
        ListNode listNode = solution.mergeTwoLists(new ListNode(5), new ListNode(1, new ListNode(2, new ListNode(4))));
        log.info("");
    }
}
