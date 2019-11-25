package cn.zyj.tunnel.boxpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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

        final int[] startState = new int[6];
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
                    startState[step] = 0;
                    startState[dist] = Math.abs(tx - i) + Math.abs(ty - j);
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
//        Set<int[]> oldStateSet = new HashSet<>();
        Set<Integer> oldStateSet = new HashSet<>();
        oldStateSetToStr(grid, oldStateSet, startState);

        int index = 0;
        int[] curState;
        while ((curState = stateQueue.poll()) != null) {
            if (curState[bx] == tx && curState[by] == ty) {
                return curState[step];
            }
//            oldStateSet.add(Arrays.copyOfRange(curState, 0, 4));
            oldStateSet.add(computeStateHash(curState));

            for (int i = 0; i < 4; i++) {
                int bx2 = curState[bx] + dx[i];   // 箱子新坐标
                int by2 = curState[by] + dy[i];
                int sx2 = curState[bx];   // 人新坐标,也是箱子当前位置
                int sy2 = curState[by];

                int[] nextState = {bx2, by2, sx2, sy2, -1, -1};
                nextState[step] = curState[step] + 1;   // 路径+1
                nextState[dist] = Math.abs(tx - bx2) + Math.abs(ty - by2); // 后续路径估计

                System.out.printf("[%04d]nextState:%s", ++index, Arrays.toString(nextState));
                if (oldStateSet.contains(computeStateHash(nextState))) {
                    System.out.printf(",%s\n", "1-已经处理过的状态");
                    continue; // 已经处理过的状态，跳过
                }
                if (isWall(grid, bx2, by2)) {
                    System.out.printf(",%s\n", "2-移动后位置不可达");
                    continue; // 移动后位置不可达
                }
                int sx1 = curState[bx] - dx[i];
                int sy1 = curState[by] - dy[i];
                if (isWall(grid, sx1, sy1)) {
                    System.out.printf(",%s\n", "3-移动前后方位置不可达");
                    continue; // 移动前后方位置不可达
                }
                // 检查连通性,箱子不可通过
                if (!isConnect(grid, curState[sx], curState[sy], sx1, sy1, sx2, sy2)) {
                    System.out.printf(",%s:(%s,%s)->(%s,%s)\n", "4-不可连通", curState[sx], curState[sy], sx1, sy1);
                    continue;
                }
                System.out.printf(",join stateQueue:%s\n", Arrays.toString(nextState));
                oldStateSetToStr(grid, oldStateSet, nextState);
                stateQueue.offer(nextState);
            }
        }
        return -1;
    }

    public static void oldStateSetToStr(char[][] grid, Set<Integer> oldStateSet, int[] nextState) {
        char[][] boxPassed = createGrid(grid);
        char[][] playerPassed = createGrid(grid);
        for (Integer code : oldStateSet) {
            int bx1 = code & 0xFF;
            int by1 = (code >> 8) & 0xFF;
            int sx1 = (code >> 16) & 0xFF;
            int sy1 = (code >> 24) & 0xFF;

            boxPassed[bx1][by1] = '1';
            playerPassed[sx1][sy1] = '1';
        }
        List<String> lines1 = passedToStr(grid, boxPassed);
        List<String> lines2 = passedToStr(grid, playerPassed);

        char[][] nextPassed = createGrid(grid);
        nextPassed[nextState[bx]][nextState[by]] = 'B';
        nextPassed[nextState[sx]][nextState[sy]] = 'S';
        List<String> lines3 = passedToStr(grid, nextPassed);
        for (int i = 0; i < grid.length; i++) {
            System.out.println(lines3.get(i) + "    " + lines1.get(i) + "    " + lines2.get(i));
        }
        System.out.println();
    }

    public static int computeStateHash(int[] curState) {
//        return Arrays.hashCode(Arrays.copyOfRange(curState, 0, 4));
        return curState[bx] | (curState[by] << 8) | (curState[sx] << 16) | (curState[sy] << 24);
    }

    // BFS
    public static boolean isConnect(char[][] grid, int sx0, int sy0, int sx1, int sy1, int sx2, int sy2) {

        char[][] grid2 = createGrid(grid);
        grid2[sx0][sy0] = 's';
        grid2[sx1][sy1] = 'S';
        grid2[sx2][sy2] = 'B';
        System.out.println("\n" + String.join("\n", passedToStr(grid, grid2)));

        char oldChar = grid[sx2][sy2];
        grid[sx2][sy2] = '#';
        boolean isConnect = isConnect(grid, sx0, sy0, sx1, sy1);
        grid[sx2][sy2] = oldChar;
        return isConnect;
    }

    public static char[][] createGrid(char[][] grid) {
        char[][] passed = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            passed[i] = new char[grid[i].length];
        }
        return passed;
    }

    // BFS
    public static boolean isConnect(char[][] grid, int sx0, int sy0, int sx1, int sy1) {
        if (sx0 == sx1 && sy0 == sy1) {
            return true;
        }
        LinkedList<int[]> newPosQueue = new LinkedList<>();
        newPosQueue.offer(new int[]{sx1, sy1});

        char[][] passed = createGrid(grid);
        int[] pos;
        while ((pos = newPosQueue.poll()) != null) {
            sx1 = pos[0];
            sy1 = pos[1];
//            oldPosSet.add(pos);
//            passed[sx1] |= (1 << sy1);
            passed[sx1][sy1] = '1';
            if (sx0 == sx1 && sy0 == sy1) {
                return true;
            }
//            System.out.println(Arrays.toString(pos) + "->(" + sx0 + "," + sy0 + ")");
            for (int i = 0; i < 4; i++) {
                int sx2 = sx1 + dx[i];
                int sy2 = sy1 + dy[i];
                if (isWall(grid, sx2, sy2)) {
                    continue;
                }
//                if (oldPosSet.contains(new int[]{sx2, sy2})) {
//                if ((passed[sx2] >> sy2 & (1 << sy2)) > 0) {
                if (passed[sx2][sy2] == '1') {
                    continue;
                }
                passed[sx2][sy2] = '0';
                newPosQueue.offer(new int[]{sx2, sy2});
            }
//            System.out.println("\n" + String.join("\n", passedToStr(grid, passed)));
        }
        return false;
    }

    public static List<String> passedToStr(char[][] grid, char[][] passed) {
        List<String> lines = new ArrayList<>(grid.length);
        for (int i = 0; i < grid.length; i++) {
            String str = "";
            char[] line = grid[i];
            for (int j = 0; j < line.length; j++) {
                if (line[j] == '#') {
                    str += '#';
                } else {
                    str += passed[i][j] != 0 ? passed[i][j] : '.';
                }
                str += " ";
            }
            lines.add(str);
        }
        return lines;
        /*
        int width = grid[0].length;
        return IntStream.of(passed).mapToObj(Integer::toBinaryString)
                .map(it -> String.format("%" + width + "s", it).replaceAll(" ", "0"))
                .collect(Collectors.joining("\n"));
        */
    }

    public static boolean isWall(char[][] grid, int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return true;
        }
        return grid[x][y] == '#';
    }


}
