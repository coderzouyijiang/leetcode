package cn.zyj.tunnel.pathfind;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PathFinderTest {

    @Test
    public void test() {
        Assert.assertEquals(4, PathFinder.initUnitVecList(2).size());
        Assert.assertEquals(6, PathFinder.initUnitVecList(3).size());
        Assert.assertEquals(8, PathFinder.initUnitVecListHasHypotenuse(2).size());
        Assert.assertEquals(26, PathFinder.initUnitVecListHasHypotenuse(3).size());
    }
}
