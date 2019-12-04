package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static cn.zyj.tunnel.utils.InputUtil.parseIntGrid;

@Slf4j
@RunWith(JUnit4.class)
public class RemoveIntervalTest {

    public List<List<Integer>> removeInterval(int[][] intervals, int[] toBeRemoved) {
        Comparator<int[]> comparator = (o1, o2) -> {
            if (o1[1] < o2[0]) return -1;
            else if (o1[0] > o2[1]) return 1;
            else return 0;
        };
        int l0 = toBeRemoved[0];
        int r0 = toBeRemoved[1];
//        index of the search key, if it is contained in the array;
//        otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>
// index1=-(i+1), i+1=-index i=-index-1
        int index1 = Arrays.binarySearch(intervals, new int[]{l0, l0 + 1}, comparator);
        if (index1 < 0) index1 = -index1 - 1;
        int index2 = Arrays.binarySearch(intervals, index1, intervals.length, new int[]{r0, r0 + 1}, comparator);
        if (index2 < 0) index2 = -index2 - 1;
        /*
        int l1 = intervals[index1][0];
        int r1 = intervals[index1][1];
        int l2 = intervals[index2][0];
        int r2 = intervals[index2][1];
        */
        List<List<Integer>> result = new ArrayList<>(intervals.length);
        for (int i = 0; i < intervals.length; i++) {
            int[] interval = intervals[i];
            int l = interval[0];
            int r = interval[1];
            if (i < index1 || i > index2) {
                result.add(Arrays.asList(l, r));
            } else if (i == index1 && i == index2) {
                if (l < l0) {
                    result.add(Arrays.asList(l, Math.min(l0, r)));
                }
                if (r > r0) {
                    result.add(Arrays.asList(Math.max(r0, l), r));
                }
            } else if (i == index1 && l < l0) {
                result.add(Arrays.asList(l, Math.min(l0, r)));
            } else if (i == index2 && r > r0) {
                result.add(Arrays.asList(Math.max(r0, l), r));
            }
        }
        return result;
    }

    @Test
    public void test() {
        int[][] input1 = parseIntGrid("[[0,2],[3,4],[5,7]]"); // [[0,1],[6,7]]
        List<List<Integer>> result = removeInterval2(input1, new int[]{1, 6});
        log.info("" + result);
    }

    @Test
    public void test2() {
        int[][] input1 = parseIntGrid("[[0,5]]"); // [[0,2],[3,5]]
        List<List<Integer>> result = removeInterval2(input1, new int[]{2, 3});
        log.info("" + result);
    }

    public List<List<Integer>> removeInterval2(int[][] intervals, int[] toBeRemoved) {
        int l0 = toBeRemoved[0];
        int r0 = toBeRemoved[1];
        List<List<Integer>> result = new ArrayList<>(intervals.length);
        for (int i = 0; i < intervals.length; i++) {
            int l = intervals[i][0];
            int r = intervals[i][1];
            if (r <= l0 || l >= r0) {
                result.add(Arrays.asList(l, r));
                continue;
            }
            if (l0 > l && l0 < r) {
                result.add(Arrays.asList(l, l0));
            }
            if (r0 > l && r0 < r) {
                result.add(Arrays.asList(r0, r));
            }
        }
        return result;
    }
}
