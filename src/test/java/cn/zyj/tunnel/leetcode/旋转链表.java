package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import scala.concurrent.java8.FuturesConvertersImpl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
                } else {
                    return head;
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

    public int majorityElement0(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        return map.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(-1);
    }

    public int majorityElement(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            final int count = countElement(nums[i], nums, 0, nums.length);
            if (count * 2 >= nums.length) {
                return nums[i];
            }
        }
        return -1;
    }

    private int countElement(int x, int[] nums, int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (nums[i] == x) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void test2() {
        final int result = majorityElement(new int[]{3, 2, 3});
        log.info("" + result);
    }

    public boolean isPalindrome(int x) {
        if (x < 0) return false;
        if (x < 10) return true;
        // 32为整数[2147483647,-2147483648]
        byte[] bytes = new byte[10];
        int num = x;
        int k = 0; // 位数
        while (num > 0) {
            int remain = num % 10;
            bytes[k++] = (byte) remain;
            num = (num - remain) / 10;
        }
        if (k == 1) return true;
        if (bytes[k - 1] == 0) return false;
        int k2 = k / 2;
        for (int i = 0; i <= k2; i++) {
            if (bytes[i] != bytes[k - 1 - i]) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void test3() {
//        log.info("" + isPalindrome(10201));
//        log.info("" + isPalindrome(102010));
//        log.info("" + isPalindrome(0102010));
//        log.info("" + isPalindrome(10));
        log.info("" + isPalindrome(0));
    }

    @Test
    public void test4() {
        String str = "";
        for (int i = 0; i <= 32; i++) {
            str += "," + (int) Math.pow(2, i);
        }
        log.info(str);
    }

    private static final int[] pows = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, 2147483647};

    public boolean isPowerOfTwo(int n) {
        return Integer.bitCount(n) <= 1;
    }

    @Test
    public void test5() {
        log.info("" + Arrays.binarySearch(pows, 3));
        log.info("" + Arrays.binarySearch(pows, 2));
        log.info("" + isPowerOfTwo(3));
        log.info("" + isPowerOfTwo(2));
        log.info("" + Integer.toBinaryString(4));

        log.info("" + Integer.numberOfLeadingZeros(4));
        log.info("" + Integer.highestOneBit(4));
        log.info("" + Integer.lowestOneBit(4));
        log.info("" + Integer.bitCount(4));
        log.info("" + Integer.bitCount(0));
        log.info("" + Integer.bitCount(Integer.MAX_VALUE));
        log.info("" + Integer.highestOneBit(Integer.MAX_VALUE));
        log.info("" + Integer.lowestOneBit(Integer.MAX_VALUE));
    }
}
