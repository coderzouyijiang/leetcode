package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Slf4j
@RunWith(JUnit4.class)
public class Test35_minimumTotal {

    //    给定一个三角形，找出自顶向下的最小路径和。每一步只能移动到下一行中相邻的结点上。
    //    相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。
    class Solution {
        public int minimumTotal(List<List<Integer>> triangle) {
            if (triangle.size() == 0) return 0;
            // 从下往上遍历
            ListIterator<List<Integer>> it0 = triangle.listIterator(triangle.size());
            List<Integer> list1 = it0.previous();
            ListIterator<Integer> it1 = list1.listIterator(0);
            int dp[] = new int[list1.size()];
            while (it1.hasNext()) {
                dp[it1.nextIndex()] = it1.next();
            }
            while (it0.hasPrevious()) {
                List<Integer> list = it0.previous();
                ListIterator<Integer> it = list.listIterator(0);
                while (it.hasNext()) {
                    int i = it.nextIndex();
                    dp[i] = it.next() + Math.min(dp[i], dp[i + 1]);
                }
            }
            return dp[0];
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(11, solution.minimumTotal(Arrays.asList(
                Arrays.asList(2)
                , Arrays.asList(3, 4)
                , Arrays.asList(6, 5, 7)
                , Arrays.asList(4, 1, 8, 3)
        )));
    }
}
