package cn.zyj.tunnel.leetcode;

import cn.zyj.tunnel.utils.InputUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate169_2 {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public List<Integer> getAllElements(TreeNode root1, TreeNode root2) {
//        PriorityQueue<Integer> queue = new PriorityQueue<>(Integer::compare);
        List<Integer> queue = new ArrayList<>();
        dfs(root1, queue);
        dfs(root2, queue);
        Collections.sort(queue);
        return queue;
    }

    public void dfs(TreeNode node, List<Integer> queue) {
        if (node == null) return;
        queue.add(node.val);
        if (node.left != null) {
            dfs(node.left, queue);
        }
        if (node.right != null) {
            dfs(node.right, queue);
        }
    }

    @Test
    public void test() {
//        int[] arr = InputUtil.parseIntArray("[12,345,2,6,7896]");
//        log.info("" + findNumbers(arr));
    }

}
