package cn.zyj.tunnel.leetcode;

import cn.zyj.tunnel.utils.InputUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.zyj.tunnel.leetcode.FindElementsTest.TreeNode;

@Slf4j
@RunWith(JUnit4.class)
public class KEmptySlotsTest {

    public int kEmptySlots(int[] bulbs, int k) {
        // day-fid
        // fid->day
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < bulbs.length; i++) {
            int b = bulbs[i];
            for (int b0 : new int[]{b - (k + 1), b + (k + 1)}) {
                if (b0 < 0) {
                    continue;
                }
                if (!map.containsKey(b0)) {
                    continue;
                }
                boolean flag = true;
                for (int j = Math.min(b, b0) + 1, h = Math.max(b, b0); j < h; j++) {
                    if (map.containsKey(j)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return i;
                }
            }
            map.put(b, i);
        }
        return -1;
    }

    @Test
    public void test() {
//        Assert.assertEquals(2, kEmptySlots(new int[]{1, 3, 2}, 1));
        Assert.assertEquals(-1, kEmptySlots(new int[]{1, 2, 3}, 1));
    }

    public String nextClosestTime(String time) {
        // 23:59
        int[] nums = new int[4];
        for (int i = 0, j = 0; i < nums.length; i++, j++) {
            if (j == 2) j++;
            nums[i] = time.charAt(j) - '0';
        }
        int hours = nums[0] * 10 + nums[1];
        int minus = nums[2] * 10 + nums[3];
        Arrays.sort(nums);
        if (minus < 59) {
            int minus2 = searchMinValue(nums, minus + 1, 60);
            if (minus2 != -1) {
                return String.format("%02d:%02d", hours, minus2);
            }
        }
        if (hours < 23) {
            int hours2 = searchMinValue(nums, hours + 1, 24);
            if (hours2 != -1) {
                int minus2 = searchMinValue(nums, 0, 60);
                return String.format("%02d:%02d", hours2, minus2);
            }
        }
        int hours2 = searchMinValue(nums, 0, 24);
        int minus2 = searchMinValue(nums, 0, 60);
        return String.format("%02d:%02d", hours2, minus2);
    }

