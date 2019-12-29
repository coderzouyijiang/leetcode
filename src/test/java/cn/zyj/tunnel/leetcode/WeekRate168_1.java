package cn.zyj.tunnel.leetcode;

import cn.zyj.tunnel.utils.InputUtil;
import jdk.internal.util.xml.impl.Input;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate168_1 {

    public int findNumbers(int[] nums) {
        int count = 0;
        for (int num : nums) {
            if ((num+"").length() % 2 == 0) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void test() {
        int[] arr = InputUtil.parseIntArray("[12,345,2,6,7896]");
        log.info("" + findNumbers(arr));
    }

}
