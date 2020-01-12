package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate171_2 {

    /*
    给你三个正整数 a、b 和 c。
    你可以对 a 和 b 的二进制表示进行位翻转操作，返回能够使按位或运算   a OR b == c  成立的最小翻转次数。
    「位翻转操作」是指将一个数的二进制表示任何单个位上的 1 变成 0 或者 0 变成 1 。
     */

    // 0 0 0 0
    // 1 0 0 1
    // 0 1 0 1
    // 1 1 0 2
    // 0 0 1 1
    // 1 0 1 0
    // 0 1 1 0
    // 1 1 1 0
    final int[] arr = {0, 1, 1, 2, 1, 0, 0, 0};

    public int minFlips(int a, int b, int c) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            int index = ((a >> i) & 1) + (((b >> i) & 1) << 1) + (((c >> i) & 1) << 2);
            count += arr[index];
        }
        return count;
    }

    @Test
    public void test() {
        System.out.println("" + minFlips(2, 6, 5)); // 3
        System.out.println("" + minFlips(4, 2, 7)); // 1
    }

}
