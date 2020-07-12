package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class Test2_longestCommonPrefix {

    class Solution {
        public String longestCommonPrefix(String[] strs) {
            if (strs == null || strs.length == 0) return "";
            if (strs.length == 1) return strs[0];
            String prefix = "";
            outLoop:
            for (int i = 0; true; i++) {
                Character firstCh = null;
                for (String str : strs) {
                    if (i >= str.length()) {
                        break outLoop;
                    }
                    char ch = str.charAt(i);
                    if (firstCh == null) {
                        firstCh = ch;
                    } else if (firstCh != ch) {
                        break outLoop;
                    }
                }
                prefix += firstCh;
            }
            return prefix;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals("fl", solution.longestCommonPrefix(new String[]{"flower", "flow", "flight"}));
        Assert.assertEquals("", solution.longestCommonPrefix(new String[]{"dog", "racecar", "car"}));
    }
}
