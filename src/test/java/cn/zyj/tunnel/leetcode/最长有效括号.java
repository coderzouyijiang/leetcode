package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class 最长有效括号 {

    public int longestValidParentheses(String s) {
        while (true) {
            final int index = ValidCharPair.firstUnvalidIndexLeft(s);
            if (index == -1) {
                return s.length();
            }
            final String s1 = s.substring(0, index).rev
            if (index >= s.length()) {
                break;
            }
            final String s2 = s.substring(index + 1);
        }
        return -1;
    }

    public static class SimpleCharStack {

        private char[] chs = new char[128];
        private int pos = 0; // 指向下一个空白的位置

        public void reallocate() {
            chs = Arrays.copyOf(chs, chs.length * 2);
        }

        public void push(char ch) {
            if (pos >= chs.length) {
                reallocate();
            }
            chs[pos++] = ch;
        }

        public char pop() {
            return chs[--pos];
        }

        public boolean isEmpty() {
            return pos <= 0;
        }

        public int size() {
            return pos;
        }

        public void clear() {
            pos = 0;
        }

        @Override
        public String toString() {
            return "SimpleCharStack" + Arrays.toString(Arrays.copyOfRange(chs, 0, pos));
        }
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
