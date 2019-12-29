package cn.zyj.tunnel.leetcode;

import cn.zyj.tunnel.utils.InputUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate169_3 {

    private static final int[] dxs = {1, -1};

    public boolean canReach(int[] arr, int start) {

        boolean[] passed = new boolean[arr.length];

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);

        Integer index;
        while ((index = queue.poll()) != null) {
            if (passed[index]) continue;
            passed[index] = true;
            // 当你位于下标 i 处时，你可以跳到 i + arr[i] 或者 i - arr[i]。
            for (int dx : dxs) {
                int val = arr[index];
                if (val == 0) return true;
                int nextIndex = index + dx * val;
                if (nextIndex < 0 || nextIndex >= arr.length) continue;
                queue.offer(nextIndex);
            }
        }
        return false;
    }

    @Test
    public void test() {
        int[] arr = InputUtil.parseIntArray("[4,2,3,0,3,1,2]");
        Assert.assertEquals(true, canReach(arr, 5));
    }

}
