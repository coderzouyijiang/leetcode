package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class 二维网格迁移 {

    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        if (grid.length == 0) {
            return Collections.EMPTY_LIST;
        }
        final int m = grid.length;
        final int n = grid[0].length;
        final int len = m * n;
        k = k % len;

        List<List<Integer>> result = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            final List<Integer> line = new ArrayList<>(n);
            for (int j = 0; j < n; j++) {
                line.add(Integer.MIN_VALUE);
            }
            result.add(line);
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                final int index = ((i * n + j) + k) % len;
                final int i2 = index / n;
                final int j2 = index % n;
                result.get(i2).set(j2, grid[i][j]);
            }
        }

        return result;
    }

    @Test
    public void test() {
//        输入：grid = [[1,2,3],[4,5,6],[7,8,9]], k = 9
//        输出：[[1,2,3],[4,5,6],[7,8,9]]
        final int[][] input = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        log.info("{}", shiftGrid(input, 9));
        log.info("{}", shiftGrid(input, 2));
        log.info("{}", shiftGrid(input, 3));
    }

}
