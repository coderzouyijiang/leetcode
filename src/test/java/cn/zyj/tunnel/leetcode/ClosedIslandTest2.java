package cn.zyj.tunnel.leetcode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@RunWith(JUnit4.class)
public class ClosedIslandTest2 {

    private int m;
    private int n;
    private int[] dx = {1, -1, 0, 0};
    private int[] dy = {0, 0, 1, -1};

    public int closedIsland(int[][] grid) {
        m = grid.length;
        n = grid[0].length;
        // i*n+j
//        BitSet passed = new BitSet(m * n);
        boolean[][] passed = createPassed();
        int count = 0;
        int m2 = m - 1, n2 = n - 1;
        for (int i = 1; i < m2; i++) {
            for (int j = 1; j < n2; j++) {
                int count1 = dfs(grid, i, j, passed);
                if (count1 > 0) {
                    count++;
                }
                if (count1 != 0) {
                    printPassed(passed, i, j, count1, count);
                }
            }
        }
        return count;
    }

    private void printPassed(boolean[][] passed, int i, int j, int count1, int count) {
        System.out.printf("(%s,%s):count1:%s,count:%s\n", j, i, count1, count);
        String str = Stream.of(passed).map(line -> Arrays.toString(line)
                .replaceAll("true", "0")
                .replaceAll("false", "1"))
                .collect(Collectors.joining("\n"));
        System.out.println(str);
        System.out.println();
    }

    private boolean[][] createPassed() {
        boolean[][] passed = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            passed[i] = new boolean[n];
        }
        return passed;
    }

    public int dfs(int[][] grid, int i, int j, boolean[][] passed) {
        if (i < 0 || j < 0 || i >= m || j >= n) { // 越界的
            return -1;
        }
        if (grid[i][j] == 1) { // 水域
            return 0;
        }
        if (passed[i][j]) { // 探索过
            return 0;
        }
        passed[i][j] = true;

        int count = 1;
        int isNegative = 1;
        for (int k = 0; k < dx.length; k++) {
            int count1 = dfs(grid, i + dx[k], j + dy[k], passed);
            if (count1 < 0) {
                isNegative = -1;
                count -= count1;
            } else {
                count += count1;
            }
        }
        return count * isNegative;
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

        /*
        String input = "[" +
                "[1,1,1,1,1,1,1,0]," +
                "[1,0,0,0,0,1,1,0]," +
                "[1,0,1,0,1,1,1,0]," +
                "[1,0,0,0,0,1,0,1]," +
                "[1,1,1,1,1,1,1,0]]";
        log.info("" + closedIsland(parseInput(input)));
        */

        String input2 = "[" +
                "[0,0,1,1,0,1,0,0,1,0]," +
                "[1,1,0,1,1,0,1,1,1,0]," +
                "[1,0,1,1,1,0,0,1,1,0]," +
                "[0,1,1,0,0,0,0,1,0,1]," +
                "[0,0,0,0,0,0,1,1,1,0]," +
                "[0,1,0,1,0,1,0,1,1,1]," +
                "[1,0,1,0,1,1,0,0,0,1]," +
                "[1,1,1,1,1,1,0,0,0,0]," +
                "[1,1,1,0,0,1,0,1,0,1]," +
                "[1,1,1,0,1,1,0,1,1,0]]";
        log.info("" + closedIsland(parseInput(input2)));


    }

}
