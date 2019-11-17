package cn.zyj.tunnel.leetcode;

public class ValidCharPair {

    private String s;

    public ValidCharPair(String s) {
        this.s = s;
    }

    private static boolean isLeft(char ch) {
        return ch == '(' || ch == '[' || ch == '{';
    }

    private static char right2Left(char ch) {
        if (ch == ')') return '(';
        else if (ch == ']') return '[';
        else if (ch == '}') return '{';
        else return 0;
    }

    public boolean isValid() {
        if (s.isEmpty()) return true;
        if (s.length() % 2 != 0) return false;
//        LinkedList<Character> stack = new LinkedList<>();
        SimpleCharStack stack = new SimpleCharStack();
        for (int i = 0; i < s.length(); i++) {
            final char ch = s.charAt(i);
            final char expectLeft = right2Left(ch);
            if (expectLeft != 0) { // ch in )]}
                if (stack.isEmpty()) {
                    return false;
                }
                final char left = stack.pop();
                if (left != expectLeft) {
                    return false;
                }
            } else if (isLeft(ch)) { // ch in ([{
                stack.push(ch);
            } else { // 不是有效符号
                return false;
            }
        }
        return stack.isEmpty();
    }

}
