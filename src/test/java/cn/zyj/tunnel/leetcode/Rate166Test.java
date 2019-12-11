package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RunWith(JUnit4.class)
public class Rate166Test {

    public List<List<Integer>> groupThePeople(int[] groupSizes) {

        List<List<Integer>> result = new LinkedList<>();
        // 组的人数-不足的组
        Map<Integer, List<Integer>> queueMap = new LinkedHashMap<>();
        for (int id = 0; id < groupSizes.length; id++) {
            int size = groupSizes[id];
            List<Integer> list = queueMap.computeIfAbsent(size, it -> new ArrayList<>(size));
            list.add(id);
            if (list.size() == size) {
                queueMap.remove(size);
                result.add(list);
            }
        }
        return result;
    }

    @Test
    public void test1() {
//        输入：groupSizes = [2,1,3,3,3,2]
//        输出：[[1],[0,5],[2,3,4]]
//        List<List<Integer>> result = groupThePeople(new int[]{2, 1, 3, 3, 3, 2});
        List<List<Integer>> result = groupThePeople(new int[]{3, 3, 3, 3, 3, 1, 3});
        log.info("" + result);
    }

    /*
    1 <= nums.length <= 5 * 10^4
    1 <= nums[i] <= 10^6
    nums.length <= threshold <= 10^6
     */
    public int smallestDivisor(int[] nums, int threshold) {
        // nums[i]=ai*x+bi
//        sum(Math.ceil((double)nums[i]/x))=sum(ceil(ai))
        int i = 1;
        int j = max(nums);
//        int vi = compute(nums, i);
//        int vj = compute(nums, j);
        while (j - i > 1) {
            int mid = i + (j - i) / 2;
            int v = compute(nums, mid);
//            System.out.println("(" + vi + "," + vj + ")[" + i + "," + j + "],mid=" + mid + ",v=" + v + ",threshold=" + threshold);
            if (threshold < v) {
                i = mid;
//                vi = v;
            } else {
                j = mid;
//                vj = v;
            }
        }
//        System.out.println("(" + vi + "," + vj + ")[" + i + "," + j + "],threshold=" + threshold);
        for (int k = i; k < j; k++) {
            if (compute(nums, k) <= threshold) {
                return k;
            }
        }
        return j;
    }

    public int compute(int[] nums, int x) {
        int sum = 0;
        for (int num : nums) {
            sum += Math.ceil((double) num / x);
        }
        return sum;
    }

    public int max(int[] nums) {
        int max = 0;
        for (int num : nums) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    @Test
    public void test2() {
//    输入：nums = [1,2,5,9], threshold = 6
//    输出：5
//    解释：如果除数为 1 ，我们可以得到和为 17 （1+2+5+9）。
//    如果除数为 4 ，我们可以得到和为 7 (1+1+2+3) 。如果除数为 5 ，和为 5 (1+1+1+2)。
        Assert.assertEquals(5, smallestDivisor(new int[]{1, 2, 5, 9}, 6));
    }

    @Test
    public void test3() {
        //  [2,3,5,7,11]
        //        11
//        Assert.assertEquals(3, smallestDivisor(new int[]{2, 3, 5, 7, 11}, 11));
//        [1,2,3]
//        6
        Assert.assertEquals(1, smallestDivisor(new int[]{1, 2, 3}, 6));
    }

//    private static final int[] di = {0, 1, 1, 0, 0};
//    private static final int[] dj = {0, 1, 0, 1, 0};

    public int minFlips0(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        int startVal = 0;
        int len = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] > 0) {
                    startVal |= (1 << len);
                }
                len++;
            }
        }
        int[] masks = computeMasks(m, n);
//        boolean[] passed = new boolean[1 << len];
        Set<Integer> closeSet = new HashSet<>();
