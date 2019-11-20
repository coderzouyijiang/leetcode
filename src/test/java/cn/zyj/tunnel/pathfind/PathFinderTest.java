package cn.zyj.tunnel.pathfind;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
        final Vec start = Vec.create(2, 2);
        final Vec target = Vec.create(6, 2);
        finder.setStartAndTarget(start, target);
        final List<Vec> path = finder.findPath();
        log.info("path:" + path);
    }
}
