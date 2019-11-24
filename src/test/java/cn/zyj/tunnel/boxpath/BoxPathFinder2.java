package cn.zyj.tunnel.boxpath;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BoxPathFinder2 {

    private static final char PLAYER = 'S';
    private static final char BOX = 'B';
    private static final char TARGET = 'T';
    private static final char WALL = '#';

    // 状态数组:0,1:box坐标；2,3:人的坐标；4:已走过步数；5:当前距离终点的估计距离
    private static final int bx = 0, by = 1, sx = 2, sy = 3, step = 4, dist = 5;
    private static final int stateNum = 6;
    // 下一步的方向
    private static final int[] dx = {0, 1, 0, -1}; // top,right,down,left
    private static final int[] dy = {1, 0, -1, 0};
    private static final int dirNum = 4;

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

        PriorityQueue<int[]> stateQueue = new PriorityQueue(BoxPathFinder2.stateComparator);
        stateQueue.add(startState);

        int[] curState;
        while ((curState = stateQueue.poll()) != null) {
            if (curState[bx] == tx && curState[by] == ty) {
                return step;
            }
            for (int i = 0; i < dirNum; i++) {
                int[] nextState = new int[stateNum];
                nextState[bx] = curState[bx] + dx[i];   // 箱子新坐标
                nextState[by] = curState[by] + dy[i];
                nextState[sx] = curState[bx];   // 人新坐标
                nextState[sy] = curState[by];
                nextState[step] = curState[step] + 1;   // 路径+1
                nextState[dist] = Math.abs(tx - nextState[bx]) + Math.abs(ty - nextState[by]); // 后续路径估计

                System.out.printf("nextState:%s\n", Arrays.toString(nextState));

            }

        }

//        PriorityQueue<int[]> stateQueue = new PriorityQueue<>(Comparator.comparingInt(state -> {
//            state
//        }));
        return -1;
    }


}