    private int searchMinValue(int[] nums, int min, int max) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                int m = nums[i] * 10 + nums[j];
                if (m >= min && m < max) {
                    return m;
                }
            }
        }
        return -1;
    }

    /*
    输入: "19:34" 输出: "19:39"
    输入: "23:59" 输出: "22:22"
     */
    @Test
    public void test2() {
        Assert.assertEquals("19:39", nextClosestTime("19:34"));
        Assert.assertEquals("22:22", nextClosestTime("23:59"));
    }

    public int longestUnivaluePath(TreeNode root) {
        Map<Integer, Integer> map = new HashMap<>();
        checkNextNode(root, map);
        return map.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getValue).orElse(0);
    }

    private void checkNextNode(TreeNode root, Map<Integer, Integer> map) {
        if (root == null) return;
        map.put(root.val, map.getOrDefault(root.val, -1) + 1);
        checkNextNode(root.left, map);
        checkNextNode(root.right, map);
    }

    public String multiply(String num1, String num2) {
        if ("0".equals(num1) || "0".equals(num2)) return "0";
        int width = 4;
        int[] arr1 = parseBigInt(num1, width);
        int[] arr2 = parseBigInt(num2, width);
        int[] arr = multiBigInt(arr1, arr2, width);
        return bigIntToStr(arr, width);
    }

    private String[] zeros = {"", "0", "00", "000", "0000"};

    private String bigIntToStr(int[] arr, int width) {
        String str = "";
        for (int i = arr.length - 1; i >= 0; i--) {
            String s = arr[i] + "";
            if (str.isEmpty()) {
                if ("0".equals(s)) continue;
            } else {
                str += zeros[width - s.length()];
            }
            str += s;
        }
        if (str.isEmpty()) {
            str = "0";
        }
        return str;
    }

    private int[] multiBigInt(int[] arr1, int[] arr2, int width) {
        int w = (int) Math.pow(10, width);
        int[] arr = new int[arr1.length + arr2.length];
        for (int i = 0; i < arr1.length; i++) {
            int v1 = arr1[i];
            for (int j = 0; j < arr2.length; j++) {
                int v2 = arr2[j];
                int k = i + j;
                int v = arr[k] + v1 * v2;
                arr[k] = v % w;
                arr[k + 1] += v / w;

            }
        }
        return arr;
    }

    private int[] parseBigInt(String num, int width) {
        int len = (int) Math.ceil((double) num.length() / width);
        int[] arr = new int[len];
        for (int i = 0, j = num.length(); i < len; i++) {
            int k = Math.max(j - width, 0);
            String str = num.substring(k, j);
            arr[i] = Integer.valueOf(str);
            j = k;
        }
        return arr;
    }

    @Test
    public void test3() {
        log.info(Arrays.toString(parseBigInt("18", 4)));
        log.info(Arrays.toString(parseBigInt("12345678", 4)));
        log.info(Arrays.toString(parseBigInt("1234567", 4)));

        log.info("6 -- " + multiply("2", "3"));
        log.info("56088 -- " + multiply("123", "456"));
        log.info(12345 * 123 + " -- " + multiply("12345", "123"));
    }

    public String reverseWords(String s) {
        StringBuilder sb = new StringBuilder();
        String[] strs = s.split(" +");
        for (int i = strs.length - 1; i >= 0; i--) {
            String str = strs[i];
            if (str.isEmpty()) continue;
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(str);
        }
        return sb.toString();
    }

    @Test
    public void test4() {
        log.info("[" + reverseWords("  hello world!  ") + "]");
    }

    public String simplifyPath(String path) {
        Deque<String> paths = new LinkedList<>();
        for (int i = 1, j = 1; true; j++, i = j) {
            if (j >= path.length()) break;
            j = path.indexOf('/', j);
            if (i == j) continue;
            if (j == -1) j = path.length();
            // 1,2
            String str = path.substring(i, j);
//            System.out.println(str);
            if (str.equals(".")) {

            } else if (str.equals("..")) {
                if (!paths.isEmpty()) {
                    paths.removeLast();
                }
            } else {
                paths.addLast(str);
            }
        }
        return "/" + String.join("/", paths);
    }

    @Test
    public void test5() {
        log.info("[" + simplifyPath("/../") + "]");
        log.info("[" + simplifyPath("/home//foo/") + "]");
        log.info("[" + simplifyPath("/a/./b/../../c/") + "]");
        log.info("[" + simplifyPath("/a//b////c/d//././/..") + "]");
    }

    /*
    给定一个只包含数字的字符串，复原它并返回所有可能的 IP 地址格式。
    输入: "25525511135"
    输出: ["255.255.11.135", "255.255.111.35"]
    */
    public List<String> restoreIpAddresses(String s) {
        return helpRestoreIpAddresses(s, 0, 4, null);
    }

    // s分成n份
    private List<String> helpRestoreIpAddresses(String s, int start, int n, String ipPrefix) {
        if (n < 1) {
            if (start < s.length()) return Collections.EMPTY_LIST;
            return Collections.singletonList(ipPrefix);
        }
        int len = s.length() - start;
        if (len > 3 * n || len < n) return Collections.EMPTY_LIST;

        List<String> results = new LinkedList<>();
        int num = 0;
        int end = Math.min(start + 3, s.length());
        for (int i = start; i < end; i++) {
            if (i > start && num == 0) break; // 最高位为0，只能是个位数
            num = num * 10 + (s.charAt(i) - '0');
            if (num > 255) break;
            String nextIp = (ipPrefix == null ? "" : (ipPrefix + ".")) + num;
            results.addAll(helpRestoreIpAddresses(s, i + 1, n - 1, nextIp));
        }
        return results;
    }


    @Test
    public void test6() {
        log.info("" + restoreIpAddresses("25525511135"));
        log.info("" + restoreIpAddresses("010010"));
    }

    /**
     * 要注意输入类型，别想当然!!!
     *
     * @param matrix
     * @return
     */
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;
        int n = matrix[0].length;
        if (n == 0) return 0;
        // dp[i][j]表示以(i,j)为右下角的正方形的最大边长
        // dp[i][j]=1+min(dp[i-1][j-1],dp[i-1][j],dp[i][j-1])
        int[][] dp = new int[m][n];
        int max = 0;
        for (int i = 0; i < m; i++) {
            max = Math.max(max, dp[i][0] = matrix[i][0] - '0');
        }
        for (int j = 0; j < n; j++) {
            max = Math.max(max, dp[0][j] = matrix[0][j] - '0');
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] != '1') continue;
                max = Math.max(max, dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j - 1], dp[i - 1][j]), dp[i][j - 1]));
            }
        }
        return max * max;
    }

    @Test
    public void test7() {
//        1 0 1 0 0
//        1 0 1 1 1
//        1 1 1 1 1
//        1 0 0 1 0
        char[][] mat = {
                {'1', '0', '1', '0', '0' },
                {'1', '0', '1', '1', '1' },
                {'1', '1', '1', '1', '1' },
                {'1', '0', '0', '1', '0' },
        };
        log.info("" + maximalSquare(mat));

        char[][] mat2 = {
                {'1', '0', '1', '0', '0' },
                {'1', '1', '1', '1', '0' },
                {'1', '0', '1', '0', '1' },
                {'1', '1', '1', '1', '1' },
                {'1', '0', '1', '1', '1' },
                {'1', '1', '1', '1', '1' },
                {'1', '0', '0', '1', '0' },
        };
        log.info("" + maximalSquare(mat2));
    }

    //    输入: [-2,1,-3,4,-1,2,1,-5,4],
