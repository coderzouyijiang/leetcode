package cn.zyj.tunnel.leetcode;

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate168_2 {

    //    给你一个整数数组 nums 和一个正整数 k，请你判断是否可以把这个数组划分成一些由 k 个连续数字组成的集合。
//    如果可以，请返回 True；否则，返回 False。
    public boolean isPossibleDivide0(int[] nums, int k) {
        int m = nums.length;
        if (m % k != 0) return false;
        Map<Integer, Integer> numCount = new HashMap<>();
        List<Integer> numList = new ArrayList<>();
        for (int num : nums) {
            Integer count = numCount.getOrDefault(num, 0);
            numCount.put(num, count + 1);
            if (count == 0) {
                numList.add(num);
            }
        }
        log.info("{}", numCount);
        Collections.sort(numList);
        for (int num : numList) {
//            Integer needCount = numCount.getOrDefault(num, 0);
            Integer needCount = numCount.remove(num);
            if (needCount == null) continue;
            log.info("needCount:{}", needCount);
            for (int i = 1; i < k; i++) {
                int nextNum = num + 1;
                Integer count = numCount.getOrDefault(nextNum, 0);
                if (count < needCount) return false;
                if (count > needCount) {
                    numCount.put(nextNum, count - needCount);
                } else {
                    numCount.remove(nextNum);
                }
            }
            log.info("{}:{}", num, numCount);
        }
        return numCount.isEmpty();
    }

    // 1 <= nums[i] <= 10^9
    public boolean isPossibleDivide1(int[] nums, int k) {
        int m = nums.length;
        if (m % k != 0) return false;
        Arrays.sort(nums);
        log.info("nums:{}", nums);
        boolean[] passed = new boolean[m + 1];
        int curNum = nums[0];
        int curSize = 0;
        int start = -1;
        int start2 = -1;
        int startSize = 0;
        int startIndex = -1;
        for (int i = 0; i <= m; i++) {
            if (passed[i]) continue;
            int num = i < m ? nums[i] : -1;
            log.info("num={}", num);
            if (num != curNum) {
                log.info("curNum={},curSize={}", curNum, curSize);
                if (start == -1) {
                    start = curNum;
                    startSize = curSize;
                    for (int j = i - startSize; j < startSize; j++) {
                        passed[j] = true;
                    }
                    start2 = start + 1;
                    startIndex = i;
                } else if (start2 < start + k) {
                    if (curNum != start2) {
                        return false;
                    }
                    if (curSize < startSize) {
                        return false;
                    }
                    for (int j = i - startSize; j < startSize; j++) {
                        passed[j] = true;
                    }
                    start2++;
                } else if (start2 == start + k) {
                    i = startIndex;
                    start = -1;
                    curNum = -1;
                    continue;
                } else {
                    return false;
                }
                log.info("start={},start2={},startSize={},startIndex={}", start, start2, startSize, startIndex);
                log.info("passed:{}", Arrays.toString(passed));
                curNum = num;
                curSize = 1;
            } else {
                curSize++;
            }
        }
        return true;
    }

    public boolean isPossibleDivide(int[] nums, int k) {
        int m = nums.length;
        if (m % k != 0) return false;
        Arrays.sort(nums);
        List<Integer> numList = new LinkedList<>();
        for (int num : nums) numList.add(num);
        log.info("numList:{}", numList);
        while (!numList.isEmpty()) {
            Integer start = numList.remove(0);
            int start2 = start + 1;
            int len = 1;
            Iterator<Integer> it = numList.iterator();
            while (len < k && it.hasNext()) {
                Integer num = it.next();
                if (num < start2) continue;
                if (num > start2) return false;
                it.remove();
                start2++;
                len++;
            }
            log.info("numList:{}", numList);
            if (len < k) return false;
        }
        return true;
    }

    @Test
    public void test() {
        Assert.assertTrue(isPossibleDivide(new int[]{3, 2, 1, 2, 3, 4, 3, 4, 5, 9, 10, 11}, 3));
        log.info("");
    }

}
