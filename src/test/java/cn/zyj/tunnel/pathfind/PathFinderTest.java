package cn.zyj.tunnel.pathfind;

import cn.zyj.tunnel.leetcode.PushBox;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Iterator;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class PathFinderTest {

    @Test
    public void test() {
        Assert.assertEquals(4, PathFinder.initUnitVecList(2).size());
        Assert.assertEquals(6, PathFinder.initUnitVecList(3).size());
        Assert.assertEquals(8, PathFinder.initUnitVecListHasHypotenuse(2).size());
        Assert.assertEquals(26, PathFinder.initUnitVecListHasHypotenuse(3).size());
    }

    private char[][] grid = {
            {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '#', '.', '.'},
            {'.', '.', 'B', '.', '#', '.', '#', 'T', '.'},
            {'.', '.', '.', '.', '#', '.', '#', '#', '#'},
            {'.', '#', '#', '#', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
    };

    @Test
    public void test_findPath() {
        final PathFinder finder = PathFinderHelper.createForPushBox(grid, false);
        final Vec start = searchVec(grid, 'B');
        final Vec target = searchVec(grid, 'T');
        finder.setStartAndTarget(start, target);
        final List<Vec> path = finder.findPath();
        log.info("path:" + path);
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

    public int minPushBox(char[][] grid) {
        final PathFinder playerFinder = PathFinderHelper.createForPushBox(grid, false);
        playerFinder.setName("player");
        playerFinder.setLogLevel(0);

        final PathFinder pathFinder = PathFinderHelper.createForPushBox(grid, false);
        pathFinder.setName("box");
        pathFinder.setLogLevel(11);
        final Vec player = searchVec(grid, 'S');
        final Vec start = searchVec(grid, 'B');
        final Vec target = searchVec(grid, 'T');
        pathFinder.setStartAndTarget(start, target);

        int result;
        for (int i = 0; (result = pathFinder.next()) == 1; i++) {
            System.out.printf("%02d:\n", i);
            System.out.println(pathFinder.mapToViews());
            // 遍历所有openVec的转折点
            final Iterator<Vec> it = pathFinder.getOpenSet().iterator();
            while (it.hasNext()) {
                final Vec v = it.next();
                final Vec v1 = pathFinder.getParentMap().get(v);
                if (v1 == null) continue;
                final Vec dv1 = v.copy().subtract(v1);
                final Vec v2;
                if (i > 0) {
                    v2 = pathFinder.getParentMap().get(v1);
                    if (v2 == null) continue;
                    final Vec dv2 = v1.copy().subtract(v2);
                    if (dv1.equals(dv2)) continue; // 走直线不管
                } else {
                    v2 = player;
                }
                final Vec v3 = v1.copy().subtract(dv1);
                // 人:v2->v3
                if (pathFinder.isWall(v3)) {
                    // 不可能推
                    it.remove();
//                    pathFinder.getCloseSet().add(v);
                    continue;
                } else {
                    // 能推，前提 v2和v3连通
                    playerFinder.setStartAndTarget(v2, v3);
                    // player从v2到v3时，箱子在v1，视为墙
                    playerFinder.getCloseSet().add(v1);
                    final List<Vec> path = playerFinder.findPath();
                    if (path == null) {
                        it.remove();
//                        pathFinder.getCloseSet().add(v);
//                        playerFinder.getCostMap().computeIfPresent(v, (k, cost) -> new PathCost(cost.toStart + 20, cost.toTarget, k));
                    }
                }
            }
            System.out.printf("%02d:\n", i);
            System.out.println(pathFinder.mapToViews());
        }
        if (result != 0) {
            return -1;
        }
        final List<Vec> path = pathFinder.findPath(pathFinder.getParentMap(), target);
        System.out.println("step:" + path.size());
        System.out.println("path:" + path);
        pathFinder.pathToViews(path).forEach(System.out::println);
        return path != null ? path.size() : -1;
    }

    char[][] grid1 = {
            {'#', '#', '#', '#', '#', '#'},
            {'#', 'T', '#', '#', '#', '#'},
            {'#', '.', '.', 'B', '.', '#'},
            {'#', '.', '#', '#', '.', '#'},
            {'#', '.', '.', '.', 'S', '#'},
            {'#', '#', '#', '#', '#', '#'}};

    char[][] grid2 = {
            {'#', '#', '#', '#', '#', '#'},
            {'#', 'T', '.', '.', '#', '#'},
            {'#', '.', '#', 'B', '.', '#'},
            {'#', '.', '.', '.', '.', '#'},
            {'#', '.', '.', '.', 'S', '#'},
            {'#', '#', '#', '#', '#', '#'}};

    char[][] grid3 = {
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', 'S', '#', '.', 'B', 'T', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}};

    char[][] grid4 = {
            {'#', '.', '.', '#', 'T', '#', '#', '#', '#'},
            {'#', '.', '.', '#', '.', '#', '.', '.', '#'},
            {'#', '.', '.', '#', '.', '#', 'B', '.', '#'},
            {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
            {'#', '.', '.', '.', '.', '#', '.', 'S', '#'},
            {'#', '.', '.', '#', '.', '#', '#', '#', '#'},
    };

    @Test
    public void test_minPushBox() {
        /*
        Assert.assertEquals(3, minPushBox(grid1));
        Assert.assertEquals(5, minPushBox(grid2));
        Assert.assertEquals(-1, minPushBox(grid3));
        */
        Assert.assertEquals(8, minPushBox(grid4));
    }

    @Test
    public void test_minPushBox2() {
        PushBox pushBox = new PushBox();
        /*
        Assert.assertEquals(3, pushBox.minPushBox(grid1));
        Assert.assertEquals(5, pushBox.minPushBox(grid2));
        Assert.assertEquals(-1, pushBox.minPushBox(grid3));
        */
        Assert.assertEquals(8, pushBox.minPushBox(grid4));
    }

}
