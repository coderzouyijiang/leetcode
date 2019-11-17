package cn.zyj.tunnel.leetcode;

import java.util.Arrays;

public class SimpleCharStack {

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
