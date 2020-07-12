package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(JUnit4.class)
public class Test27_trap {

    class Solution1 {
        private void reverse(int[] height) {
            int temp;
            for (int i = 0, j = height.length - 1; i < j; i++, j--) {
                temp = height[i];
                height[i] = height[j];
                height[j] = temp;
            }
        }

    }

    class Solution {

        public int trap(int[] height) {
            if (height.length < 3) return 0;
            Integer[] ids = new Integer[height.length];
            boolean[] passed = new boolean[height.length];
            int maxHeight = 0;
            int usedArea = 0;
            for (int i = 0; i < height.length; i++) {
                ids[i] = i;
                passed[i] = false;
                usedArea += height[i];
                maxHeight = Math.max(height[i], maxHeight);
            }
            Arrays.sort(ids, Comparator.comparingInt(i -> -height[i]));
            Integer id0 = ids[0];
            int area = 0;
            int width = height.length;
            int last = ids.length - 1;
            for (int i = 0; i < last; ) {
                Integer id = ids[i];
                if (passed[id]) {
                    i++;
                    continue;
                }
                passed[id] = true;
                width--;
                int h = height[id];
                for (int k = i; k <= last; k++) {
                    Integer id2 = ids[k];
                    int h2 = height[id2];
                    if (h2 != h) break;
                    int a = Math.min(id0, id2);
                    int b = Math.max(id0, id2);
                    for (int j = a; j <= b; j++) {
                        if (passed[j]) continue;
                        passed[j] = true;
                        width--;
                    }
                }
                int h2 = 0;
                while (i < last) {
                    i++;
                    Integer id2 = ids[i];
                    if (!passed[id2]) {
                        h2 = height[id2];
                        break;
                    }
                }
                area += (h - h2) * width;
                log.info("i={},id={},h={},h2={},width={},area={}", i, id, h, h2, width, area);
            }
            int totalArea = maxHeight * height.length;
            return totalArea - usedArea - area;
        }
    }

    class Solution2 {
        public int trap(int[] height) {
            int min, max = 0;
            int l = 0, r = height.length - 1;
            int res = 0, index;
            log.info("l={},r={},max={},res={}", l, r, max, res);
            while (l < r) {
                index = height[l] < height[r] ? l++ : r--;
                min = height[index];
                max = Math.max(min, max);
                res += max - min;
                log.info("l={},r={},index={},min={},max={},res={}", l, r, index, min, max, res);
            }
            return res;
        }
    }

    @Test
    public void test() {
        Solution2 solution = new Solution2();
        Assert.assertEquals(6, solution.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
//        Assert.assertEquals(1, solution.trap(new int[]{4, 2, 3}));
//        Assert.assertEquals(83, solution.trap(new int[]{6, 4, 2, 0, 3, 2, 0, 3, 1, 4, 5, 3, 2, 7, 5, 3, 0, 1, 2, 1, 3, 4, 6, 8, 1, 3}));
    }
}
