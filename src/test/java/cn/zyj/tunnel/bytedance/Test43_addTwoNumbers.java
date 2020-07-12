package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class Test43_addTwoNumbers {

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

        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode head = new ListNode();
            int val = 0;
            ListNode preNode = head;
            while (l1 != null || l2 != null || val > 0) {
                if (l1 != null) {
                    val += l1.val;
                    l1 = l1.next;
                }
                if (l2 != null) {
                    val += l2.val;
                    l2 = l2.next;
                }
                ListNode node = new ListNode(val % 10);
                preNode.next = node;
                preNode = node;
                val = val / 10;
            }
            return head.next;
        }

    }


    @Test
    public void test() {
        Solution solution = new Solution();
        ListNode list1 = new ListNode(2, new ListNode(4, new ListNode(3)));
        ListNode list2 = new ListNode(5, new ListNode(6, new ListNode(4)));
        ListNode list3 = solution.addTwoNumbers(list1, list2);
        log.info("");
    }
}
