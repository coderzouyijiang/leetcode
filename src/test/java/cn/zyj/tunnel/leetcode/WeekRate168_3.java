package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate168_3 {

    /*
    给你一个字符串 s ，请你返回满足以下条件且出现次数最大的 任意 子串的出现次数：
    子串中不同字母的数目必须小于等于 maxLetters 。
    子串的长度必须大于等于 minSize 且小于等于 maxSize 。
     */
    public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
        int[][] dp = new int[s.length() + 1][];
        dp[0] = new int[26];
        for (int i = 1; i <= s.length(); i++) {
            int[] arr = Arrays.copyOf(dp[i - 1], 26);
            char ch = s.charAt(i - 1);
            arr[ch - 'a']++;
            dp[i] = arr;
        }
        int count = 0;
        Map<String, Integer> subCount = new HashMap<>();
        int i1 = s.length() - minSize;
        for (int i = 0; i <= i1; i++) {
            int[] arr1 = dp[i];
            int j1 = Math.min(i + maxSize, s.length());
            for (int j = i + minSize; j <= j1; j++) {
                int[] arr2 = dp[j];
//                log.info("{},{}:{}", i, j, Arrays.toString(arr1));
//                log.info("{},{}:{}", i, j, Arrays.toString(arr2));
                if (letterNum(arr1, arr2) <= maxLetters) {
//                    count++;
                    String subStr = s.substring(i, j);
                    int num = subCount.getOrDefault(subStr, 0) + 1;
//                    log.info("{}:{}", subStr, num);
                    subCount.put(subStr, num);
                    count = Math.max(count, num);
                }
            }
        }
        return count;
    }

    private int letterNum(int[] arr1, int[] arr2) {
        int count = 0;
        for (int i = 0; i < arr1.length; i++) {
            if (arr2[i] - arr1[i] > 0) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void test() {
        // ：s = "aababcaab", maxLetters = 2, minSize = 3, maxSize = 4
        Assert.assertEquals(2, maxFreq("aababcaab", 2, 3, 4));
        log.info("");
//        输入：s = "aabcabcab", maxLetters = 2, minSize = 2, maxSize = 3
//        输出：3
    }

    @Test
    public void test2() {
        // ：s = "aababcaab", maxLetters = 2, minSize = 3, maxSize = 4
        log.info("" + maxFreq("abcd", 2, 2, 2));
//        输入：s = "aabcabcab", maxLetters = 2, minSize = 2, maxSize = 3
//        输出：3
    }

}
