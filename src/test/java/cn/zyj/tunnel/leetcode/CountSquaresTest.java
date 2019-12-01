package cn.zyj.tunnel.leetcode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@Slf4j
@RunWith(JUnit4.class)
public class CountSquaresTest {

    public int countSquares(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int maxWidth = Math.min(m, n);
        int count = 0;
        for (int w = 1; w <= maxWidth; w++) {
            int[] arr = new int[n]; // 每一小列是否全为1 [(i,j),(i+w-1,j)]
            Arrays.fill(arr, -1);
            int m2 = m - (w - 1);
            for (int i = 0; i < m2; i++) { // 行
                int num = 0; // 累计连续有多少个小列全是1
                for (int j = 0; j < n; j++) { // 列
                    int i2 = i + w - 1;
                    System.out.printf("----(%s,%s)->(%s,%s),%s,%s\n", i, j, i2, j, arr[j], matrix[i2][j]);
                    boolean isAllOne = false;
                    if (i2 - arr[j] >= w) {
                        arr[j] = getLastZeroIndex(matrix, arr[j] + 1, i2, j); // 重新检查整个小列
                        isAllOne = i2 - arr[j] >= w;
                    }
                    if (isAllOne) {
                        num++;
                    } else {
                        num = 0; // 重新累计
                    }
                    if (num == w) { // 累计到w个小列都=1
                        num--; // 找同行的下一个正方形
                        count++; // 找到了正方形
                        System.out.printf("%02d,w=%s,i=%s,j=%s\n", count, w, i, j); // 右上角
                    }
                }
            }
        }
        return count;
    }

    private int getLastZeroIndex(int[][] matrix, int start, int end, int j) {
        for (int i = end; i >= start; i--) {
            if (matrix[i][j] == 0) {
                return i;
            }
        }
        return start - 1;
    }

    private int[][] parseInput(String input) {
        JSONArray arr = JSONObject.parseArray(input);
        int[][] grid = new int[arr.size()][];
        for (int i = 0; i < arr.size(); i++) {
            JSONArray arr2 = arr.getJSONArray(i);
            int[] line = new int[arr2.size()];
            for (int j = 0; j < arr2.size(); j++) {
                line[j] = arr2.getIntValue(j);
            }
            grid[i] = line;
        }
        return grid;
    }

    @Test
    public void test() {

        String input1 = "[\n" +
                "  [0,1,1,1],\n" +
                "  [1,1,1,1],\n" +
                "  [0,1,1,1]\n" +
                "]";
        Assert.assertEquals(15, countSquares(parseInput(input1)));
        System.out.println();
        String input2 = "[\n" +
                "  [1,0,1],\n" +
                "  [1,1,0],\n" +
                "  [1,1,0]\n" +
                "]";
        Assert.assertEquals(7, countSquares(parseInput(input2)));
        System.out.println();

        String input3 = "[" +
                "[0,1,1,1]," +
                "[1,1,0,1]," +
                "[1,1,1,1]," +
                "[1,0,1,0]]";
        Assert.assertEquals(13, countSquares(parseInput(input3)));
        String input4 = "[" +
                "[0,1,1,1]," +
                "[1,1,0,1]," +
                "[1,1,1,1]," +
                "[1,0,1,0]]";
        Assert.assertEquals(13, countSquares(parseInput(input4)));

    }

}
