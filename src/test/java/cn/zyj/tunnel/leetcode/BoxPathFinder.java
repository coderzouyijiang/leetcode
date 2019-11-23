package cn.zyj.tunnel.leetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BoxPathFinder {

    public static class Vec3 {
        public final int x;
        public final int y;
        public final int d;

        public Vec3(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vec3 vec3 = (Vec3) o;

            if (x != vec3.x) return false;
            if (y != vec3.y) return false;
            return d == vec3.d;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + d;
            return result;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + "," + d + ")";
        }

        public static Vec3 create(int x, int y, int d) {
            return new Vec3(x, y, d);
        }

        public Vec2 toVec2() {
            return Vec2.create(x, y);
        }
    }

    public static class Vec2 {
        public final int x;
        public final int y;

        public Vec2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vec2 vec2 = (Vec2) o;

            if (x != vec2.x) return false;
            return y == vec2.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public Vec2 add(Vec2 v) {
            return new Vec2(x + v.x, y + v.y);
        }

        public Vec2 subtract(Vec2 v) {
            return new Vec2(x - v.x, y - v.y);
        }

        public int distanceTo(Vec2 v) {
            return Math.abs(this.x - v.x) + Math.abs(this.y - v.y);
        }

        public static Vec2 create(int x, int y) {
            return new Vec2(x, y);
        }

        public Vec3 toVec3(int d) {
            return Vec3.create(x, y, d);
        }
    }

    public static class PathCost {

        public int toStart;
        public int toTarget;
        public int totalCost;

        public Vec2 point;

        public PathCost(int toStart, int toTarget, Vec2 point) {
            this.toStart = toStart;
            this.toTarget = toTarget;
            this.totalCost = toStart + toTarget;
            this.point = point;
        }

        @Override
        public String toString() {
            return "PathCost[" +
                    "" + toStart +
                    "," + toTarget +
                    "," + totalCost +
                    ']';
        }
    }

    // 方向代号
    private static final int DIR_NONE = 0;
    private static final int DIR_TOP = 1;
    private static final int DIR_RIGHT = 2;
    private static final int DIR_DOWN = 3;
    private static final int DIR_LEFT = 4;

    private static final int DIR_NUM = 4;

    // 方向向量
    private static final Vec2[] dirArr = new Vec2[]{
            new Vec2(0, 0),
            new Vec2(0, 1),
            new Vec2(1, 0),
            new Vec2(0, -1),
            new Vec2(-1, 0),
    };

    // 反方向
    private static final Vec2[] negDirArr = new Vec2[]{
            new Vec2(0, 0),
            new Vec2(0, -1),
            new Vec2(-1, 0),
            new Vec2(0, 1),
            new Vec2(1, 0),

    };

    private int[][] map;

    private Vec2 dimVec;
    // 起点
    private Vec2 start;
    // 目标
    private Vec2 target;
    // 存放未确定路径点
    private Set<Vec3> openSet;
    private Set<Vec2> openSet2;
    // 存放已确定的路径点
    private Set<Vec3> closeSet;
    // 存放路径点和上一级路径点的关系,3个维度:x,y,dir
    private Vec3[][][] parentMap;
    // 路径代价 v -> (p,h,g)
    private PathCost[][][] costMap;

    public BoxPathFinder(char[][] grid) {
        this.map = createMap(grid);
        this.dimVec = Vec2.create(map.length, map[0].length);
    }

    public boolean isWall(Vec2 v) {
        return getMapVal(v) < 0;
    }

    public int getMapVal(Vec2 v) {
        if ((v.x < 0 || v.x >= dimVec.x) || (v.y < 0 || v.y >= dimVec.y)) {
            return -1;
        }
        return this.map[v.x][v.y];
    }

    public Vec3 getParent(Vec3 v) {
        return this.parentMap[v.x][v.y][v.d];
    }

    public void putParent(Vec3 v, Vec3 parent) {
        this.parentMap[v.x][v.y][v.d] = parent;
    }

    public PathCost getCost(Vec3 v) {
        return this.costMap[v.x][v.y][v.d];
    }

    public void putCost(Vec3 v, PathCost cost) {
        this.costMap[v.x][v.y][v.d] = cost;
    }

    // 设置起点和目标点
    public void setStartAndTarget(Vec2 start, Vec2 target) {
        this.start = start;
        this.target = target;
        this.openSet = new HashSet<>();
        this.closeSet = new HashSet<>();
        this.parentMap = new Vec3[dimVec.x][][];
        this.costMap = new PathCost[dimVec.x][][];
        for (int i = 0; i < dimVec.x; i++) {
            parentMap[i] = new Vec3[dimVec.y][];
            costMap[i] = new PathCost[dimVec.y][];
            for (int j = 0; j < dimVec.y; j++) {
                parentMap[i][j] = new Vec3[DIR_NUM];
                costMap[i][j] = new PathCost[DIR_NUM];
            }
        }
        PathCost startCost = new PathCost(0, estimateToTarget(start, target), start);
        for (int d = DIR_TOP; d <= DIR_LEFT; d++) {
            Vec2 dir = dirArr[d];
            Vec2 player = start.subtract(dir);

            openSet2.add(start);
            Vec3 p = start.toVec3(d);
            openSet.add(p);
            putCost(p, startCost);
        }
    }

    public int next() {
        if (openSet2.contains(target)) {
            return 0; // 找到了终点
        }
        if (openSet.isEmpty()) {
            return -1; // 终点不存在
        }
        // 找到代价最小的路径
        final Vec3 p = openSet.stream().min(Comparator.comparingInt(it -> getCost(it).totalCost)).orElse(null);
        openSet.remove(p);
        closeSet.add(p);
        // 旋转
        for (int d = DIR_TOP; d <= DIR_LEFT; d++) {
            Vec2 dir = dirArr[d]; // 方向
            Vec2 p2 = p.toVec2().add(dir);
            if (d != p.d) {
                Vec2 p0 = p.toVec2().subtract(dir);
            }

        }
        /*
        for (Vec2 dirVec3 : dirArr) {
            final Vec3 v2 = p.add(dirVec3);
            if (isWall(v2)) {
                continue; // 不可通过的点
            }
            if (closeSet.contains(v2)) {
                continue; // 已经确定的路径
            }
            final PathCost cost = estimateCost(p, v2);
            if (!openSet.contains(v2)) {
                openSet.add(v2);
                costMap.put(v2, cost);
                parentMap.put(v2, p);
            } else {
                // 已经探测过的路径，重新计算代价
                PathCost oldCost = costMap.get(v2);
                if (cost.totalCost < oldCost.totalCost) {
                    costMap.put(v2, cost);
                    parentMap.put(v2, p);
                }
            }
        }
        */
        return 1;
    }

    private PathCost estimateCost(Vec3 v, Vec2 v2) {
        return new PathCost(computeToStart(v, v2), estimateToTarget(v2, target), v2);
    }

    // 计算到起点的路径长度
    private int computeToStart(Vec3 v, Vec2 v2) {
        return getCost(v).toStart + getMapVal(v2);
    }

    // 估算到终点的路径长度
    private int estimateToTarget(Vec2 v2, Vec2 target) {
        return target.distanceTo(v2);
    }

    public static int[][] createMap(char[][] grid) {
        Map<Character, Integer> valMap = new HashMap<>();
        valMap.put('#', -1); // 不可移动
        valMap.put('.', 1); // 移动代价是1
        valMap.put('S', 1);
        valMap.put('B', 1);
        valMap.put('T', 1);

//        final Vec3 dimVec3 = Vec3.create(grid[0].length, grid.length, DIR_NUM);
//        Vec3Map<Integer> map = new Vec3Map<>(dimVec3);
        int[][] map = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            final char[] line = grid[i];
            map[i] = new int[line.length];
            for (int j = 0; j < line.length; j++) {
                final char val = line[j];
                final Integer val2 = valMap.get(val);
                if (val2 == null) {
                    throw new IllegalArgumentException("找不到对应的映射,val(" + i + "," + j + ")=" + val);
                }
                map[i][j] = val2;
            }
        }
        return map;
    }

    public static Vec2 searchVec2(char[][] grid, char x) {
        for (int i = 0; i < grid.length; i++) {
            final char[] line = grid[i];
            for (int j = 0; j < line.length; j++) {
                if (line[j] == x) {
                    return Vec2.create(i, j);
                }
            }
        }
        return null;
    }

    public static class PathFinder {

        private int[][] map;

        private Vec2 dimVec;
        // 起点
        private Vec2 start;
        // 目标
        private Vec2 target;
        // 存放未确定路径点
        private Set<Vec2> openSet;
        // 存放已确定的路径点
        private Set<Vec2> closeSet;
        // 存放路径点和上一级路径点的关系
        private Vec2[][] parentMap;
        // 路径代价 v -> (p,h,g)
        private PathCost[][] costMap;

        private static final Vec2[] unitVecList = dirArr;

        public int getMapVal(Vec2 v) {
            if ((v.x < 0 || v.x >= dimVec.x) || (v.y < 0 || v.y >= dimVec.y)) {
                return -1;
            }
            return this.map[v.x][v.y];
        }

        public Vec2 getParent(Vec2 v) {
            return this.parentMap[v.x][v.y];
        }

        public void putParent(Vec2 v, Vec2 parent) {
            this.parentMap[v.x][v.y] = parent;
        }

        public PathCost getCost(Vec2 v) {
            return this.costMap[v.x][v.y];
        }

        public void putCost(Vec2 v, PathCost cost) {
            this.costMap[v.x][v.y] = cost;
        }

        public int next() {
            if (openSet.contains(target)) {
                return 0; // 找到了终点
            }
            if (openSet.isEmpty()) {
                return -1; // 终点不存在
            }
            // 找到代价最小的路径
            final Vec2 v = openSet.stream().min(Comparator.comparingInt(it -> getCost(it).totalCost)).orElse(null);
            openSet.remove(v);
            closeSet.add(v);
            for (Vec2 unitVec : unitVecList) {
                final Vec2 v2 = v.add(unitVec);
                if (isWall(v2)) {
                    continue; // 不可通过的点
                }
                if (closeSet.contains(v2)) {
                    continue; // 已经确定的路径
                }
                final PathCost cost = estimateCost(v, v2);
                if (!openSet.contains(v2)) {
                    openSet.add(v2);
                    putCost(v2, cost);
                    putParent(v2, v);
                } else {
                    // 已经探测过的路径，重新计算代价
                    PathCost oldCost = getCost(v2);
                    if (cost.totalCost < oldCost.totalCost) {
                        putCost(v2, cost);
                        putParent(v2, v);
                    }
                }
            }
            return 1;
        }

        public boolean isWall(Vec2 v2) {
            return getMapVal(v2) < 0;
        }

        private PathCost estimateCost(Vec2 v, Vec2 v2) {
            return new PathCost(computeToStart(v, v2), estimateToTarget(v2, target), v2);
        }

        // 计算到起点的路径长度
        private int computeToStart(Vec2 v, Vec2 v2) {
            return getCost(v).toStart + getMapVal(v2);
        }

        // 估算到终点的路径长度
        private int estimateToTarget(Vec2 v2, Vec2 target) {
            return target.distanceTo(v2);
        }

        public List<Vec2> findPath() {
            int result;
            for (int i = 0; (result = next()) == 1; i++) {
            }
            if (result != 0) {
                return null;
            }
            final List<Vec2> path = findPath(parentMap, target);
            return path;
        }

        public List<Vec2> findPath(Vec2[][] parentMap, Vec2 v) {
            List<Vec2> path = new ArrayList<>();
            Vec2 pre;
            while ((pre = getParent(v)) != null) {
                path.add(v);
                v = pre;
            }
            return path;
        }

        public PathFinder(int[][] map) {
            this.map = map;
            this.dimVec = Vec2.create(map.length, map[0].length);
        }

        // 设置起点和目标点
        public void setStartAndTarget(Vec2 start, Vec2 target) {
            this.start = start;
            this.target = target;
            this.openSet = new HashSet<>();
            this.closeSet = new HashSet<>();
            this.parentMap = new Vec2[dimVec.x][];
            this.costMap = new PathCost[dimVec.x][];
            for (int i = 0; i < dimVec.x; i++) {
                parentMap[i] = new Vec2[dimVec.y];
                costMap[i] = new PathCost[dimVec.y];
            }
            openSet.add(start);
            putCost(start, new PathCost(0, estimateToTarget(start, target), start));
        }

    }

    public static List<Vec2> minPath(char[][] grid) {
        int[][] map = BoxPathFinder.createMap(grid);
        PathFinder pathFinder = new PathFinder(map);
//        final Vec2 player = BoxPathFinder.searchVec2(grid, 'S');
        final Vec2 box = BoxPathFinder.searchVec2(grid, 'B');
        final Vec2 target = BoxPathFinder.searchVec2(grid, 'T');
        pathFinder.setStartAndTarget(box, target);
        return pathFinder.findPath();
    }

    public static List<Vec2> minPath(PathFinder pathFinder, Vec2 start, Vec2 target, Vec2 box) {
        pathFinder.setStartAndTarget(start, target);
        pathFinder.closeSet.add(box);
        return pathFinder.findPath();
    }

}
