package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PalindromePartitionTest2 {

    /**
     * 1 <= k <= s.length <= 100
     * s 中只含有小写英文字母。
     * <p>
     * 首先，你可以将 s 中的部分字符修改为其他的小写英文字母。
     * 接着，你需要把 s 分割成 k 个非空且不相交的子串，并且每个子串都是回文串。
     * 请返回以这种方式分割字符串所需修改的最少字符数。
     */
    public int palindromePartition(String str, int k) {
        int len = str.length();
        int[][] costCache = new int[len + 1][len + 1];
        for (int i = 1; i <= len; i++) {
            for (int j = i + 1; j <= len; j++) {
                costCache[i][j] = cost(str, i, j);
            }
        }
        // dp[i][j] 前i个字符切割成k个部分时最小的修改字符数,i0<i
        // 状态转移方程 dp[i][j]=min(dp[i0][j-1]+cost(i0+1,j)),j-1 =< i0 < i
        int[][] dp = new int[str.length() + 1][k + 1];
        for (int i = 1; i < dp.length; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        dp[0][0] = 0;

        for (int i = 1; i <= len; i++) {
            int len2 = Math.min(i, k);
            dp[i][1] = costCache[1][i];
            for (int j = 2; j <= len2; j++) {
                for (int i0 = j - 1; i0 < i; i0++) {
                    System.out.printf("dp[%s][%s]=%s,dp[%s][%s]=%s,cost(%s,%s)=%s,str=%s,%s\n"
                            , i, j, dp[i][j], i0, j - 1, dp[i0][j - 1], i0 + 1, i, costCache[i0 + 1][i], str.substring(0, i0), str.substring(i0, i));
//                    dp[i][j] = Math.min(dp[i][j], dp[i0][j - 1] + cost(str, i0 + 1, i));
                    dp[i][j] = Math.min(dp[i][j], dp[i0][j - 1] + costCache[i0 + 1][i]);
                }
                System.out.printf("------(%02d,%02d)=%s\n", i, j, dp[i][j]);
            }
        }
        return dp[str.length()][k];
    }

    private int cost(String str, int start, int end) {
        int num = 0;
        for (int i = start - 1, j = end - 1; i < j; i++, j--) {
            if (str.charAt(i) != str.charAt(j)) {
                num++;
            }
        }
//        System.out.printf("cost(%02d,%02d)=%s,%s\n", start, end, num, str.substring(start - 1, end));
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
