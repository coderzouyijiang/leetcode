package cn.zyj.tunnel.leetcode;

import cn.zyj.tunnel.utils.InputUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate169_4 {

    /*
    给你一个方程，左边用 words 表示，右边用 result 表示。
    你需要根据以下规则检查方程是否可解：
    每个字符都会被解码成一位数字（0 - 9）。
    每对不同的字符必须映射到不同的数字。
    每个 words[i] 和 result 都会被解码成一个没有前导零的数字。
    左侧数字之和（words）等于右侧数字（result）。
    如果方程可解，返回 True，否则返回 False。
     */
    /*
    输入：words = ["SEND","MORE"], result = "MONEY"
    输出：true
    解释：映射 'S'-> 9, 'E'->5, 'N'->6, 'D'->7, 'M'->1, 'O'->0, 'R'->8, 'Y'->'2'
    所以 "SEND" + "MORE" = "MONEY" ,  9567 + 1085 = 10652
     */
    /*
    2 <= words.length <= 5
    1 <= words[i].length, results.length <= 7
    words[i], result 只含有大写英文字母
    表达式中使用的不同字符数最大为 10
     */
    public boolean isSolvable(String[] words, String result) {
        int maxLen = 0;
        for (String word : words) {
            maxLen = Math.max(maxLen, word.length());
        }
        if (maxLen > result.length()) {
            return false;
        }
        // (char-'A') -> number(0-9);
        int[] chMap = new int[26]; // 字符索引->字符代表的数字
        Arrays.fill(chMap, -1);
        boolean[] usedVal = new boolean[10];
        int chNum = 10; // 剩余字符数
        int preVal = 0;
        int start = 1;
        return dfs(words, result, chMap, usedVal, chNum, start, preVal);
    }

    private boolean dfs(String[] words, String result, int[] chMap, boolean[] usedVal, int chNum, int start, int preVal) {
        int len = result.length();
        for (int i = start; i <= len; i++) {
            int sum = preVal;
            for (String word : words) {
                final int j = word.length() - i;
                final int val;
                if (j < 0) {
                    val = 0;
                } else {
                    final int chIndex = word.charAt(j) - 'A';
                    val = chMap[chIndex];
                    if (val == -1) {
                        if (chNum == 0) {
                            // 已经有10个字符映射关系了
                            return false;
                        } else {
                            // 找到一个映射关系 todo
                            return findNextMapping(words, result, chMap, usedVal, chNum, i, preVal, chIndex);
                        }
                    }
                }
                sum += val;
            }
            int chIndex = result.charAt(result.length() - i) - 'A';
            int res = chMap[chIndex];
            if (res == -1) {
                if (chNum == 0) {
                    // 已经有10个字符映射关系了
                    return false;
                } else {
                    // 找到一个映射关系
                    return findNextMapping(words, result, chMap, usedVal, chNum, i, preVal, chIndex);
                }
            }
            if (sum % 10 != res) {
                return false;
            }
            preVal = sum / 10;
        }
        return true;
    }

    private boolean findNextMapping(String[] words, String result, int[] chMap, boolean[] usedVal, int chNum, int i, int preVal, int chIndex) {
        for (int num = 0; num < usedVal.length; num++) {
            if (usedVal[num]) continue;
            boolean[] usedVal2 = Arrays.copyOf(usedVal, usedVal.length);
            usedVal2[num] = true;
            int[] chMap2 = Arrays.copyOf(chMap, chMap.length);
            chMap2[chIndex] = num;
            if (dfs(words, result, chMap2, usedVal2, chNum + 1, i, preVal)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void test() {
//        boolean result = isSolvable(new String[]{"SEND", "MORE"}, "MONEY");
//        Assert.assertEquals(true, result);

        String[] words;
        String str;
        words = new String[]{"JACAH", "IIJI", "GACG"};
        str = "DDJF";
        Assert.assertEquals(false, isSolvable(words, str));
    }

}
