package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PalindromePartitionTest {

    /**
     * 1 <= k <= s.length <= 100
     * s 中只含有小写英文字母。
     * <p>
     * 首先，你可以将 s 中的部分字符修改为其他的小写英文字母。
     * 接着，你需要把 s 分割成 k 个非空且不相交的子串，并且每个子串都是回文串。
     * 请返回以这种方式分割字符串所需修改的最少字符数。
     */
    public int palindromePartition(String str, int k) {
        // 极限情况
        // str="abc...xyz",k=1;return 13
        // str="aaa...aaa",k=任意数;return 0
        // 风格方式数量fg=((k-1)!)/3
        Map<String, Integer> cache = new HashMap<>();
        if (k == 1) {
            return changeToPalindromeStepNum(str, cache);
        }
        return palindromePartition0(str, k - 1, cache);
    }

    // 先来一遍暴力算法
    public int palindromePartition0(String str, int k, Map<String, Integer> cache) {
        String key = str + "-" + k;
        Integer num0 = cache.get(key);
        if (num0 != null) {
            return num0;
        }
        int num = Integer.MAX_VALUE;
        int len = str.length() - k;
        for (int i = 1; i <= len; i++) {
            String str1 = str.substring(0, i);
            String str2 = str.substring(i);
            int num1 = changeToPalindromeStepNum(str1, cache);
            int num2;
            if (k == str2.length()) {
                num2 = 0;
            } else if (k > 1) {
                num2 = palindromePartition0(str2, k - 1, cache);
            } else {
                num2 = changeToPalindromeStepNum(str2, cache);
            }
            num = Math.min(num, num1 + num2);
        }
        cache.put(key, num);
        System.out.printf("%03d,%s,%s\n", cache.size(), key, num);
        return num;
    }

    private int changeToPalindromeStepNum(String str, Map<String, Integer> cache) {
        String key = str + "-" + 0;
        Integer num0 = cache.get(key);
        if (num0 != null) {
            return num0;
        }
        int lastIndex = str.length() - 1;
        int halfLen = str.length() / 2;
        int num = 0;
        for (int i = 0; i < halfLen; i++) {
            if (str.charAt(i) != str.charAt(lastIndex - i)) {
                num++;
            }
        }
        cache.put(key, num);
        System.out.printf("%03d,%s,%s\n", cache.size(), key, num);
        return num;
    }

    @Test
    public void test() {
        Assert.assertEquals(6, palindromePartition("spsvmwkvwyfnrrfklevvyxsayc", 6));
        System.out.println();
        Assert.assertEquals(4, palindromePartition("oiwwhqjkb", 1));
        System.out.println();
        Assert.assertEquals(0, palindromePartition("ihhyviwv", 7));
        System.out.println();
        Assert.assertEquals(1, palindromePartition("abc", 2));
        System.out.println();
        Assert.assertEquals(0, palindromePartition("aabbc", 3));
        System.out.println();
        Assert.assertEquals(0, palindromePartition("leetcode", 8));
        System.out.println();
    }
}
