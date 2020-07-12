package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@Slf4j
@RunWith(JUnit4.class)
public class Test28_findCircleNum {

    class Solution {
        public int findCircleNum(int[][] M) {
            final int width = M.length;
            if (width == 0) return 0;
            final int height = M[0].length;
            int num = 0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (bfs(i, j, M) > 0) {
                        num++;
                    }
                }
            }
            return num;
        }

        private int bfs(int i, int j, int[][] grid) {
            if (grid[i][j] == 0) return 0;
            grid[i][j] = 0;
            grid[j][i] = 0;
            int num = 1;
            int len = grid[i].length;
            for (int k = 0; k < len; k++) {
                num += bfs(i, k, grid);
            }
            len = grid.length;
            for (int k = 0; k < len; k++) {
                num += bfs(k, j, grid);
            }
            return num;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(0, solution);
    }
}
