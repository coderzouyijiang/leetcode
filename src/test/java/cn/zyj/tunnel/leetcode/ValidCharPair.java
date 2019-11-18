package cn.zyj.tunnel.leetcode;

public class ValidCharPair {

    public static boolean isLeft(char ch) {
        return ch == '(' || ch == '[' || ch == '{';
    }

    public static char right2Left(char ch) {
        if (ch == ')') return '(';
        else if (ch == ']') return '[';
        else if (ch == '}') return '{';
        else return 0;
    }

    /**
     * 第一个无效索引
     *
     * @param s
     * @return -1 不存在
     * [0,len) 无效索引值
     * len 末尾缺右括号
     */
    public static int firstUnvalidIndexLeft(String s) {
        if (s.isEmpty()) return -1;
//        LinkedList<Character> stack = new LinkedList<>();
        SimpleCharStack stack = new SimpleCharStack();
        for (int i = 0; i < s.length(); i++) {
            final char ch = s.charAt(i);
            final char expectLeft = right2Left(ch);
            if (expectLeft != 0) { // ch in )]}
                if (stack.isEmpty()) {
                    return i;
                }
                final char left = stack.pop();
                if (left != expectLeft) {
                    return i;
                }
            } else if (isLeft(ch)) { // ch in ([{
                stack.push(ch);
            } else { // 不是有效符号
                return i;
            }
        }
        return stack.isEmpty() ? -1 : s.length();
    }

    public static boolean isRight(char ch) {
        return ch == ')' || ch == ']' || ch == '}';
    }

    public static char left2Right(char ch) {
        if (ch == '(') return ')';
        else if (ch == '[') return ']';
        else if (ch == '{') return '}';
        else return 0;
    }

    public static int firstUnvalidIndexRight(String s) {
        if (s.isEmpty()) return -1;
//        LinkedList<Character> stack = new LinkedList<>();
        SimpleCharStack stack = new SimpleCharStack();
        for (int i = s.length() - 1; i >= 0; i--) {
            final char ch = s.charAt(i);
            final char expectRight = left2Right(ch);
            if (expectRight != 0) { // ch in )]}
                if (stack.isEmpty()) {
                    return i;
                }
                final char right = stack.pop();
                if (right != expectRight) {
                    return i;
                }
            } else if (isRight(ch)) { // ch in ([{
                stack.push(ch);
            } else { // 不是有效符号
                return i;
            }
        }
        return stack.isEmpty() ? -1 : s.length();
    }

}
