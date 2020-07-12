package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class Test31_maxProfit {

    class Solution {
        public int maxProfit(int[] prices) {
            if (prices.length < 1) return 0;
            // 任意i<j，dest=price[j]-price[i]，取max(dest)
            int min = prices[0];
            int maxProfit = 0;
            for (int i = 1; i < prices.length; i++) {
                int price = prices[i];
                if (price < min) {
                    min = price;
                } else {
                    maxProfit = Math.max(price - min, maxProfit);
                }
            }
            return maxProfit;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(5, solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
    }
}
