package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.Arrays;

@Slf4j
@RunWith(JUnit4.class)
public class Test4_multiply {

    /*
    num1 和 num2 的长度小于110。
    num1 和 num2 只包含数字 0-9。
    num1 和 num2 均不以零开头，除非是数字 0 本身。
    不能使用任何标准库的大数类型（比如 BigInteger）或直接将输入转换为整数来处理。
     */
    class Solution {
        public String multiply(String num1, String num2) {
            // int:32位，存8位十进制数,乘法参数4位
            // 110位，28个int,结果56个int
            final int radix = 10;
            final int W = 8; // 每个int存储十进制位数
            int[] ints1 = numStr2Ints(num1, W, radix);
            int[] ints2 = numStr2Ints(num2, W, radix);
            int[] ints = intsMultiply(ints1, ints2, W, radix);
            return ints2numStr(ints, W);
        }

        private int[] numStr2Ints(String numStr, final int W, final int radix) {
            final int L = (int) Math.ceil((double) numStr.length() / W); // 存储结果需要的int数
            int[] ints = new int[L];
            for (int i = 0, end1 = numStr.length(); end1 > 0; i++, end1 -= W) {
                ints[i] = Integer.parseInt(numStr.substring(Math.max(end1 - W, 0), end1), radix);
            }
            return ints;
        }

        private int[] intsMultiply(int[] ints1, int[] ints2, final int W, final int radix) {
            final int L = ints1.length + ints2.length; // 存储结果需要的int数
            final int M = (int) Math.pow(radix, W); // 每个int数存储的最大值
            int[] ints = new int[L];
            Arrays.fill(ints, 0);
            for (int i = 0; i < ints1.length; i++) {
                for (int j = 0; j < ints2.length; j++) {
                    int k = i + j; // 当前位
                    long v = ((long) ints1[i]) * ((long) ints2[j]); // 当前位数值乘法
                    log.info("计算前：ints1[{}]={},ints1[{}]={},k={},v={},ints={}", i, ints1[i], j, ints2[j], k, v, Arrays.toString(ints));
                    do {
                        v += ints[k];
                        ints[k] = (int) (v % M);
                        v = v / M;
                        k++;
                    } while (v != 0); // 需要进位
                    log.info("计算后：ints1[{}]={},ints1[{}]={},k={},v={},ints={}", i, ints1[i], j, ints2[j], k, v, Arrays.toString(ints));
                }
            }
            return ints;
        }

        private String ints2numStr(int[] ints, final int W) {
            String resultStr = "";
            boolean flag = false;
            for (int i = ints.length - 1; i >= 0; i--) {
                if (!flag) {
                    if (ints[i] == 0) continue;
                    resultStr += ints[i];
                    flag = true;
                } else {
                    resultStr += String.format("%0" + W + "d", ints[i]);
                }
            }
            return resultStr.isEmpty() ? "0" : resultStr;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals("6", solution.multiply("2", "3"));
        Assert.assertEquals("56088", solution.multiply("123", "456"));
        Assert.assertEquals("560880", solution.multiply("1230", "456"));
        Assert.assertEquals(new BigDecimal("123456789").multiply(new BigDecimal("98765")).toString(), solution.multiply("123456789", "98765"));
        Assert.assertEquals("24690", solution.multiply("12345", "2"));
        Assert.assertEquals(new BigDecimal("123456789").multiply(new BigDecimal("987654321")).toString(), solution.multiply("123456789", "987654321"));
        Assert.assertEquals(new BigDecimal("5297").multiply(new BigDecimal("6019530762")).toString(), solution.multiply("5297", "6019530762"));
    }
}
