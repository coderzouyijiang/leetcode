package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class Test34_maxSubArray {

    // 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
    class Solution {
        public int maxSubArray(int[] nums) {
            if (nums.length == 0) return 0;
            // dp[i],右端为i的子数组的最大和
            int[] dp = new int[nums.length];
            int result = dp[0] = nums[0];
            for (int i = 1; i < nums.length; i++) {
                dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
                result = Math.max(result, dp[i]);
            }
            return result;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(6, solution.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }
}
