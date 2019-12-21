package cn.zyj.tunnel.leetcode;

import cn.zyj.tunnel.utils.InputUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

@Slf4j
public class DoubleWeekRate15 {

    public int removeCoveredIntervals(int[][] intervals) {
        // 按left排序,n*log(n)
        Arrays.sort(intervals, Comparator.comparingInt(it -> it[0]));
        int count = 0;
        int maxRight = 0;
        // n
        for (int[] interval : intervals) {
            // left>=前一个left
            int right = interval[1];
            if (right > maxRight) {
                maxRight = right;
                count++;
            }
        }
        return count;
    }

    class CombinationIterator {

        private String allChars;
        private int[] ps;
        private int len;
        private String nextStr;

        public CombinationIterator(String characters, int combinationLength) {
            this.allChars = characters;
            this.len = combinationLength;
            ps = new int[len + 1];
            String str = "";
            for (int i = 0; i < len; i++) {
                ps[i] = i;
                str += allChars.charAt(i);
            }
            this.nextStr = str;
            ps[len] = allChars.length();
        }

        public String next() {
            if (this.nextStr != null || hasNext()) {
                String str = this.nextStr;
                this.nextStr = null;
                return str;
            } else {
                throw new NoSuchElementException("没有下一个字母组合了");
            }
        }

        public boolean hasNext() {
            if (this.nextStr != null) {
                return true;
            }
            boolean isChange = false;
            for (int i = len - 1; i >= 0; i--) {
                int nextIndex = ps[i] + 1;
                if (nextIndex >= ps[i + 1]) continue;
                isChange = true;
                ps[i] = nextIndex;
                for (int j = i + 1, index = nextIndex + 1; j < len; j++, index++) {
                    ps[j] = index;
                }
                break;
            }
            if (isChange) {
                String str = "";
                for (int i = 0; i < len; i++) {
                    str += allChars.charAt(ps[i]);
                }
                this.nextStr = str;
            }
            return isChange;
        }
    }

    @Test
    public void test2() {
        CombinationIterator it = new CombinationIterator("abcd", 2);
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    // allChars中第k个字母开始可选，还要选h个字母
    public void combStr(String allChars, String str, int k, int h, List<String> result) {
        log.info("{},{},{}", str, k, h);
        if (h == 0) {
            result.add(str);
            return;
        }
        int optionCharLen = allChars.length() - (h - 1);
        for (int i = k; i < optionCharLen; i++) {
            char ch = allChars.charAt(i);
            combStr(allChars, str + ch, i + 1, h - 1, result);
        }
    }

    @Test
    public void test_combStr() {
        List<String> result = new LinkedList<>();
        combStr("abcd", "", 0, 2, result);
        log.info("" + result);
    }

    @Test
    public void test_combStr2() {
        List<String> result = new LinkedList<>();
        combStr("abc", "", 0, 2, result);
        log.info("" + result);
    }

    public int minFallingPathSum0(int[][] arr) {
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(it -> it[2]));
//        Queue<int[]> queue = new LinkedList<>();
        // (i,j，总和)
        queue.offer(new int[]{-1, -1, 0});
        int[] state;
        int index = 0;
        int minSum = Integer.MAX_VALUE;
        while ((state = queue.poll()) != null) {
            final int sum = state[2];
            final int j2 = state[1] + 1;
            if (j2 >= arr.length) {
                log.info("{},{}", ++index, Arrays.toString(state));
                minSum = Math.min(minSum, sum);
                continue;
            }
            final int i = state[0];
            int[] arr2 = arr[j2];
            for (int i2 = 0; i2 < arr2.length; i2++) {
                if (i2 == i) continue;
                int sum2 = sum + arr2[i2];
                queue.offer(new int[]{i2, j2, sum2});
            }
//            log.info("{}", queueToStr(queue));
        }
        return minSum;
    }

