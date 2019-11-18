package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

@RunWith(JUnit4.class)
public class 最长有效括号 {

    public static boolean isLeft(char ch) {
        return ch == '(' || ch == '[' || ch == '{';
    }

    public static boolean isRight(char ch) {
        return ch == ')' || ch == ']' || ch == '}';
    }

    public static char right2Left(char i) {
        if (i == ')') return '(';
        else if (i == ']') return '[';
        else if (i == '}') return '{';
        else return 0;
    }

    public int longestValidParentheses(String s) {
//        LinkedList<Character> stack = new LinkedList<>();
        SimpleCharStack stack = new SimpleCharStack();
        int maxLen = 0;
        int curLen = 0;
        for (int i = 0; i < s.length(); i++) {
            final char ch = s.charAt(i);
            final char expectLeft = right2Left(ch);
            if (expectLeft != 0) { // ch in )]}
                if (stack.isEmpty() || stack.pop() != expectLeft) {
//                    return false;
                    maxLen = Math.max(maxLen, curLen - stack.size() * 2);
                    curLen = 0;
                    stack.clear();
                    continue; // 跳过
                }
                if (stack.isEmpty()) {

                }
                curLen += 2;
            } else if (isLeft(ch)) { // ch in ([{
                stack.push(ch);
            } else { // 不是有效符号
                return -1;
            }
        }
//        return stack.isEmpty();
        return Math.max(maxLen, curLen - stack.size() * 2);
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
