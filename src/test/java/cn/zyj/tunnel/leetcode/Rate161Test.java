package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@Slf4j
@RunWith(JUnit4.class)
public class Rate161Test {

    public int minimumSwap(String s1, String s2) {
        // x,y总字符偶数个，能交换
        int swapNum = 0;
        int xNum1 = 0, yNum1 = 0, xNum2 = 0, yNum2 = 0;
        for (int i = 0; i < s1.length(); i++) {
            char ch1 = s1.charAt(i);
            char ch2 = s2.charAt(i);
            if (ch1 == ch2) continue;
            if (ch1 == 'x') {
                xNum1++;
                yNum2++;
            } else {
                yNum1++;
                xNum2++;
            }
        }
        if ((xNum1 + xNum2) % 2 != 0 || (yNum1 + yNum2) % 2 != 0) {
            return -1;
        }
        // xxyx  xyxx s1      xxy xxxy xyxy xyyy  xxyyyy xyxyxy
        // yyxy  yxyy s2      yyx yyyx xyyx xyxx  yyxxxx
        // s1[1]-s2[0],s1[3]-s2[3],s1[3]-s2[2] => xyyx
        // s1[2]-s2[3],s1[0]-s2[0],s1[0]-s2[1] => xyyx
//        System.out.printf("xNum1=%s,yNum1=%s,xNum2=%s,yNum2=%s\n", xNum1, yNum1, xNum2, yNum2);
        return xNum1 / 2 + xNum1 % 2 + yNum1 / 2 + yNum1 % 2;
    }

    @Test
    public void test1() {
        String s1 = "xxyyxyxyxx";
        String s2 = "xyyxyxxxyx";
        // xyxyy
        // yxyxx
        Assert.assertEquals(4, minimumSwap(s1, s2));
        s1 = "xy";
        s2 = "yx";
        Assert.assertEquals(2, minimumSwap(s1, s2));
    }

    /*
    给你一个整数数组 nums 和一个整数 k。
    如果某个 连续 子数组中恰好有 k 个奇数数字，我们就认为这个子数组是「优美子数组」。
    请返回这个数组中「优美子数组」的数目。
     */
    public int numberOfSubarrays(int[] nums, int k) {
        // dp[i][j] 子数组[i,j) 的奇数个数
        // i=0,    dp[0][k]
        // i>0,    dp[i][k]=dp[i-1][k]-nums[i]%2
        // j>i+k,  dp[i][j]=dp[i][j-1]+nums[j]%2
        // i:0->length-k
        // j:
        int len = nums.length - k + 1;
        int[][] dp = new int[len][nums.length];
        for (int j = 0; j < k; j++) {
            dp[0][k] += nums[j] % 2;
        }
        int count = (dp[0][k] == k) ? 1 : 0;
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                dp[i][0] = dp[i - 1][0] - nums[i] % 2;
            }
            for (int j = 0; j < len; j++) {
                System.out.printf("[%s,%s]:dp[%s][%s]=%s,nums[%s]=%s\n", i, j, i, j - 1, dp[i][j - 1], j + k, nums[j + k]);
                dp[i][j] = dp[i][j - 1] + nums[j + k] % 2;
                count += (dp[i][j] == k) ? 1 : 0;
            }
        }
        return count;
    }

    /*
    示例 1：
    输入：nums = [1,1,2,1,1], k = 3
    输出：2
    解释：包含 3 个奇数的子数组是 [1,1,2,1] 和 [1,2,1,1] 。
    示例 2：
    输入：nums = [2,4,6], k = 1
    输出：0
    解释：数列中不包含任何奇数，所以不存在优美子数组。
    示例 3：
    输入：nums = [2,2,2,1,2,2,1,2,2,2], k = 2
    输出：16
     */
    @Test
    public void test2() {
        int[] arr;
        arr = new int[]{1, 1, 2, 1, 1};
        Assert.assertEquals(2, numberOfSubarrays(arr, 3));
        arr = new int[]{2, 4, 6};
        Assert.assertEquals(0, numberOfSubarrays(arr, 1));
        arr = new int[]{2, 2, 2, 1, 2, 2, 1, 2, 2, 2};
        Assert.assertEquals(16, numberOfSubarrays(arr, 2));

    }
}
