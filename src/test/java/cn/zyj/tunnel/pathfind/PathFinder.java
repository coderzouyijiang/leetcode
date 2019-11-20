package cn.zyj.tunnel.pathfind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
    private Map<Vec, int[]> costMap;

    // 设置起点和目标点
    public void setStartAndTarget(Vec start, Vec target) {
        this.start = start;
        this.target = target;
        this.openSet = new LinkedHashSet<>();
        this.closeSet = new LinkedHashSet<>();
        this.parentMap = new LinkedHashMap<>();

        openSet.add(start);
    }

    public boolean next() {
        if (start.equals(target)) {
            return true;
        }
        for (Vec v : openSet) {
            for (Vec unitVec : unitVecList) {
                final Vec v2 = v.copy().add(unitVec);
                final Integer val = map.get(v2);
                if (val == null || val < 0) {
                    continue; // 不可通过的点
                }

            }
        }
        return false;
    }

}
