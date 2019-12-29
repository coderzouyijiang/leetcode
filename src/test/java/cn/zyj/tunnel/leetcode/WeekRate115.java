package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate115 {

    /*
    cells.length == 8
cells[i] 的值为 0 或 1
1 <= N <= 10^9
     */
    public int[] prisonAfterNDays(int[] cells, int N) {
        if (N == 0) return cells;
        int state = 0;
        int mask = 0;
        for (int i = cells.length - 1, k = 1; i >= 0; i--, k <<= 1) {
            if (cells[i] == 1) {
                state |= k;
            }
            if (i > 0 && i < cells.length - 1) {
                mask |= k;
            }
        }
//        log.info("state:{}", String.format("%8s", Integer.toBinaryString(state)).replace(' ', '0'));
//        log.info("mask :{}", String.format("%8s", Integer.toBinaryString(mask)).replace(' ', '0'));
        List<Integer> states = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            state = (~((state >> 1) ^ (state << 1))) & mask;
//            log.info("{}:{}", String.format("%02d", i), String.format("%8s", Integer.toBinaryString(state)).replace(' ', '0'));
            if (i > 0 && state == states.get(0)) {
                break;
            }
            states.add(state);
        }
        int index = N <= states.size() ? (states.size() - 1) : (N - 1) % states.size();
        int last = states.get(index);
//        log.info("index={},last={}", index, last);
        int[] arr = new int[cells.length];
        for (int i = cells.length - 1, k = 1; i >= 0; i--, k <<= 1) {
            arr[i] = (last & k) > 0 ? 1 : 0;
        }
        return arr;
    }

    @Test
    public void test() {
        // 0, 1, 1, 0, 0, 0, 0, 0
        int[] arr = prisonAfterNDays(new int[]{0, 1, 0, 1, 1, 0, 0, 1}, 7);
        int[] expect = new int[]{0, 0, 1, 1, 0, 0, 0, 0};
        log.info("expect:" + Arrays.toString(expect));
        log.info("actual:" + Arrays.toString(arr));
        Assert.assertArrayEquals(expect, arr);
    }

    @Test
    public void test2() {
        // [1,0,0,1,0,0,1,0]
        // 1000000000
        int[] arr = prisonAfterNDays(new int[]{1, 0, 0, 1, 0, 0, 1, 0}, 1000000000);
        int[] expect = new int[]{0, 0, 1, 1, 1, 1, 1, 0};
        log.info("expect:" + Arrays.toString(expect));
        log.info("actual:" + Arrays.toString(arr));
        Assert.assertArrayEquals(expect, arr);
    }

    @Test
    public void test3() {
        // [1,0,0,1,0,0,1,0]
        // 1000000000
        int[] arr = prisonAfterNDays(new int[]{1, 1, 0, 1, 1, 0, 1, 1}, 6);
        int[] expect = new int[]{0, 0, 1, 1, 1, 1, 1, 0};
        log.info("actual:" + Arrays.toString(arr));
//        log.info("expect:" + Arrays.toString(expect));
//        Assert.assertArrayEquals(expect, arr);
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public boolean isCompleteTree(TreeNode root) {
        List<TreeNode> nodes = new ArrayList<>(1);
        List<TreeNode> nodes2 = Collections.EMPTY_LIST;
        nodes.add(root);
        int width = 1;
        while (!nodes.isEmpty()) {
            width *= 2;
            nodes2 = new ArrayList<>(width);
            for (TreeNode node : nodes) {
                if (node.left != null) nodes2.add(node.left);
                if (node.right != null) nodes2.add(node.right);
            }
            if (nodes2.size() < width) {
                break;
            }
            nodes = nodes2;
        }
        for (int i = 0; i < nodes2.size(); i++) {
            TreeNode child = i % 2 == 0 ? nodes.get(i / 2).left : nodes.get(i / 2).right;
            if (child != nodes2.get(i)) return false;
            if (child.left != null || child.right != null) return false;
        }
        return true;
    }

}
