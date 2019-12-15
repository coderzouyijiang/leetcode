package cn.zyj.tunnel.leetcode;

import cn.zyj.tunnel.utils.InputUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@RunWith(JUnit4.class)
public class Test167 {

    private int[] addNums = {0, 1, 11, 111, 1111, 11111,
            111111, 1111111, 11111111, 111111111};
    private int[] beginNums = {0, 1, 12, 123, 1234, 12345,
            123456, 1234567, 12345678, 123456789};

    // 10 <= low <= high <= 10^9
    public List<Integer> sequentialDigits(int low, int high) {
        int n0 = Math.max((int) Math.log10(low) + 1, 2);
        int n1 = Math.min((int) Math.log10(high) + 1, addNums.length - 1);
        List<Integer> list = new LinkedList<>();
        loop1:
        for (int n = n0; n <= n1; n++) {
            int beginNum = beginNums[n];
            int step = addNums[n];
            loop2:
            for (; true; beginNum += step) {
//                log.info("n:" + n + ",begin:" + beginNum + ",step:" + step);
                if (beginNum >= low) {
                    if (beginNum > high) break loop1;
//                    log.info("add:" + beginNum);
                    list.add(beginNum);
                }
                if (beginNum % 10 == 9) break loop2;
            }
        }
        return list;
    }

    @Test
    public void test() {
      /*
//        输出：low = 1000, high = 13000
//        输出：[1234,2345,3456,4567,5678,6789,12345]
        log.info("" + sequentialDigits(1000, 13000));
        Assert.assertEquals("[1234, 2345, 3456, 4567, 5678, 6789, 12345]", "" + sequentialDigits(1000, 13000));
        */
        /*
        8511 23553
        [9011,10122,11233,12344,13455,14566,15677,16788,17899,12345,23456]
         */
        List<Integer> list3 = sequentialDigits(8511, 23553);
        log.info("list=" + list3);
        List<Integer> list2 = JSON.parseArray("[12345,23456]", Integer.class);
        Assert.assertEquals(list2, list3);
    }

    final int L = 1, V = 1;

    public int maxSideLength0(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;
        // dp[i][j] 以(i,j)为右下角的和小于阈值的最大正方形
        int[][][] dp = new int[m][n][2];
        int maxLen = 0; // 边长
        for (int i = 0; i < m; i++) {
            dp[i][0][L] = 1;
            int v = dp[i][0][V] = mat[i][0]; // 总和
            if (v <= threshold) maxLen = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j][L] = 1;
            int v = dp[0][j][V] = mat[0][j]; // 总和
            if (v <= threshold) maxLen = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                int v = mat[i][j];
                if (dp[i][j][V] + v > threshold || dp[i][j - 1][V] + v > threshold || dp[i - 1][j][V] + v > threshold) {
                    continue;
                }
                for (int k = 0; k < i; i++) v += mat[k][j];
                for (int k = 0; k < j; i++) v += mat[i][k];
                v += dp[i][j][V];
            }
        }

        return 0;
    }

    public int maxSideLength(int[][] mat, int threshold) {

        return -1;
    }

    @Test
    public void test3() {
        /*
        输入：mat = [[1,1,3,2,4,3,2],[1,1,3,2,4,3,2],[1,1,3,2,4,3,2]], threshold = 4
        输出：2
        解释：总和小于 4 的正方形的最大边长为 2，如图所示。
         */
        int[][] grid1 = InputUtil.parseIntGrid("[[1,1,3,2,4,3,2],[1,1,3,2,4,3,2],[1,1,3,2,4,3,2]]");
        Assert.assertEquals(2, maxSideLength(grid1, 4));
    }

    final static int x = 0, y = 1, step = 2, clear = 3;
    final static int[] dx = {-1, 1, 0, 0};
    final static int[] dy = {0, 0, -1, 1};

    public int shortestPath(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int endX = m - 1, endY = n - 1;
        boolean[][][] passed = new boolean[m][n][k + 1];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0, 0, k});
        int[] state;
        while ((state = queue.poll()) != null) {
            if (passed[state[x]][state[y]][state[clear]]) continue;
            if (state[x] == endX && state[y] == endY) {
                return state[step];
            }
            passed[state[x]][state[y]][state[clear]] = true;

            int step2 = state[step] + 1;
            for (int i = 0; i < dx.length; i++) {
                int x2 = state[x] + dx[i];
                int y2 = state[y] + dy[i];
                if (x2 < 0 || x2 >= m || y2 < 0 || y2 >= n) continue;
                if (x2 == endX && y2 == endY) {
                    return step2;
                }
                int clear2 = state[clear];
                if (grid[x2][y2] == 0) {
                } else if (state[clear] > 0) {
                    clear2--;
                } else {
                    continue;
                }
                queue.offer(new int[]{x2, y2, step2, clear2});
            }
        }
        return -1;
    }

    @Test
    public void test4() {
        int[][] grid = InputUtil.parseIntGrid("" +
                "[[0,0,0],\n" +
                " [1,1,0],\n" +
                " [0,0,0],\n" +
                " [0,1,1],\n" +
                " [0,0,0]]");
        Assert.assertEquals(6, shortestPath(grid, 1));
    }

    @Test
    public void test5() {
        int[][] grid = InputUtil.parseIntGrid("" +
                "[[0,0,1,0,0,0,0,1,0,1,1,0,0,1,1],[0,0,0,1,1,0,0,1,1,0,1,0,0,0,1],[1,1,0,0,0,0,0,1,0,1,0,0,1,0,0],[1,0,1,1,1,1,0,0,1,1,0,1,0,0,1],[1,0,0,0,1,1,0,1,1,0,0,1,1,1,1],[0,0,0,1,1,1,0,1,1,0,0,1,1,1,1],[0,0,0,1,0,1,0,0,0,0,1,1,0,1,1],[1,0,0,1,1,1,1,1,1,0,0,0,1,1,0],[0,0,1,0,0,1,1,1,1,1,0,1,0,0,0],[0,0,0,1,1,0,0,1,1,1,1,1,1,0,0],[0,0,0,0,1,1,1,0,0,1,1,1,0,1,0]]"
        );
        log.info("" + shortestPath(grid, 27));
//        Assert.assertEquals(6, shortestPath(grid, 27));
    }
}
