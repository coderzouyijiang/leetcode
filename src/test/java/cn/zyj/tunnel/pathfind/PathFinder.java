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

public class PathFinder {

    private String name = "pathFinder";
    private int logLevel = 0;

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
        final Vec v = openSet.stream().min(Comparator.comparingInt(it -> costMap.get(it).totalCost)).orElse(null);
        openSet.remove(v);
        closeSet.add(v);
        for (Vec unitVec : unitVecList) {
            final Vec v2 = v.copy().add(unitVec);
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

    public List<Vec> findPath() {
        int result;
        for (int i = 0; (result = next()) == 1; i++) {
            if (logLevel % 100 >= 10) {
                System.out.printf("[%s]%02d:\n", name, i);
                System.out.println(mapToViews());
            }
        }
        if (result != 0) {
            return null;
        }
        final List<Vec> path = findPath(parentMap, target);
        if (logLevel % 10 >= 1) {
            System.out.printf("[%s]step:%s\n", name, path.size());
            System.out.printf("[%s]path:%s\n", name, path);
            pathToViews(path).forEach(System.out::println);
        }
        return path;
    }

    public List<String> pathToViews(List<Vec> path) {
        LinkedHashMap<Vec, String> symbolMap = new LinkedHashMap<>();
        map.forEach((v, val) -> symbolMap.put(v, val >= 0 ? "." : "#"));
        symbolMap.put(start, "S");
        symbolMap.put(target, "T");
        path.forEach(v -> symbolMap.put(v, "@"));
        return mapToView(symbolMap, 2);
    }

    public String mapToViews() {
//            System.out.println("openSet:" + openSet);
//            System.out.println("closeSet:" + closeSet);
//            System.out.println("costMap:" + costMap);
        LinkedHashMap<Vec, String> symbolMap = new LinkedHashMap<>();
        map.forEach((v, val) -> symbolMap.put(v, val >= 0 ? "." : "#"));
        openSet.forEach(v -> symbolMap.put(v, "O"));
        closeSet.forEach(v -> symbolMap.put(v, "X"));
        symbolMap.put(start, "S");
        symbolMap.put(target, "T");

        final List<String> view1 = mapToView(symbolMap, 2);

        LinkedHashMap<Vec, String> symbolMap2 = new LinkedHashMap<>();
        map.forEach((v, val) -> symbolMap2.put(v, val >= 0 ? "." : "#"));
        symbolMap2.put(start, "S");
        symbolMap2.put(target, "T");
//        costMap.forEach((v, cost) -> symbolMap2.put(v, cost.totalCost + ""));
        costMap.forEach((v, cost) -> symbolMap2.put(v, cost.toStart + "+" + cost.toTarget));
        final List<String> view2 = mapToView(symbolMap2, 5);

        final Vec v = openSet.stream().min(Comparator.comparingInt(it -> costMap.get(it).totalCost)).orElse(null);
        List<String> view0 = pathToViews(findPath(parentMap, v));

        String str = "";
        for (int i = 0; i < view1.size(); i++) {
            str += view0.get(i) + "  |   " + view1.get(i) + "  |   " + view2.get(i) + "\n";
        }
        return str;
    }

    private List<String> mapToView(LinkedHashMap<Vec, String> symbolMap, int width) {
        List<String> lines = new ArrayList<>();
        for (int j = 0; j < dimVec.get(1); j++) {
            String line = "";
            for (int i = 0; i < dimVec.get(0); i++) {
                final Vec v = Vec.create(i, j);
                final String symbol = symbolMap.getOrDefault(v, "?");
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
            path.add(v);
            v = pre;
        }
        return path;
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
        return target.copy().subtract(v2).distance();
    }

    public Map<Vec, Integer> getMap() {
        return map;
    }

    public Vec getDimVec() {
        return dimVec;
    }

    public List<Vec> getUnitVecList() {
        return unitVecList;
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

    public Map<Vec, Vec> getParentMap() {
        return parentMap;
    }

    public Map<Vec, PathCost> getCostMap() {
        return costMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }
}