    private String queueToStr(Queue<int[]> queue) {
        String str = "";
        for (int[] state : queue) {
            str += Arrays.toString(state) + ",";
        }
        return str;
    }

    // 不好理解
    public int minFallingPathSum(int[][] arrs) {
        int[][] state = {{-1, 0}};
        for (int[] arr : arrs) {
            log.info("{},{}", Arrays.deepToString(state), Arrays.toString(arr));
            int[][] state3 = {{-1, Integer.MAX_VALUE}, {-2, Integer.MAX_VALUE}};
            for (int j = 0; j < state.length; j++) {
                int[] arr1 = state[j];
                for (int k = 0; k < arr.length; k++) {
                    if (k == arr1[0] || k == state3[0][0] || k == state3[1][0]) continue;
                    minState(state3, arr1[1] + arr[k], k);
                }
            }
            state = state3;
        }
        return state[0][1];
    }

    private void minState(int[][] state2, int v, int i) {
        if (v < state2[1][1]) {
            if (v < state2[0][1]) {
                state2[1][0] = state2[0][0];
                state2[1][1] = state2[0][1];
                state2[0][0] = i;
                state2[0][1] = v;
            } else {
                state2[1][0] = i;
                state2[1][1] = v;
            }
        }
    }

    public int minFallingPathSum3(int[][] arrs) {
        if (arrs.length == 1) return arrs[0][0];
        return minFallingPathSum(arrs, 0, arrs.length - 1)[0][0];
    }

    // [ (sum,top,bottom),( sum,top,bottom) ]
    public int[][] minFallingPathSum(int[][] arrs, int start, int end) {
        if (end - start == 0) {
            int[] arr = arrs[start];
            int[][] state = {{Integer.MAX_VALUE, -1, -1}, {Integer.MAX_VALUE, -1, -1}};
            for (int i = 0; i < arr.length; i++) {
                minState(state, arr[i], i, i);
            }
            log.info(start + ":" + Arrays.deepToString(state));
            return state;
        }
        int mid = start + (end - start) / 2;
        int[][] state1 = minFallingPathSum(arrs, start, mid);
        int[][] state2 = minFallingPathSum(arrs, mid + 1, end);

        int[][] state3 = {{Integer.MAX_VALUE, -1, -1}, {Integer.MAX_VALUE, -1, -1}};
        for (int[] arr1 : state1) {
            for (int[] arr2 : state2) {
                if (arr1[2] == arr2[1]) continue;
                minState(state3, arr1[0] + arr2[0], arr1[1], arr2[2]);
            }
        }
        log.info(start + "-" + end + ":" + Arrays.deepToString(state3));
        return state3;
    }

    private void minState(int[][] state2, int v, int i, int j) {
        if (v < state2[0][0]) {
            state2[1] = state2[0];
            state2[0] = new int[]{v, i, j};
        } else if (v < state2[1][0]) {
            state2[1] = new int[]{v, i, j};
        }
    }

    @Test
    public void test_minFallingPathSum() {
        int[][] grid = InputUtil.parseIntGrid("[[1,2,3],[4,5,6],[7,8,9]]");
        Assert.assertEquals(13, minFallingPathSum(grid));

        int[][] grid2 = InputUtil.parseIntGrid("[[2,2,1,2,2],[2,2,1,2,2],[2,2,1,2,2],[2,2,1,2,2],[2,2,1,2,2]]");
        Assert.assertEquals(7, minFallingPathSum(grid2));

    }

    @Test
    public void test_minFallingPathSum2() {
        int[][] grid = InputUtil.parseIntGrid("[" +
                "[-37,51,-36,34,-22]," +
                "[82,4,30,14,38]," +
                "[-68,-52,-92,65,-85]," +
                "[-49,-3,-77,8,-19]," +
                "[-60,-71,-21,-62,-73]" +
                "]");
        Assert.assertEquals(-268, minFallingPathSum(grid));

    }

}
