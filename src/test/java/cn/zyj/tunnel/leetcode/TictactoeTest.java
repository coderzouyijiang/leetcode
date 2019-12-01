package cn.zyj.tunnel.leetcode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

@Slf4j
@RunWith(JUnit4.class)
public class TictactoeTest {

    private static final int[] dx = {1, -1, 0, 0, 1, -1, -1, 1};
    private static final int[] dy = {0, 0, 1, -1, 1, -1, 1, -1};

    private static final int max = 2;
    private static final int min = 0;

    /**
     * 第一个玩家 A 总是用 "X" 作为棋子，而第二个玩家 B 总是用 "O" 作为棋子。
     * <p>
     * 1 <= moves.length <= 9
     * moves[i].length == 2
     * 0 <= moves[i][j] <= 2
     * moves 里没有重复的元素。
     * moves 遵循井字棋的规则。
     * <p>
     * 如果游戏存在获胜者（A 或 B），就返回该游戏的获胜者；如果游戏以平局结束，则返回 "Draw"；
     * 如果仍会有行动（游戏未结束），则返回 "Pending"。
     */
    public String tictactoe(int[][] moves) {
        // 直线 y=ax+b
        // 可能的直线6条: [(0,0),(1,1)],[(0,0),(0,1)],[(0,0),(1,0)]
        //               [(0,1),(1,0)],[(0,1),(1,1)],[(1,0),(1,1)]
        char[][] grid = new char[3][3];
        for (int i = 0; i < moves.length; i++) {
            char v = (i % 2 == 0) ? 'X' : 'O';
            String role = v == 'X' ? "A" : "B";
            int x = moves[i][0];
            int y = moves[i][1];
            grid[x][y] = v;

            for (int k = 0; k < dx.length; k++) {
                int dx1 = dx[k];
                int dy1 = dy[k];
                if ((isWin(grid, v, x, y, dx1, dy1, -1) && isWin(grid, v, x, y, dx1, dy1, 1))
                        || (isWin(grid, v, x, y, dx1, dy1, -1) && isWin(grid, v, x, y, dx1, dy1, -2))
                        || (isWin(grid, v, x, y, dx1, dy1, 1) && isWin(grid, v, x, y, dx1, dy1, 2))) {
                    return role;
                }
            }
        }
        if (moves.length < 9) {
            return "Pending";
        }
        return "Draw";
    }

    private boolean isWin(char[][] grid, char v, int x, int y, int dx, int dy, int n1) {
        int x1 = x + n1 * dx;
        int y1 = y + n1 * dy;
        return x1 >= min && x1 <= max && y1 >= min && y1 <= max && grid[x1][y1] == v;
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
        String input = "[[0,0],[1,1],[2,0],[1,0],[1,2],[2,1],[0,1],[0,2],[2,2]]";
        Assert.assertEquals("Draw", tictactoe(parseInput(input)));

        String input2 = "[[0,0],[1,1],[0,1],[0,2],[1,0],[2,0]]";
        Assert.assertEquals("B", tictactoe(parseInput(input2)));

        String input3 = "[[0,0],[2,0],[1,1],[2,1],[2,2]]";
        Assert.assertEquals("A", tictactoe(parseInput(input3)));

        String input4 = "[[0,0],[1,1]]";
        Assert.assertEquals("Pending", tictactoe(parseInput(input4)));

    }
}
