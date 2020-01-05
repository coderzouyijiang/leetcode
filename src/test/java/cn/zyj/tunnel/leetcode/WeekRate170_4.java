package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate170_4 {

    /*
    输入：s = "zzazz"
    输出：0
    解释：字符串 "zzazz" 已经是回文串了，所以不需要做任何插入操作。
    示例 2：

    输入：s = "mbadm"
    输出：2
    解释：字符串可变为 "mbdadbm" 或者 "mdbabdm" 。
     */
    public int minInsertions(String s) {
        boolean[][] passed = new boolean[s.length()][s.length()];
        Queue<int[]> queue = new LinkedList<>();
        // leftIndex,rightIndex,0  ; leftIndex和rightIndex以外的字符串是回文串
        queue.offer(new int[]{0, s.length() - 1, 0});
        int[] state;
        while ((state = queue.poll()) != null) {
            log.info(Arrays.toString(state));
            int l = state[0], r = state[1], changeNum = state[2];
            if (passed[l][r]) continue;
            passed[l][r] = true;
            if (l >= r) return changeNum;
            while (l < r && s.charAt(l) == s.charAt(r)) {
                l++;
                r--;
            }
            if (l >= r) return changeNum;
            queue.offer(new int[]{l + 1, r, changeNum + 1});
            queue.offer(new int[]{l, r - 1, changeNum + 1});
        }
        return -1;
    }

    public int minInsertions2(String s) {
        if (isHw(s)) return 0;
        // i:回文字符串的中心
        for (int i = 0; i < s.length(); i++) {

        }
        return -1;
    }

    private boolean isHw(String s) {
        int mid = s.length() / 2;
        for (int i = 0, j = s.length() - 1; i < mid; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) return false;
        }
        return true;
    }

    @Test
    public void test() {
//        输入：s = "10#11#12"
//        输出：""
//        解释："j" -> "10#" , "k" -> "11#" , "a" -> "1" , "b" -> "2".

//        Assert.assertEquals(2, minInsertions("mbadm"));
//        Assert.assertEquals(0, minInsertions("zzazz"));
        Assert.assertEquals(0, minInsertions("tldjbqjdogipebqsohdypcxjqkrqltpgviqtqz"));
    }

}
