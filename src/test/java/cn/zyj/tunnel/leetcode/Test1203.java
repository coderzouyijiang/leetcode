package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@Slf4j
@RunWith(JUnit4.class)
public class Test1203 {

    private final int w10 = 10;
    private final int w16 = 1 << 16;
    private final int toUpper = 'A' - 'a';

    /**
     * 你有一个十进制数字，请按照此规则将它变成「十六进制魔术数字」：首先将它变成字母大写的十六进制字符串，
     * 然后将所有的数字 0 变成字母 O ，将数字 1  变成字母 I 。
     * 如果一个数字在转换后只包含 {"A", "B", "C", "D", "E", "F", "I", "O"} ，那么我们就认为这个转换是有效的。
     * 给你一个字符串 num ，它表示一个十进制数 N，如果它的十六进制魔术数字转换是有效的，请返回转换后的结果，否则返回 "ERROR" 。
     */
    public String toHexspeak0(String num) {
        int[] arr = new int[(int) Math.ceil(num.length() / width)];
        for (int j = num.length(), i = 0; j > 0; j -= width, i++) {
            Integer x = Integer.valueOf(num.substring(Math.max(0, j - width), j));
            System.out.println(x);

            for (int k = 0; x != 0; k++) {
                arr[k] += x % w16;
                x = x / w16;
            }
            System.out.println(Arrays.toString(arr));
        }
        return "";
    }

    private final byte w = 10;
    private final byte v = 1 << 4;

    private final int w4 = 10000;
    private final int v16 = 1 << 16;
    private final int width = 4;

    public String toHexspeak(String num) {
        // 每一个元素，用于保存16进制的位
        int[] bs = new int[num.length()];
        for (int i = num.length(), k = 0; i > 0; i -= width, k++) {
            // v=a*(10^w)
//            int a = (num.charAt(i) - '0');
            Integer a = Integer.valueOf(num.substring(Math.max(i - width, 0), i));
//            System.out.printf("%s,%s,%s\n", Math.max(i - width, 0), i, a);
            if (a == 0) continue;
            // a*w+b = b*v+c
//            int k = (int) Math.ceil(num.length() / width);
            int[] bs2 = ten2Hex(a, k, w4, v16);
            bs = add(bs, bs2, v16);
        }
        System.out.printf("bs=%s\n", Arrays.toString(bs));
//        System.out.println(str);
        return hexToStr(bs);
    }

    private String hexToStr(int[] bs) {
        final int mask = (1 << 4) - 1;
        String str = null;
        for (int i = bs.length - 1; i >= 0; i--) {
            if (str == null && bs[i] == 0) {
                continue;
            }
//            String hexStr = Integer.toHexString(bs[i]);
            for (int j = 0; j < 4; j++) {
                int b = (bs[i] >> ((3 - j) * 4)) & mask;
//                char b = j < hexStr.length() ? hexStr.charAt(j) : '0';
                if (str == null) {
                    if (b == 0) continue;
                    str = "";
                }
                if (b == 0) str += 'O';
                else if (b == 1) str += 'I';
                else if (b >= 10 && b <= 15) str += (char) (b - 10 + 'A');
                else return "ERROR";
            }
        }
        return str;
    }

    private int[] add(int[] bs1, int[] bs2, int v) {
        int len = Math.max(bs1.length, bs2.length);
        int[] bytes = new int[len];
        int b = 0;
        for (int i = 0; i < len; i++) {
            b = ((i < bs1.length ? bs1[i] : 0) + (i < bs2.length ? bs2[i] : 0) + b);
            bytes[i] = (b % v);
            b = (b / v);
        }
        if (b > 0) {
            bytes = Arrays.copyOf(bytes, len + 1);
            bytes[len] = b;
        }
        return bytes;
    }

    // a*w^k => b0*v0+b1*v1...
    private int[] ten2Hex(int a, int k, int w, int v) {
        int[] bytes = new int[Math.max(1, k)];
        bytes[0] = a;
        for (int j = 0; j < k; j++) {
            int b = 0;
            for (int h = 0; h < k; h++) {
                b = (bytes[h] * w + b);
                bytes[h] = (b % v);
                b = (b / v);
            }
            if (b > 0) {
                bytes = Arrays.copyOf(bytes, k + 1);
                bytes[k] = b;
            }
        }
//        System.out.printf("%s*%s^%s => (%s)%s\n", a, w, k, v, Arrays.toString(bytes));
        return bytes;
    }

    @Test
    public void test() {
        System.out.println(Integer.valueOf("C0B0A", 16));
//        log.info(toHexspeak("257"));
//        log.info(toHexspeak("619879596177"));
//        log.info(toHexspeak("26221644803"));
//        log.info(toHexspeak("747823223228"));
//        Assert.assertEquals("AEIDBCDIBC", toHexspeak("747823223228"));
        Assert.assertEquals("COBOA", toHexspeak("789258"));

    }

    class Solution {

        public final int width = 4;
        public final int w = (int) Math.pow(10, width);
        public final int v = 1 << (4 * width);

        public int[] ten2HexArr(String num) {

            int len = (int) Math.ceil((double) num.length() / width);
            int offset = width * len - num.length();

            int[] arr = new int[len];
            for (int start = 0, end = width - offset; end <= num.length(); end += width, start = end - width) {
                // 0,w;w,2w;2w
                Integer x = Integer.valueOf(num.substring(start, end));
                int b = x;
                for (int i = 0; i < len - 1; i++) {
                    b = arr[i] * w + b;
                    arr[i] = b % v;
                    b = b / v;
                }
                if (b > 0) {
                    arr[len - 1] += b;
                }
            }
            return arr;
        }

        private String hexArrToStr(int[] bs) {
            final int mask = (1 << 4) - 1;
            String str = null;
            for (int i = bs.length - 1; i >= 0; i--) {
                if (str == null && bs[i] == 0) {
                    continue;
                }
//            String hexStr = Integer.toHexString(bs[i]);
                for (int j = 0; j < 4; j++) {
                    int b = (bs[i] >> ((3 - j) * 4)) & mask;
//                char b = j < hexStr.length() ? hexStr.charAt(j) : '0';
                    if (str == null) {
                        if (b == 0) continue;
                        str = "";
                    }
                    if (b == 0) str += 'O';
                    else if (b == 1) str += 'I';
                    else if (b >= 10 && b <= 15) str += (char) (b - 10 + 'A');
                    else return "ERROR";
                }
            }
            return str;
        }

        public String toHexspeak(String num) {
            int[] hexArr = ten2HexArr(num);
            return hexArrToStr(hexArr);
        }
    }

    @Test
    public void test2() {
        Solution solution = new Solution();
        log.info("" + Arrays.toString(solution.ten2HexArr("3")));
        log.info("" + toHexspeak("789258"));
        log.info("" + Arrays.toString(solution.ten2HexArr("789258")));
        log.info("" + solution.toHexspeak("789258"));
        Assert.assertEquals("AEIDBCDIBC", solution.toHexspeak("747823223228"));
    }

}
