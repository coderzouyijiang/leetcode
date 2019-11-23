package cn.zyj.tunnel.pathfind;

import cn.zyj.tunnel.leetcode.BoxPathFinder;
import cn.zyj.tunnel.leetcode.PushBox;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

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
        final Vec player = searchVec(grid, 'S');
        final Vec start = searchVec(grid, 'B');
        final Vec target = searchVec(grid, 'T');

        final PathFinder playerFinder = PathFinderHelper.createForPushBox(grid, false);
        playerFinder.setName("player-0");
        playerFinder.setLogLevel(0);

        List<Vec> player2List = new ArrayList<>();
        // 先找player可达的box周围点
        if (start.copy().subtract(player).distance() > 1) {
            for (Vec unitVec : playerFinder.getUnitVecList()) {
                Vec player2 = start.copy().add(unitVec);
                // player从start到v2时，箱子在start，视为墙
                playerFinder.setStartAndTarget(player, player2);
                playerFinder.getCloseSet().add(start);
                List<Vec> playerPath = playerFinder.findPath();
                if (playerPath == null) continue;
                player2List.add(player2);
            }
            if (player2List.isEmpty()) {
                return -1;
            }
        } else {
            player2List.add(player);
        }
        System.out.println("box周围可达点:" + player2List);
        // 视player永远在box后面,player2List内部必然连通,任取一个
        /*
        Vec player2 = player2List.get(0);
        List<Vec> path = minPushBoxPath(grid, start, target, player2, Collections.EMPTY_LIST);
        */
        List<Vec> path = null;
        for (Vec player2 : player2List) {
            List<Vec> path2 = minPushBoxPath(grid, start, target, player2, Collections.EMPTY_LIST);
            if (path == null || path.size() > path2.size()) {
                path = path2;
            }
        }
        return path != null ? path.size() : -1;
    }

    public List<Vec> minPushBoxPath(char[][] grid, Vec start, Vec target, Vec player, List<Vec> historyPath) {

        final PathFinder playerFinder = PathFinderHelper.createForPushBox(grid, false);
        playerFinder.setName("player-" + start);
        playerFinder.setLogLevel(0);

        final PathFinder pathFinder = PathFinderHelper.createForPushBox(grid, false);
        pathFinder.setName("box-" + start);
        pathFinder.setLogLevel(11);
        pathFinder.setStartAndTarget(start, target);
        pathFinder.getCloseSet().addAll(historyPath);
        // 视box从player点出发,简化处理逻辑
        pathFinder.getParentMap().put(start, player);

        int result;
        for (int i = 0; (result = pathFinder.next()) == 1; i++) {
            System.out.printf("%02d:\n", i);
            System.out.println(pathFinder.mapToViews());
            // 遍历所有openVec的转折点
            final Iterator<Vec> it = pathFinder.getOpenSet().iterator();
            while (it.hasNext()) {
                final Vec v = it.next();
                final Vec v1 = pathFinder.getParentMap().get(v);
                Objects.requireNonNull(v1, "data error!");
                final Vec dv1 = v.copy().subtract(v1);
                final Vec v2 = pathFinder.getParentMap().get(v1);
                Objects.requireNonNull(v2, "data error!");
                final Vec dv2 = v1.copy().subtract(v2);
                if (dv1.equals(dv2)) continue; // 走直线不管
//                if (v2 != null) {
//                } else {
//                    dv2 = dv1.copy();
//                    v2 = v1.copy().subtract(dv2);
//                }
                // player 下一步位置
                final Vec v3 = v1.copy().subtract(dv1);
                // 人:v2->v3
                if (pathFinder.isWall(v3)) {
                    // 不可能推,把v移除出openList
                    it.remove();
//                    pathFinder.getCloseSet().add(v);
                    continue;
                }
                // 能推，前提 v2和v3连通
                playerFinder.setStartAndTarget(v2, v3);
                // player从v2到v3时，箱子在v1，视为墙
                playerFinder.getCloseSet().add(v1);
                final List<Vec> playerPath = playerFinder.findPath();
                if (playerPath != null) {
                    continue; // player可过去，正常执行
                }
//                if (playerPath == null) {
//                    it.remove();
//                    pathFinder.getCloseSet().add(v);
//                    playerFinder.getCostMap().computeIfPresent(v, (k, cost) -> new PathCost(cost.toStart + 20, cost.toTarget, k));
//                }
                // 遇到无法推的，直走。box:v1->V4,player:v2->v1
                Vec v4 = v1.copy().add(dv2);
                if (!pathFinder.getNextVecSet().contains(v4)) {
                    it.remove();
                    continue;
                }
                // path:S->V1
                List<Vec> path = pathFinder.findPath(pathFinder.getParentMap(), v);
                path.remove(0); // 移除最开始加入的player
                // 直走可行。递归计算v4->T
                List<Vec> path2 = minPushBoxPath(grid, v4, target, v1, path);
                if (path2 == null) {
                    it.remove();
                    continue;
                }
                path.addAll(path2);
                return path;
            }
            System.out.printf("%02d:\n", i);
            System.out.println(pathFinder.mapToViews());
        }
        if (result != 0) {
            return null;
        }
        List<Vec> path = pathFinder.findPath(pathFinder.getParentMap(), target);
        path.remove(0); // 移除最开始加入的player
        System.out.printf("[%s]step:%s\n", pathFinder.getName(), path.size());
        System.out.printf("[%s]path:%s\n", pathFinder.getName(), path);
        pathFinder.pathToViews(path).forEach(System.out::println);
        return path;
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

    char[][] grid31 = {
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', '.', 'B', '.', 'S', 'T', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}};

    char[][] grid4 = {
            {'#', '.', '.', '#', 'T', '#', '#', '#', '#'},
            {'#', '.', '.', '#', '.', '#', '.', '.', '#'},
            {'#', '.', '.', '#', '.', '#', 'B', '.', '#'},
            {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
            {'#', '.', '.', '.', '.', '#', '.', 'S', '#'},
            {'#', '.', '.', '#', '.', '#', '#', '#', '#'},
    };

    char[][] grid5 = {
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#',},
            {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#',},
            {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#',},
            {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#',},
            {'#', '.', '.', '.', '#', '#', '#', '#', '#', '#', '.', '.', '.', '#',},
            {'#', '.', '.', '.', '#', '#', '#', '#', '#', '#', '.', 'T', '.', '#',},
            {'#', '.', '.', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#',},
            {'#', '.', '.', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#',},
            {'#', '.', '.', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#',},
            {'#', '.', '.', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#',},
            {'#', '.', '.', '.', '#', '#', '#', '#', '#', '#', '.', '.', '.', '#',},
            {'#', '.', '.', '.', '#', '#', '#', '#', '#', '#', '.', '.', '.', '#',},
            {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#',},
            {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#',},
            {'#', '.', '.', '.', '.', '.', 'S', '.', '.', '.', '.', '.', '.', '#',},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#',},
    };

    @Test
    public void test_dk() {
        char[][] grid = this.grid5;
        final PathFinder playerFinder = PathFinderHelper.createForPushBox(grid, false);
        playerFinder.setName("player");
        playerFinder.setLogLevel(11);

        final Vec player = searchVec(grid, 'S');
        final Vec target = searchVec(grid, 'T');
        playerFinder.setStartAndTarget(player, target);
        List<Vec> path = playerFinder.findPath();
        log.info("path=" + path);
    }

    @Test
    public void test_minPushBox() {
        Assert.assertEquals(3, minPushBox(grid1));
//        Assert.assertEquals(5, minPushBox(grid2));
//        Assert.assertEquals(-1, minPushBox(grid3));
//        Assert.assertEquals(-1, minPushBox(grid31));

//        Assert.assertEquals(8, minPushBox(grid4));
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

    @Test
    public void test_minPushBox3() {

        Assert.assertEquals(3, BoxPathFinder.minPathByAStar(grid1).size());
        Assert.assertEquals(3, BoxPathFinder.minPathByAStar(grid2).size());
        Assert.assertEquals(1, BoxPathFinder.minPathByAStar(grid3).size());
    }

    @Test
    public void test_minPushBox4() {

        Assert.assertEquals(3, BoxPathFinder.minPath(grid1).size());
        Assert.assertEquals(5, BoxPathFinder.minPath(grid2).size());
        Assert.assertEquals(-1, BoxPathFinder.minPath(grid3).size());
    }

}
