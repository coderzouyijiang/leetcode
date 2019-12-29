package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate169_1 {

    public int[] sumZero(int n) {
        int[] arr = new int[n];
        int size = n / 2;
        for (int i = 1; i <= size; i++) {
            arr[2 * i] = i;
            arr[2 * i + 1] = -i;
        }
        if (size * 2 < n) {
            arr[n - 1] = 0;
        }
        return arr;
    }

    @Test
    public void test() {
//        int[] arr = InputUtil.parseIntArray("[12,345,2,6,7896]");
//        log.info("" + sumZero(arr));
    }

}
