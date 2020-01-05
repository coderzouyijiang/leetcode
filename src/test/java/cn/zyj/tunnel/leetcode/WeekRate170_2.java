package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate170_2 {

    /*
    输入：arr = [1,3,4,8], queries = [[0,1},{1,2},{0,3},{3,3]]
输出：[2,7,14,8]
解释：
数组中元素的二进制表示形式是：
1 = 0001
3 = 0011
4 = 0100
8 = 1000
查询的 XOR 值为：
[0,1] = 1 xor 3 = 2
[1,2] = 3 xor 4 = 7
[0,3] = 1 xor 3 xor 4 xor 8 = 14
[3,3] = 8
     */
    public int[] xorQueries(int[] arr, int[][] queries) {
        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int[] query = queries[i];
            int l = query[0];
            int r = query[1];
            int v = arr[l];
            for (int j = l + 1; j <= r; j++) {
                log.info(j + "," + arr[j] + "," + v + "," + (v ^ arr[j]));
                v ^= arr[j];
            }
            log.info("");
            result[i] = v;
        }
        return result;
    }

    @Test
    public void test() {
//        输入：s = "10#11#12"
//        输出：""
//        解释："j" -> "10#" , "k" -> "11#" , "a" -> "1" , "b" -> "2".

//        Assert.assertEquals("jkab", freqAlphabets("10#11#12"));
        xorQueries(new int[]{1, 3, 4, 8}, new int[][]{{0, 1}, {1, 2}, {0, 3}, {3, 3}});
    }

}
