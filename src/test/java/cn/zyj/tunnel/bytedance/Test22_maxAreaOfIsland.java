package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class Test22_maxAreaOfIsland {

    class Solution {

        public int maxAreaOfIsland(int[][] grid) {
            int maxArea = 0;
            for (int i = 0; i < grid.length; i++) {
                int[] line = grid[i];
                for (int j = 0; j < line.length; j++) {
                    maxArea = Math.max(bfs(i, j, grid), maxArea);
                }
            }
            return maxArea;
        }

        private final int[] di = {0, 1, 0, -1};
        private final int[] dj = {1, 0, -1, 0};

        private int bfs(int i, int j, int[][] grid) {
            if (grid[i][j] == 0) return 0;
            grid[i][j] = 0;
            int num = 1;
            for (int k = 0; k < di.length; k++) {
                int i2 = i + di[k];
                if (i2 < 0 || i2 >= grid.length) continue;
                int j2 = j + dj[k];
                if (j2 < 0 || j2 >= grid[0].length) continue;
                num += bfs(i2, j2, grid);
            }
            return num;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        log.info("");
    }
}
