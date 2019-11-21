package cn.zyj.tunnel.leetcode;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * 推箱子终极版 on 20191121
 */
public class PushBox {

    public static class Vec {
        public final int x;
        public final int y;

        public Vec(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vec vec = (Vec) o;

            if (x != vec.x) return false;
            return y == vec.y;
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

        public Vec add(Vec v) {
            return new Vec(x + v.x, y + v.y);
        }

        public Vec subtract(Vec v) {
            return new Vec(x - v.x, y - v.y);
        }

        public int distance() {
            return Math.abs(x) + Math.abs(y);
        }

        public static Vec create(int x, int y) {
            return new Vec(x, y);
        }
    }

    public static class PathCost {

        public int toStart;
        public int toTarget;
        public int totalCost;

        public Vec point;

        public PathCost(int toStart, int toTarget, Vec point) {
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

    public static class VecMap<T> {

        private final T[][] data;
        private int size;

        public VecMap(int xLen, int yLen) {
            Object[][] data = new Object[xLen][];
            for (int i = 0; i < xLen; i++) {
                data[i] = new Object[yLen];
            }
            this.data = (T[][]) data;
            this.size = 0;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public T forEachKey(BiFunction<Integer, Integer, T> loopBody) {
            for (int i = 0; i < data.length; i++) {
                final T[] line = data[i];
                for (int j = 0; j < line.length; j++) {
                    final T val = loopBody.apply(i, j);
                    if (val != null) {
                        return val;
                    }
                }
            }
            return null;
        }

        public boolean containsKey(Object key) {
            if (key instanceof Vec) {
                Vec v = (Vec) key;
                return this.data[v.x][v.y] != null;
            }
            return false;
        }

        public boolean containsValue(Object value) {
            return forEachKey((x, y) -> {
                final T val = data[x][y];
                return value.equals(val) ? val : null;
            }) != null;
        }

        public T get(Vec v) {
            return this.data[v.x][v.y];
        }


        public T put(Vec v, T value) {
            T oldVal = this.data[v.x][v.y];
            if (oldVal != null) {
                size--;
            }
            if (value != null) {
                size++;
                this.data[v.x][v.y] = value;
            }
            return oldVal;
        }

        public T remove(Vec v) {
            return put(v, null);
        }

    }

    public static class PathFinder {

        private VecMap<Integer> map;

        private Vec dimVec;
        // 起点
        private Vec start;
        // 目标
        private Vec target;
        // 存放未确定路径点
        private Set<Vec> openSet;
        // 存放已确定的路径点
        private Set<Vec> closeSet;
        // 存放路径点和上一级路径点的关系
        private VecMap<Vec> parentMap;
        // 路径代价 v -> (p,h,g)
        private VecMap<PathCost> costMap;

        private static LinkedHashSet<Vec> unitVecList;

        static {
            unitVecList = new LinkedHashSet<>();
            unitVecList.add(new Vec(1, 0));
            unitVecList.add(new Vec(-1, 0));
            unitVecList.add(new Vec(0, 1));
            unitVecList.add(new Vec(0, -1));
        }

        private <T> T get(T[][] map, Vec v) {
            return map[v.x][v.y];
        }

        public int next() {
            if (openSet.contains(target)) {
                return 0; // 找到了终点
            }
            if (openSet.isEmpty()) {
                return -1; // 终点不存在
            }
            // 找到代价最小的路径
            final Vec v = openSet.stream().min(Comparator.comparingDouble(it -> costMap.get(it).totalCost)).orElse(null);
            openSet.remove(v);
            closeSet.add(v);
            for (Vec unitVec : unitVecList) {
                final Vec v2 = v.add(unitVec);
                if (isWall(v2)) {
                    continue; // 不可通过的点
                }
                if (closeSet.contains(v2)) {
                    continue; // 已经确定的路径
                }
                final PathCost cost = estimateCost(v, v2);
                if (!openSet.contains(v2)) {
                    openSet.add(v2);
                    costMap.put(v2, cost);
                    parentMap.put(v2, v);
                } else {
                    // 已经探测过的路径，重新计算代价
                    PathCost oldCost = costMap.get(v2);
                    if (cost.totalCost < oldCost.totalCost) {
                        costMap.put(v2, cost);
                        parentMap.put(v2, v);
                    }
                }
            }
            return 1;
        }

        public boolean isWall(Vec v2) {
            final Integer val = map.get(v2);
            return val == null || val < 0;
        }

        private PathCost estimateCost(Vec v, Vec v2) {
            return new PathCost(computeToStart(v, v2), estimateToTarget(v2, target), v2);
        }

        // 计算到起点的路径长度
        private int computeToStart(Vec v, Vec v2) {
            return costMap.get(v).toStart + map.get(v2);
        }

        // 估算到终点的路径长度
        private int estimateToTarget(Vec v2, Vec target) {
            return target.subtract(v2).distance();
        }

        public List<Vec> findPath() {
            int result;
            for (int i = 0; (result = next()) == 1; i++) {
            }
            if (result != 0) {
                return null;
            }
            final List<Vec> path = findPath(parentMap, target);
            return path;
        }

        public List<Vec> findPath(VecMap<Vec> parentMap, Vec v) {
            List<Vec> path = new ArrayList<>();
            Vec pre;
            while ((pre = parentMap.get(v)) != null) {
                path.add(v);
                v = pre;
            }
            return path;
        }

        public VecMap<Integer> getMap() {
            return map;
        }

        public Vec getStart() {
            return start;
        }

        public Vec getTarget() {
            return target;
        }

        public Set<Vec> getOpenSet() {
            return openSet;
        }

        public Set<Vec> getCloseSet() {
            return closeSet;
        }

        public VecMap<Vec> getParentMap() {
            return parentMap;
        }

        public VecMap<PathCost> getCostMap() {
            return costMap;
        }

        public static LinkedHashSet<Vec> getUnitVecList() {
            return unitVecList;
        }

        public PathFinder(VecMap<Integer> map, Vec dimVec) {
            this.map = map;
            this.dimVec = dimVec;
        }

        // 设置起点和目标点
        public void setStartAndTarget(Vec start, Vec target) {
            this.start = start;
            this.target = target;
            this.openSet = new HashSet<>();
            this.closeSet = new HashSet<>();
            this.parentMap = new VecMap<>(dimVec.x, dimVec.y);
            this.costMap = new VecMap<>(dimVec.x, dimVec.y);

            openSet.add(start);
            costMap.put(start, new PathCost(0, estimateToTarget(start, target), start));
        }

    }

    public Vec searchVec(char[][] grid, char x) {
        for (int i = 0; i < grid.length; i++) {
            final char[] line = grid[i];
            for (int j = 0; j < line.length; j++) {
                if (line[j] == x) {
                    return Vec.create(j, i);
                }
            }
        }
        return null;
    }

    public static PathFinder createForPushBox(char[][] grid) {
        Map<Character, Integer> valMap = new HashMap<>();
        valMap.put('#', -1); // 不可移动
        valMap.put('.', 1); // 移动代价是1
        valMap.put('S', 1);
        valMap.put('B', 1);
        valMap.put('T', 1);

        final Vec dimVec = Vec.create(grid[0].length, grid.length);
        VecMap<Integer> map = new VecMap<>(dimVec.x, dimVec.y);
        for (int i = 0; i < grid.length; i++) {
            final char[] line = grid[i];
            for (int j = 0; j < line.length; j++) {
                final char val = line[j];
                final Integer val2 = valMap.get(val);
                if (val2 == null) {
                    throw new IllegalArgumentException("找不到对应的映射,val(" + i + "," + j + ")=" + val);
                }
                map.put(Vec.create(j, i), val2);
            }
        }
        return new PathFinder(map, dimVec);
    }

    public List<Vec> minPushBoxPath(char[][] grid) {
        final PathFinder playerFinder = createForPushBox(grid);

        final PathFinder pathFinder = createForPushBox(grid);
        final Vec player = searchVec(grid, 'S');
        final Vec start = searchVec(grid, 'B');
        final Vec target = searchVec(grid, 'T');
        pathFinder.setStartAndTarget(start, target);

        int result;
        for (int i = 0; (result = pathFinder.next()) == 1; i++) {
            // 遍历所有openVec的转折点
            final Iterator<Vec> it = pathFinder.getOpenSet().iterator();
            while (it.hasNext()) {
                final Vec v = it.next();
                final Vec v1 = pathFinder.getParentMap().get(v);
                if (v1 == null) continue;
                final Vec dv1 = v.subtract(v1);
                final Vec v2;
                if (i > 0) {
                    v2 = pathFinder.getParentMap().get(v1);
                    if (v2 == null) continue;
                    final Vec dv2 = v1.subtract(v2);
                    if (dv1.equals(dv2)) continue; // 走直线不管
                } else {
                    v2 = player;
                }
                final Vec v3 = v1.subtract(dv1);
                // 人:v2->v3
                if (pathFinder.isWall(v3)) {
                    // 不可能推
                    it.remove();
                    pathFinder.getCloseSet().add(v);
                    continue;
                } else {
                    // 能推，前提 v2和v3连通
                    playerFinder.setStartAndTarget(v2, v3);
                    // player从v2到v3时，箱子在v1，视为墙
                    playerFinder.getCloseSet().add(v1);
                    final List<Vec> path = playerFinder.findPath();
                    if (path == null) {
                        it.remove();
                        pathFinder.getCloseSet().add(v);
                    }
                }
            }
        }
        if (result != 0) {
            return null;
        }
        final List<Vec> path = pathFinder.findPath(pathFinder.getParentMap(), target);
        return path;
    }

    public int minPushBox(char[][] grid) {
        List<Vec> path = minPushBoxPath(grid);
        return path != null ? path.size() : -1;
    }
}
