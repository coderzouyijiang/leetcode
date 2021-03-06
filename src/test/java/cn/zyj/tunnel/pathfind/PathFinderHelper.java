package cn.zyj.tunnel.pathfind;

import java.util.HashMap;
import java.util.Map;

public class PathFinderHelper {

    public static PathFinder createForPushBox(char[][] grid, boolean hasHypotenuse) {
        Map<Character, Integer> valMap = new HashMap<>();
        valMap.put('#', -1); // 不可移动
        valMap.put('.', 1); // 移动代价是1
        valMap.put('S', 1);
        valMap.put('B', 1);
        valMap.put('T', 1);

        final Vec dimVec = Vec.create(grid[0].length, grid.length);
        Map<Vec, Integer> map = new HashMap<>();
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
        return new PathFinder(map, dimVec, hasHypotenuse);
    }

}
