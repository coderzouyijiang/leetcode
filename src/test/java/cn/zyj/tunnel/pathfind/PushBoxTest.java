package cn.zyj.tunnel.pathfind;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiFunction;

@RunWith(JUnit4.class)
public class PushBoxTest {

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

        private T[][] data;
        private int size;

        public VecMap(int xLen, int yLen) {
            Object[] data = new Object[xLen];
            for (int i = 0; i < yLen; i++) {
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
            }
            this.data[v.x][v.y] = value;
            return oldVal;
        }

        public T remove(Vec v) {
            return put(v, null);
        }

    }

    public static class PathFinder {

        private VecMap<Integer> map;
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

    }

}
