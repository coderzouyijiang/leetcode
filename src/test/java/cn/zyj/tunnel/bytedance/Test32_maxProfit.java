package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class Test32_maxProfit {

    class Solution {
        public int maxProfit(int[] prices) {
            if (prices.length < 1) return 0;
            // arr[i]:第i天卖出时的累计最大利润
            int[] arr = new int[prices.length];

            return 0;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(5, solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
    }
}
