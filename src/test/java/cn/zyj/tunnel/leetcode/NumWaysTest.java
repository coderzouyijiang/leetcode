package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class NumWaysTest {

    /**
     * 有一个长度为 arrLen 的数组，开始有一个指针在索引 0 处。
     * 每一步操作中，你可以将指针向左或向右移动 1 步，或者停在原地（指针不能被移动到数组范围外）。
     * 给你两个整数 steps 和 arrLen ，请你计算并返回：在恰好执行 steps 次操作以后，指针仍然指向索引 0 处的方案数。
     * 由于答案可能会很大，请返回方案数 模 10^9 + 7 后的结果。
     */
    public int numWays(int steps, int arrLen) {
        Map<Integer, Integer> cache = new LinkedHashMap<>();
        int num = numWays(steps, arrLen, 0, cache);
        System.out.println("总计算次数:" + cache.size());
        AtomicInteger index = new AtomicInteger(0);
        List<String> lines = cache.entrySet().stream()
                .map(it -> String.format("%03d", index.getAndIncrement()) + ":(" + (it.getKey() >> 9) + "," + (it.getKey() & ((1 << 9) - 1)) + ")=" + it.getValue())
                .collect(Collectors.toList());
        lines.forEach(System.out::println);
        return num;
    }

    //                             21_4748_3647
    private static final int MOD = 10_0000_0007;

    /***
     1 <= steps <= 500
     1 <= arrLen <= 10^6
     递归次数=3^Steps
     **/
    public int numWays(int steps, int arrLen, int sum, Map<Integer, Integer> cache) {
//        cache.computeIfPresent(-2, (k, v) -> v + 1);
//        System.out.println(steps + "," + arrLen + "," + sum);
        if (sum >= arrLen || sum < 0) return 0; // 累计移动距离超过了上界
        if (sum > steps) return 0; // 剩余步数不足以回到起点
        if (sum == steps) return 1; // 刚好可以回到起点，只有一种可能性

        int key = steps | (sum << 9); // steps<=500 < 512=2^9。sum<=10^6 < 4*10^4 < 2^22=4194304
        Integer totalNum0 = cache.get(key); // 只有这2个值会变化
        if (totalNum0 != null) return totalNum0;

        int totalNum = numWays(steps - 1, arrLen, sum, cache); // 不动
        int num1 = numWays(steps - 1, arrLen, sum + 1, cache);// 往外走一步
        int num2 = numWays(steps - 1, arrLen, sum - 1, cache);// 往里走一步
        totalNum = (totalNum + num1) % MOD;
        totalNum = (totalNum + num2) % MOD;
        cache.put(key, totalNum);
        return totalNum;
    }

    public int numWays2(int steps, int arrLen) {
        if (arrLen == 1) return 0;
        int s = steps, l = arrLen - 1;
        int sum = 1;// 完全不动
        int x = steps / 2; // 左走步数
        for (int i = 1; i <= x; i++) {
            if (i == 1) {
                sum += (s * (s - 1) / 2) % MOD; // 总和取模=取模后相加
            } else if (i == 2) {
                for (int j = 0; j < s; j++) {

                }
            }
        }
        return -1;
    }

    @Test
    public void test() {
//        Assert.assertEquals(numWays(3, 2), 4);
//        Assert.assertEquals(numWays(2, 4), 2);
//        Assert.assertEquals(numWays(4, 2), 8);
//        Assert.assertEquals(numWays(4, 3), 9);
        Assert.assertEquals(numWays(27, 7), 127784505);
    }
}
