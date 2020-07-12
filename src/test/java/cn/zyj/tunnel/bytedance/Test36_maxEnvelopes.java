package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Comparator;

@Slf4j
@RunWith(JUnit4.class)
public class Test36_maxEnvelopes {

    /*
    给定一些标记了宽度和高度的信封，宽度和高度以整数对形式 (w, h) 出现。当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
    请计算最多能有多少个信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）。
    说明:
    不允许旋转信封。
     */
    class Solution0 {
        public int maxEnvelopes(int[][] envelopes) {
            if (envelopes.length <= 1) return envelopes.length;
            int maxWidth = 0, maxHeight = 0;
            for (int[] envelope : envelopes) {
                maxWidth = Math.max(envelope[0], maxWidth);
                maxHeight = Math.max(envelope[1], maxHeight);
            }
            final int W = maxWidth + 1;
            final int H = maxHeight + 1;
            boolean[][] exist = new boolean[W][H];
            // dp[i][j]:以(i,j)里嵌套的最大信封数
            int[][] dp = new int[W][H];
            int maxNum = 0;
            for (int[] envelope : envelopes) {
                int i = envelope[0];
                int j = envelope[1];
                exist[i][j] = true;
                if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                    maxNum = 1;
                }
            }
            for (int i = 1; i < W; i++) {
                for (int j = 1; j < H; j++) {
                    if (!exist[i][j]) continue;
                    log.info("aaa ({},{}),({},{})={},({},{})={},({},{})={}", i, j, i, j, dp[i][j], i, j - 1, dp[i][j - 1], i - 1, j, dp[i - 1][j]);
                    dp[i][j] = Math.max(dp[i][j], Math.max(dp[i][j - 1], dp[i - 1][j])) + 1;
                    maxNum = Math.max(maxNum, dp[i][j]);
                    log.info("bbb ({},{}),({},{})={},({},{})={},({},{})={}", i, j, i, j, dp[i][j], i, j - 1, dp[i][j - 1], i - 1, j, dp[i - 1][j]);
                }
            }
            return maxNum;
        }
    }

    class Solution {
        public int maxEnvelopes(int[][] envelopes) {
            if (envelopes.length <= 1) return envelopes.length;
//            Arrays.sort(envelopes, (o1, o2) -> o1[0] < o2[0] ? o1[0] - o2[0] : o1[1] - o2[1]);
            Arrays.sort(envelopes, Comparator.comparingInt(arr -> arr[0] * 10000 + arr[1]));
            int[] cache = new int[envelopes.length];
            Arrays.fill(cache, -1);
            int num = dfs(0, 0, 0, envelopes, cache);
            return num - 1;
        }

        private int dfs(int w, int h, int begin, int[][] envelopes, int[] cache) {
            if (begin >= envelopes.length) return 0;
            int val = cache[begin];
            if (val != -1) return val;
            int num = 0;
            for (int j = begin; j < envelopes.length; j++) {
                int w2 = envelopes[j][0];
                int h2 = envelopes[j][1];
                if (w2 > w && h2 > h) {
                    num = Math.max(num, dfs(w2, h2, j, envelopes, cache));
                }
            }
            num++;
            cache[begin] = num;
            return num;
        }

    }

    @Test
    public void test() {
        Solution solution = new Solution();
        // [[5,4],[6,4],[6,7],[2,3]]
        Assert.assertEquals(3, solution.maxEnvelopes(new int[][]{
                        {5, 4},
                        {6, 4},
                        {6, 7},
                        {2, 3},
                }
        ));
    }
}
