package cn.zyj.tunnel.pathfind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PathFinder {

    // 地图
    private final Map<Vec, Integer> map;

    // 维度
    private final int dimNum;

    // 单位向量
    private final List<Vec> unitVecList;

    public PathFinder(Map<Vec, Integer> map, int dimNum, boolean hasHypotenuse) {
        this.map = Collections.unmodifiableMap(map);
        this.dimNum = dimNum;
        this.unitVecList = Collections.unmodifiableList(!hasHypotenuse ? initUnitVecList(dimNum) : initUnitVecListHasHypotenuse(dimNum));
    }

    /**
     * @param dimNum 维度
     * @return
     */
    public static List<Vec> initUnitVecList(int dimNum) {
        // 不考虑斜边 dim*2
        final List<Vec> unitVecList = new ArrayList<>(dimNum * 2);
        final Vec zeroVec = new Vec(dimNum, 0);
        for (int i = 0; i < dimNum; i++) {
            for (int val = -1; val <= 1; val += 2) {
                unitVecList.add(zeroVec.copy().set(i, val));
            }
        }
        return unitVecList;
    }

    public static List<Vec> initUnitVecListHasHypotenuse(int dimNum) {
        // 考虑斜边 3^dim-1
        List<Vec> unitVecList = new ArrayList<>((int) Math.pow(3, dimNum));
        unitVecList.add(new Vec(dimNum, 0));
        // 执行dimNum次
        for (int i = 0; i < dimNum; i++) {
            // 每次执行 unitVecList2.length=unitVecList.length*=3
            final int len = unitVecList.size();
            for (int j = 0; j < len; j++) {
                final Vec unitVec = unitVecList.get(j);
                for (int val = -1; val <= 1; val += 2) { // -1,1的新建,0的已经有了，后面addAll
                    unitVecList.add(unitVec.copy().set(i, val));
                }
            }
        }
        unitVecList.remove(0);
        return unitVecList;
    }

    // 起点
    private Vec start;
    // 目标
    private Vec target;
    // 存放未确定路径点
    private LinkedHashSet<Vec> openSet;
    // 存放已确定的路径点
    private LinkedHashSet<Vec> closeSet;
    // 存放路径点和上一级路径点的关系
    private Map<Vec, Vec> parentMap;
    // 路径代价 v -> (p,h,g)
    private Map<Vec, PathCost> costMap;

    // 设置起点和目标点
    public void setStartAndTarget(Vec start, Vec target) {
        this.start = start;
        this.target = target;
        this.openSet = new LinkedHashSet<>();
        this.closeSet = new LinkedHashSet<>();
        this.parentMap = new LinkedHashMap<>();

        openSet.add(start);
        costMap.put(start, new PathCost(0, estimateToTarget(start, target), start));
    }

    public int next() {
        if (openSet.equals(target)) {
            return 0; // 找到了终点
        }
        if (openSet.isEmpty()) {
            return -1; // 终点不存在
        }
        // 找到代价最小的路径
        final Vec v = openSet.stream().min(Comparator.comparingInt(it -> costMap.get(it).totalCost)).orElse(null);
        openSet.remove(v);
        closeSet.add(v);
        for (Vec unitVec : unitVecList) {
            final Vec v2 = v.copy().add(unitVec);
            final Integer val = map.get(v2);
            if (val == null || val < 0) {
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

    public List<Vec> findPath() {
        int result;
        while ((result = next()) != 1) ;
        if (result != 0) {
            return null;
        }
        return findPath(parentMap, target);
    }

    public List<Vec> findPath(Map<Vec, Vec> parentMap, Vec v) {
        List<Vec> path = new ArrayList<>();
        Vec pre;
        while ((pre = parentMap.get(v)) != null) {
            v = pre;
            path.add(v);
        }
        return path;
    }

    private PathCost estimateCost(Vec v, Vec v2) {
        final int toStart = computeToStart(v, v2);
        final int toTarget = estimateToTarget(v2, target);
        return new PathCost(toStart, toTarget, v2);
    }

    // 计算到起点的路径长度
    private int computeToStart(Vec v, Vec v2) {
        return costMap.get(v).toStart + map.get(v2);
    }

    // 估算到终点的路径长度
    private int estimateToTarget(Vec v2, Vec target) {
        return target.copy().subtract(v2).distance();
    }

}