//    输出: 6
//    解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
    public int maxSubArray(int[] nums) {
        if (nums.length == 0) return 0;
        // 最右侧为第i个字符的连续子数组的最大和
        int[] dp = new int[nums.length];
        int max = dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    @Test
    public void test8() {
        Assert.assertEquals(6, maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        Assert.assertEquals(1, maxSubArray(new int[]{1}));
        Assert.assertEquals(-1, maxSubArray(new int[]{-1}));
    }

    /*
    给定一个三角形，找出自顶向下的最小路径和。每一步只能移动到下一行中相邻的结点上。
    例如，给定三角形：
    [
         [2],
        [3,4],
       [6,5,7],
      [4,1,8,3]
    ]
    自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。
    如果你可以只使用 O(n) 的额外空间（n 为三角形的总行数）来解决这个问题，那么你的算法会很加分。
    */
    public int minimumTotal0(List<List<Integer>> triangle) {
        Queue<int[]> queue = new LinkedList<>(); // n=triangle.size(), 最多使用n的空间
        queue.offer(new int[]{0, 0, 0}); // 层,横向位置，累计路径
        int minVal = Integer.MAX_VALUE;
        int[] state;
        while ((state = queue.poll()) != null) {
            state[2] += triangle.get(state[0]).get(state[1]);
//            System.out.println(Arrays.toString(state));
            if (state[0] >= triangle.size() - 1) {
                minVal = Math.min(minVal, state[2]);
                continue;
            }
            queue.offer(new int[]{state[0] + 1, state[1], state[2]});
            queue.offer(new int[]{state[0] + 1, state[1] + 1, state[2]});
        }
        return minVal;
    }

    public int minimumTotal1(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = triangle.get(0).get(0);
        for (int i = 1; i < n; i++) {
            int left = Integer.MAX_VALUE;
            for (int j = 0; j <= i; j++) {
                int dpj_1 = left;
                left = dp[j];
                dp[j] = Math.min(dpj_1, dp[j]) + triangle.get(i).get(j);
            }
        }
        int minVal = Integer.MAX_VALUE;
        for (int val : dp) minVal = Math.min(minVal, val);
        return minVal;
    }

    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        if (n == 0) return 0;
        /*
        逆向思考：路径是双向的，可以从底向上作dp，这样就减少了特殊情况
        视三角形为n+1层，第n+1层的代价全是0，这样就不影响结果
         */
        int[] dp = new int[n + 1];
        ListIterator<List<Integer>> listIt = triangle.listIterator(n);
        while (listIt.hasPrevious()) {
            ListIterator<Integer> it = listIt.previous().listIterator(0);
            while (it.hasNext()) {
                int j = it.nextIndex();
                dp[j] = Math.min(dp[j], dp[j + 1]) + it.next();
            }
        }
        return dp[0];
    }

    @Test
    public void test9() {
        List<List<Integer>> input = Arrays.asList(
                Arrays.asList(2),
                Arrays.asList(3, 4),
                Arrays.asList(6, 5, 7),
                Arrays.asList(4, 1, 8, 3)
        );
        Assert.assertEquals(11, minimumTotal(input));
        List<List<Integer>> input2 = Arrays.asList(
                Arrays.asList(-1),
                Arrays.asList(-2, -3)
        );
        Assert.assertEquals(-4, minimumTotal(input2));

    }

    /*
    给定一些标记了宽度和高度的信封，宽度和高度以整数对形式 (w, h) 出现。当另一个信封的宽度和高度都比这个信封大的时候，
    这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
    请计算最多能有多少个信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）。
    说明: 不允许旋转信封。
     */
    public int maxEnvelopes(int[][] envelopes) {
        if (envelopes.length == 0) return 0;
        int[] wh = envelopes[0];
        for (int i = 0; i < envelopes.length; i++) {

        }
        return -1;
    }

    @Test
    public void test10() {
//        输入: envelopes = [[5,4],[6,4],[6,7],[2,3]]
//        输出: 3
//        解释: 最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
//        Assert.assertEquals(3, maxEnvelopes(new int[][]{{5, 4}, {6, 4}, {6, 7}, {2, 3}}));

        int[][] input2 = InputUtil.parseIntGrid("[[2,100],[3,200],[4,300],[5,500],[5,400],[5,250],[6,370],[6,360],[7,380]]");
        Assert.assertEquals(5, maxEnvelopes(input2));

    }

}
