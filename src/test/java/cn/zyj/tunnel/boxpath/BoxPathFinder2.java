package cn.zyj.tunnel.boxpath;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class BoxPathFinder2 {

    private static final char PLAYER = 'S';
    private static final char BOX = 'B';
    private static final char TARGET = 'T';
    private static final char WALL = '#';

    // 状态数组:0,1:box坐标；2,3:人的坐标；4:已走过步数；5:当前距离终点的估计距离
    private static final int bx = 0, by = 1, sx = 2, sy = 3, step = 4, dist = 5;
    // 下一步的方向
    private static final int[] dx = {0, 1, 0, -1}; // top,right,down,left
    private static final int[] dy = {1, 0, -1, 0};

    public static final Comparator<int[]> stateComparator = Comparator.comparingInt(state -> state[step] + state[dist]);

    public int minPushBox(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        final int[] startState = {-1, -1, -1, -1, 0, 0};
        int tx = -1, ty = -1; // 终点坐标

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char ch = grid[i][j];
                if (ch == 'S') {
                    startState[sx] = i;
                    startState[sy] = j;
                } else if (ch == 'B') {
                    startState[bx] = i;
                    startState[by] = j;
                } else if (ch == 'T') {
                    tx = i;
                    ty = j;
                }
            }
        }
        System.out.printf("startState:%s,target:(%s,%s)\n", Arrays.toString(startState), tx, ty);
        // 优先队列，每次从中获取【步数+预估剩余步数】最少的状态
        PriorityQueue<int[]> stateQueue = new PriorityQueue(stateComparator);
        stateQueue.add(startState);
        // 已经经过的状态值,只包含bx,by,sx,sy
        Set<int[]> oldStateSet = new HashSet<>();

        int[] curState;
        while ((curState = stateQueue.poll()) != null) {
            if (curState[bx] == tx && curState[by] == ty) {
                return step;
            }
            oldStateSet.add(Arrays.copyOfRange(curState, 0, 4));

            for (int i = 0; i < 4; i++) {
                int bx2 = curState[bx] + dx[i];   // 箱子新坐标
                int by2 = curState[by] + dy[i];
                int sx2 = curState[bx];   // 人新坐标,也是箱子当前位置
                int sy2 = curState[by];

                int[] nextState = {bx2, by2, sx2, sy2};
                System.out.printf("nextState:%s\n", Arrays.toString(nextState));
                if (oldStateSet.contains(nextState)) {
                    continue; // 已经处理过的状态，跳过
                }
                if (isWall(grid, bx2, by2)) {
                    continue; // 移动后位置不可达
                }
                int sx1 = curState[bx] - dx[i];
                int sy1 = curState[by] - dy[i];
                if (isWall(grid, sx1, sy1)) {
                    continue; // 移动前后方位置不可达
                }
                // 检查连通性,箱子不可通过
                char oldChar = grid[sx2][sy2];
                grid[sx2][sy2] = '#';
                boolean isConnect = isConnect(grid, curState[sx], curState[sy], sx1, sy1);
                grid[sx2][sy2] = oldChar;
                if (!isConnect) {
                    continue;
                }
                nextState = Arrays.copyOf(nextState, 6);
                nextState[step] = curState[step] + 1;   // 路径+1
                nextState[dist] = Math.abs(tx - bx2) + Math.abs(ty - by2); // 后续路径估计
                System.out.printf("join stateQueue:%s\n", Arrays.toString(nextState));
            }
        }
        return -1;
    }

    private boolean isConnect(char[][] grid, int sx0, int sy0, int sx1, int sy1) {
        return isConnect(grid, sx0, sy0, sx1, sy1, new HashSet<>());
    }

    private boolean isConnect(char[][] grid, int sx0, int sy0, int sx1, int sy1, Set<int[]> oldPosSet) {
        if (sx0 == sx1 && sy0 == sy1) {
            return true;
        }
        oldPosSet.add(new int[]{sx1, sy1});
        for (int i = 0; i < 4; i++) {
            int sx2 = sx1 + dx[i];
            int sy2 = sy1 + dy[i];
            if (oldPosSet.contains(new int[]{sx2, sy2})) {
                continue;
            }
            if (isWall(grid, sx2, sy2)) {
                continue;
            }
            if (isConnect(grid, sx0, sy0, sx2, sy2, oldPosSet)) {
                return true;
            }
        }
        return false;
    }

    private boolean isWall(char[][] grid, int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return true;
        }
        return grid[x][y] == '#';
    }


}
