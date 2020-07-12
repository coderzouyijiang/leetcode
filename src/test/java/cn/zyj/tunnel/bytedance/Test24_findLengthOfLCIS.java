package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@Slf4j
@RunWith(JUnit4.class)
public class Test24_findLengthOfLCIS {

    /*
    给定一个未经排序的整数数组，找到最长且连续的的递增序列，并返回该序列的长度。

    输入: [1,3,5,4,7]
    输出: 3
    解释: 最长连续递增序列是 [1,3,5], 长度为3。
    尽管 [1,3,5,7] 也是升序的子序列, 但它不是连续的，因为5和7在原数组里被4隔开。
     */
    class Solution {
        public int findLengthOfLCIS(int[] nums) {
            if (nums.length <= 1) return nums.length;
            int maxLength = 0;
            int preDiff = 0;
            int length = 0;
            for (int i = 1; i < nums.length; i++) {
                // 连续n个相同大于0的差值，连续序列长度=n+1
                int diff = nums[i] - nums[i - 1];
                if (diff > 0 && preDiff > 0) {
                    length++;
                } else {
                    preDiff = diff;
                    maxLength = Math.max(length, maxLength);
                    length = diff > 0 ? 1 : 0;
                }
            }
            maxLength = Math.max(length, maxLength);
            return maxLength + 1;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(3, solution.findLengthOfLCIS(new int[]{1, 3, 5, 4, 7}));
        Assert.assertEquals(1, solution.findLengthOfLCIS(new int[]{2, 2, 2, 2, 2}));
        Assert.assertEquals(4, solution.findLengthOfLCIS(new int[]{1, 3, 5, 7}));
        Assert.assertEquals(4, solution.findLengthOfLCIS(new int[]{1, 2, 5, 8, 0}));
    }
}