//        PriorityQueue<int[]> openSet = new PriorityQueue<>(Comparator.comparingInt(it -> it.length));
        LinkedList<int[]> openSet = new LinkedList<>();
        openSet.offer(new int[]{startVal, 0});
        int[] state;
        loop1:
        while ((state = openSet.poll()) != null) {
            final int v0 = state[0];
            if (v0 == 0) {
//                System.out.println(Arrays.toString(state));
                /*
                String[] strs = new String[state.length];
                for (int i = 0; i < state.length; i++) {
                    strs[i] = bitGridToStr(state[i], m, n);
                }
                System.out.println(concatMutiLine(" -> ", strs));
                return (state.length - 1);
                */
                return state[1];
            }
            /*
            System.out.println("state:" + v0 + ",step=" + (state.length - 1));
            System.out.println(bitGridToStr(v0, m, n));
            */
            closeSet.add(v0);
//            passed[v0] = true;
            loop2:
            // 所有可能的点
            for (int mask : masks) {
                int v = v0 ^ mask;
                if (closeSet.contains(v)) continue loop2;
                /*
                System.out.println(concatMutiLine("   ", bitGridToStr(v0, m, n)
                        , bitGridToStr(mask, m, n), bitGridToStr(v, m, n)));
                int[] nextState = Arrays.copyOf(state, state.length + 1);
                nextState[state.length] = v;
                */
                openSet.offer(new int[]{v, state[1] + 1});
            }
        }
        return -1;
    }

    public static int[] computeMasks(int m, int n) {
        int[] dxs = {0, -1, 0, 1, 0};
        int[] dys = {0, 0, -1, 0, 1};
        int[] masks = new int[m * n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int mask = 0;
                for (int k = 0; k < dxs.length; k++) {
                    int i2 = i + dxs[k];
                    int j2 = j + dys[k];
//                    System.out.printf("(%s,%s)->(%s,%s):%s\n", i, j, i2, j2, (i2 * n + j2));
                    if (i2 >= 0 && i2 < m && j2 >= 0 && j2 < n) {
                        int bitIndex = i2 * n + j2;
                        mask |= (1 << bitIndex);
                    }
                }
//                System.out.printf("(%s,%s):mask[%s]=%s\n", i, j, i * n + j, mask);
//                System.out.println(bitGridToStr(mask, m, n));
                masks[i * n + j] = mask;
            }
        }
        return masks;
    }

    public int minFlips(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        int startVal = 0;
        int len = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] > 0) {
                    startVal |= (1 << len);
                }
                len++;
            }
        }
        int[] masks = computeMasks(m, n);
        boolean[] passed = new boolean[1 << len];
        LinkedList<int[]> openSet = new LinkedList<>();
        openSet.offer(new int[]{startVal, 0});
        int[] state;
        loop1:
        while ((state = openSet.poll()) != null) {
            final int v0 = state[0];
            if (v0 == 0) {
                return state[1];
            }
            passed[v0] = true;
            loop2:
            // 所有可能的点
            for (int mask : masks) {
                int v = v0 ^ mask;
                if (passed[v]) continue loop2;
                openSet.offer(new int[]{v, state[1] + 1});
            }
        }
        return -1;
    }

    public static String bitGridToStr(int mask, int m, int n) {
        String maskStr = String.format("%" + m * n + "s", Integer.toBinaryString(mask)).replace(' ', '0');
        maskStr = new StringBuffer(maskStr).reverse().toString();
        String str = "";
        for (int i = 0; i < m; i++) {
            str += maskStr.substring(i * n, (i + 1) * n) + "\n";
        }
        return str;
    }

    public static String concatMutiLine(String delimer, String... strs) {
        String result = "";
        List<String[]> linesList = Stream.of(strs).map(it -> it.split("\n")).collect(Collectors.toList());
        Integer maxLen = linesList.stream().map(it -> it.length).max(Integer::compare).orElse(0);
        for (int i = 0; i < maxLen; i++) {
            for (String[] lines : linesList) {
                result += (i < lines.length ? lines[i] : "") + delimer;
            }
            result += "\n";
        }
        return result;
    }

    @Test
    public void test4() {
        int[][] mat;
//        [[0,0],[0,1]]
//        3
//        mat = new int[][]{{0, 0}, {0, 1}};
//        Assert.assertEquals(3, minFlips(mat));
//        输入：mat = [[1,1,1],[1,0,1],[0,0,0]]
//        输出：6
        mat = new int[][]{{1, 1, 1}, {1, 0, 1}, {0, 0, 0}};
        Assert.assertEquals(6, minFlips(mat));
    }

    @Test
    public void test5() {
//        computeMasks(3, 3);
        computeMasks(2, 2);
    }


}
