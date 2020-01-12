package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedList;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate171_1 {

    /*
    输入：n = 2
    输出：[1,1]
    解释：A = 1, B = 1. A + B = n 并且 A 和 B 的十进制表示形式都不包含任何 0 。
     */
    public int[] getNoZeroIntegers(int n) {
        int a = 0;
        int b = 0;
        int w = 1;
        while (n != 0) {
            int v = n % 10;
            n = n / 10;
            if (v >= 2) {
                a += w;
                b += (v - 1) * w;
            } else if (n == 0) {
                // v=1
                b += w;
            } else {
                a += 2 * w;
                b += (10 + v - 2) * w;
                n -= 1;
            }
            System.out.printf("%s,%s,%s,%s\n", n, a, b, w);
            w *= 10;
        }
        System.out.printf("-----%s,%s\n", a, b);
        return new int[]{a, b};
    }

    @Test
    public void test() {
//        Assert.assertArrayEquals(new int[]{1, 1}, getNoZeroIntegers(2));
//        Assert.assertArrayEquals(new int[]{2, 8}, getNoZeroIntegers(10));
        Assert.assertArrayEquals(new int[]{2, 8}, getNoZeroIntegers(19));
    }

}
