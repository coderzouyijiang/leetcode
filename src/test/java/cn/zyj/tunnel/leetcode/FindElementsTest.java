package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

@Slf4j
@RunWith(JUnit4.class)
public class FindElementsTest {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }

        @Override
        public String toString() {
            return "[" +
                    "" + val +
                    "," + left +
                    "," + right +
                    ']';
        }
    }

    public static class FindElements {
        private TreeNode root;

        public FindElements(TreeNode root) {
            this.root = root;
            resume(root, 0);
        }

        private void resume(TreeNode root, int val) {
            if (root == null) return;
            root.val = val;
            resume(root.left, 2 * val + 1);
            resume(root.right, 2 * val + 2);
        }

        public boolean find(int target) {
            int i = 0;
            int x = 1; // 2^1
            while ((x - 2) < target) { // (2^i)-1 <= target <= (2^(i+1))-2
                x = x << 1;
                i++;
            }
            if (i < 0 || i >= 20) return false;
            return find(root, target, (x >> 1) - 1, x - 2);
        }

        private boolean find(TreeNode root, int target, int min, int max) {
            if (root == null) return false;
            if (root.val == target) return true;
            int mid = min + ((max - min) >> 1);
            if (target <= mid) {
                return find(root.left, target, min, mid);
            } else {
                return find(root.right, target, mid + 1, max);
            }
        }

        public static TreeNode createTree(List<Integer> list) {
            if (list.isEmpty() || list.get(0) == null) return null;
            Queue<TreeNode> nodes = new LinkedList<>();
            int i = 0;
            TreeNode root = new TreeNode(list.get(i++));
            nodes.offer(root);
            TreeNode node;
            while ((node = nodes.poll()) != null) {
                if (i >= list.size()) break;
                Integer val = list.get(i++);
                if (val != null) {
                    node.left = new TreeNode(val);
                    nodes.offer(node.left);
                }
                if (i >= list.size()) break;
                val = list.get(i++);
                if (val != null) {
                    node.right = new TreeNode(val);
                    nodes.offer(node.right);
                }
            }
            return root;
        }
    }

    public static class FindElements2 {
        private TreeNode root;

        private byte[] data;
        private int len;

        public FindElements2(TreeNode root) {
            this.root = root;
            this.len = 1 << 8;
            this.data = new byte[len / 8];
            resume(root, 0);
        }

        private void resume(TreeNode root, int val) {
            if (root == null) return;

            while (val >= len) {
                len <<= 1;
            }
            if (len > data.length * 8) {
                data = Arrays.copyOf(data, len / 8);
            }
            int i = val / 8;
            int j = val % 8;
            data[i] |= (1 << j);

            root.val = val;
            resume(root.left, 2 * val + 1);
            resume(root.right, 2 * val + 2);
        }

        public boolean find(int target) {
            if (target >= len) {
                return false;
            }
            int i = target / 8;
            int j = target % 8;
            return (data[i] & (1 << j)) > 0;
        }

    }

    public List<Integer> findFloor(int target) {
        int i = 0;
        int x = 1; // 2^1
        while ((x - 2) < target) { // (2^i)-1 <= target <= (2^(i+1))-2
            x = x << 1;
            i++;
        }
        return Arrays.asList(i - 1, (x >> 1) - 1, x - 2);
    }

    @Test
    public void test() {
        log.info("" + findFloor(0));
        log.info("" + findFloor(2));
        log.info("" + findFloor(1));
        log.info("" + findFloor(3));
        log.info("" + findFloor(6));
        log.info("" + findFloor(9));
        log.info("" + findFloor(7));
        log.info("" + findFloor(14));
        log.info("" + findFloor(-1));
    }

    @Test
    public void test2() {
//        TreeNode tree = createTree(Arrays.asList(-1, -1, -1, -1, -1));
//        log.info("" + tree);
        //[[[-1,null,-1,-1,null,-1]],[2],[3],[4],[5]]
        // [null,true,false,false,true]
        TreeNode tree2 = FindElements.createTree(Arrays.asList(-1, null, -1, -1, null, -1, null, -1, null, -1, null, -1));
        log.info("" + tree2);
        FindElements2 findElements = new FindElements2(tree2);
        log.info("" + findElements.root);
        String str = "";
        for (byte b : findElements.data) {
            for (int i = 0; i < 8; i++) {
                str += ((b >> i) & 1);
            }
            str += " ";
//            str += " " + String.format("%8s", Integer.toBinaryString(b)).replaceAll(" ", "0");
        }
        log.info(str);
        Assert.assertEquals(true, findElements.find(5));
        Assert.assertEquals(true, findElements.find(2));
        Assert.assertEquals(false, findElements.find(3));
        Assert.assertEquals(false, findElements.find(4));
        Assert.assertEquals(true, findElements.find(95));

    }
}
