package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class Test42_reverseList {

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

        public ListNode reverseList(ListNode head) {
            return helpReverseList(null, head);
        }

        private ListNode helpReverseList(ListNode preList, ListNode node) {
            if (node == null) return preList;
            ListNode temp = node.next;
            node.next = preList;
            return helpReverseList(node, temp);
        }

    }

    class Solution2 {

        public ListNode reverseList(ListNode head) {
            ListNode node = head;
            ListNode next = node.next;
            node.next = null;
            while (next != null) {
                ListNode temp = next.next;
                next.next = node;
                node = next;
                next = temp;
            }
            return node;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        log.info("");
    }
}
