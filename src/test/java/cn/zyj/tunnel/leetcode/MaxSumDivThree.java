package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class MaxSumDivThree {

    /*
    1 <= nums.length <= 4 * 10^4
    1 <= nums[i] <= 10^4
    */
    public int maxSumDivThree(int[] nums) {
        int sum0 = 0;
        for (int num : nums) sum0 += num;
        if (sum0 % 3 == 0) return sum0;
        // 2^n-1 种组合
        Arrays.sort(nums);
//        List<Integer> sumList = new LinkedList<>();
        int len = 0;
        int[] sumArr = new int[1 << 8];
        sumArr[len++] = sum0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            int curLen = len;
            if (len == sumArr.length) {
                sumArr = Arrays.copyOf(sumArr, len * 2);
            }
            for (int j = 0; j < curLen; j++) {
                int sum = sumArr[j] - num;
                if (sum % 3 == 0) {
                    return sum;
                }
                sumArr[len++] = sum;
            }
        }
        return 0;
    }

    public static int sum(int[] nums) {
        int result = 0;
        for (int num : nums) result += num;
        return result;
    }

    public int maxSumDivThree1(int[] nums) {
        int sum0 = 0;
        for (int num : nums) sum0 += num;
        if (sum0 % 3 == 0) return sum0;
        // 2^n-1 种组合
        Arrays.sort(nums);
        return maxSumDivThree(nums, sum0, 0);
    }

    public int maxSumDivThree(int[] nums, int sum0, int index) {
        int sum = sum0 - nums[index];
        if (sum % 3 == 0) return sum;
        if (++index >= nums.length) return 0;
        int sum2 = maxSumDivThree(nums, sum0, index);
        if (sum2 > 0) return sum2;
        return maxSumDivThree(nums, sum, index);
    }

    @Test
    public void test() {
//        Assert.assertEquals(18, maxSumDivThree(new int[]{3, 6, 5, 1, 8}));
//        Assert.assertEquals(0, maxSumDivThree(new int[]{4}));
//        Assert.assertEquals(12, maxSumDivThree(new int[]{1, 2, 3, 4, 4}));
        Assert.assertEquals(15, maxSumDivThree(new int[]{2, 6, 2, 2, 7}));
    }

}
