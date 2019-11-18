package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class 最长有效括号 {

    public int longestValidParentheses(String s) {
        String str = s;
        int maxLen = 0;
        while (true) {
            final int index = ValidCharPair.firstUnvalidIndexLeft(str);
            if (index == -1) {
                return str.length();
            }
            final String s1 = str.substring(0, index);
            final int index1 = ValidCharPair.firstUnvalidIndexRight(s1);
            maxLen = Math.max(maxLen, index - (index1 + 1));
            if (index >= str.length()) {
                break;
            }
            str = str.substring(index + 1);
        }
        return maxLen;
    }

    @Test
    public void test() {
        Assert.assertEquals(2, longestValidParentheses("()(()"));
        /*
        Assert.assertEquals(2, longestValidParentheses("()"));
        Assert.assertEquals(4, longestValidParentheses("()[]"));
        Assert.assertEquals(2, longestValidParentheses("(]{}"));
        Assert.assertEquals(6, longestValidParentheses("()[]{}"));
        Assert.assertEquals(6, longestValidParentheses(")([]){}"));
        Assert.assertEquals(4, longestValidParentheses(")(]{[]})"));
        */
        Assert.assertEquals(0, longestValidParentheses("({[}])"));
        Assert.assertEquals(4, longestValidParentheses("()[]({[}])()"));
        Assert.assertEquals(10, longestValidParentheses("))))())()()(())()(()"));
    }


}
