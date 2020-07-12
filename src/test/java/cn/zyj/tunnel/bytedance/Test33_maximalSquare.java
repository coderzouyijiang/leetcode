package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@Slf4j
@RunWith(JUnit4.class)
public class Test33_maximalSquare {

    class Solution {
        public int maximalSquare(char[][] matrix) {
            final int W = matrix.length;
            if (W == 0) return 0;
            final int H = matrix[0].length;
            if (H == 0) return 0;
            // dp[i][j]:以(i,j)为右下角的最大正方形的边长
            int[][] dp = new int[W][H];
            int maxLen = 0;
            for (int i = 0; i < W; i++) {
                if ((dp[i][0] = matrix[i][0] - '0') == 1) {
                    maxLen = 1;
                }
            }
            for (int j = 0; j < H; j++) {
                if ((dp[0][j] = matrix[0][j] - '0') == 1) {
                    maxLen = 1;
                }
            }
            for (int i = 1; i < W; i++) {
                for (int j = 1; j < H; j++) {
                    if (matrix[i][j] != '1') continue;
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                    maxLen = Math.max(maxLen, dp[i][j]);
                }
            }
            return maxLen * maxLen;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
    }
}
