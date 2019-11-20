package cn.zyj.tunnel.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PathFinder {

    // 地图
    private final Map<int[], Integer> map;

    // 维度
    private final int dimNum;

    // 单位向量
    private final List<int[]> unitVecList;

    public PathFinder(Map<int[], Integer> map, int dimNum, boolean hasHypotenuse) {
        this.map = Collections.unmodifiableMap(map);
        this.dimNum = dimNum;
        this.unitVecList = Collections.unmodifiableList(!hasHypotenuse ? initUnitVecList(dimNum) : initUnitVecListHasHypotenuse(dimNum));
    }

    /**
     * @param dimNum 维度
     * @return
     */
    public static List<int[]> initUnitVecList(int dimNum) {
        // 不考虑斜边 dim*2
        final List<int[]> unitVecList = new ArrayList<>(dimNum * 2);
        final int[] zeroVec = createVec(dimNum, 0);
        for (int i = 0; i < dimNum; i++) {
            for (int val = -1; val <= 1; val += 2) {
                final int[] vec = Arrays.copyOf(zeroVec, dimNum);
                vec[i] = val;
                unitVecList.add(vec);
            }
        }
        return unitVecList;
    }

    public static List<int[]> initUnitVecListHasHypotenuse(int dimNum) {
        // 考虑斜边 3^dim-1
//        List<int[]> unitVecList = new ArrayList<>(3 ^ dimNum);
        final int[] zeroVec = createVec(dimNum, 0);
        int[][] unitVecArr = new int[(int) Math.pow(3, dimNum)][dimNum];
        int unitVecArrLen = 0;
        unitVecArr[unitVecArrLen++] = zeroVec;
        // 执行dimNum次
        for (int i = 0; i < dimNum; i++) {
            // 每次执行 unitVecList2.length=unitVecList.length*=3
            final int len = unitVecArrLen;
            for (int j = 0; j < len; j++) {
                final int[] unitVec = unitVecArr[j];
                for (int val = -1; val <= 1; val += 2) { // -1,1的新建,0的已经有了，后面addAll
                    final int[] vec = Arrays.copyOf(unitVec, unitVec.length);
                    vec[i] = val;
                    unitVecArr[unitVecArrLen++] = vec;
                }
            }
        }
        return Arrays.asList(unitVecArr).subList(1, unitVecArrLen);
    }

    public static int[] createVec(int dimNum, int val) {
        int[] zeros = new int[dimNum];
        for (int i = 0; i < zeros.length; i++) {
            zeros[i] = val;
        }
        return zeros;
    }

    private int[] start;

    private int[] end;

    public void setStart(int[] start) {
        this.start = start;
    }

    public void setEnd(int[] end) {
        this.end = end;
    }

    public void next() {

    }

}
