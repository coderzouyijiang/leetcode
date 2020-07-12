package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class Test26_merge {

    class Solution {
        public int[][] merge(int[][] intervals) {
            if (intervals.length <= 1) return intervals;
            Arrays.sort(intervals, Comparator.comparingInt(arr -> arr[0]));
            List<int[]> list = new LinkedList<>();
            int[] preArr = intervals[0];
            for (int i = 1; i < intervals.length; i++) {
                int[] arr = intervals[i];
                if (preArr[1] < arr[0]) {
                    list.add(preArr);
                    preArr = arr;
                } else {
                    preArr[1] = Math.max(preArr[1], arr[1]);
                }
            }
            list.add(preArr);
            int[][] result = new int[list.size()][];
            int i = 0;
            for (int[] arr : list) {
                result[i++] = arr;
            }
            return result;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(0, solution);
    }
}
