package cn.zyj.tunnel.pathfind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PathFinder {

    // 地图
    private final Map<Vec, Integer> map;

    // 维度
    private final Vec dimVec;

    // 单位向量
    private final List<Vec> unitVecList;

    public PathFinder(Map<Vec, Integer> map, Vec dimVec, boolean hasHypotenuse) {
        this.map = Collections.unmodifiableMap(map);
        this.dimVec = dimVec;
        final int dimNum = dimVec.length();
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
    private Set<Vec> openSet;
    // 存放已确定的路径点
    private Set<Vec> closeSet;
    // 存放路径点和上一级路径点的关系
    private Map<Vec, Vec> parentMap;
    // 路径代价 v -> (p,h,g)
    private Map<Vec, PathCost> costMap;

    // 设置起点和目标点
    public void setStartAndTarget(Vec start, Vec target) {
        this.start = start;
        this.target = target;
        this.openSet = new HashSet<>();
        this.closeSet = new HashSet<>();
        this.parentMap = new HashMap<>();
        this.costMap = new HashMap<>();

        openSet.add(start);
        costMap.put(start, new PathCost(0, estimateToTarget(start, target), start));
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
        while ((result = next()) == 1) {
            printView();
        }
        if (result != 0) {
            return null;
        }
        final List<Vec> path = findPath(parentMap, target);
        return path;
    }

    private void printView() {
        //            System.out.println("openSet:" + openSet);
//            System.out.println("closeSet:" + closeSet);
//            System.out.println("costMap:" + costMap);
        LinkedHashMap<String, Set<Vec>> symbolMap = new LinkedHashMap<>();
        symbolMap.put("S", Collections.singleton(start));
        symbolMap.put("T", Collections.singleton(target));
        symbolMap.put("O", openSet);
        symbolMap.put("X", closeSet);
        /*
        Map<String, Set<Vec>> baseSymbolMap = map.entrySet().stream()
                .collect(Collectors.groupingBy(en -> en.getValue() >= 0 ? "." : "#"
                        , Collectors.mapping(en -> en.getKey(), Collectors.toSet())));
        symbolMap.putAll(baseSymbolMap);
        */
        final List<String> view1 = mapToView(symbolMap, 1);

        LinkedHashMap<String, Set<Vec>> symbolMap2 = new LinkedHashMap<>();
        costMap.forEach((v, cost) -> {
            symbolMap2.put(Double.valueOf(cost.totalCost).intValue() + "", Collections.singleton(v));
        });
        final List<String> view2 = mapToView(symbolMap2, 2);

        for (int i = 0; i < view1.size(); i++) {
            System.out.println(view1.get(i) + "          " + view2.get(i));
        }
        System.out.println();
    }

    private List<String> mapToView(LinkedHashMap<String, Set<Vec>> symbolMap, int width) {
        List<String> lines = new ArrayList<>();
        for (int j = 0; j < dimVec.get(1); j++) {
            String line = "";
            for (int i = 0; i < dimVec.get(0); i++) {
                final Vec v = Vec.create(i, j);
                String symbol = symbolMap.entrySet().stream()
                        .filter(en -> en.getValue().contains(v))
                        .findFirst().map(en -> en.getKey())
                        .orElseGet(() -> map.get(v) >= 0 ? "." : "#");
                line += String.format("%-" + width + "s", symbol) + " ";
            }
            lines.add(line);
        }
        return lines;
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
        return new PathCost(computeToStart(v, v2), estimateToTarget(v2, target), v2);
    }

    // 计算到起点的路径长度
    private double computeToStart(Vec v, Vec v2) {
        return costMap.get(v).toStart + map.get(v2);
    }

    // 估算到终点的路径长度
    private double estimateToTarget(Vec v2, Vec target) {
        return target.copy().subtract(v2).distance();
    }

}
