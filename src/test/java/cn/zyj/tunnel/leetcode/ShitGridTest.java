package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class ShitGridTest {

    public class ShitGrid {

        public List<List<Integer>> shiftGrid(int[][] grid, int k) {
            return null;
        }

    }

    @Test
    public void test() {
        ShitGrid obj = new ShitGrid();
//        输入：grid = {{1,2,3},{4,5,6},{7,8,9}}, k = 1
//        输出：{{9,1,2},{3,4,5},{6,7,8}}
        Assert.assertEquals(new int[][]{{9, 1, 2}, {3, 4, 5}, {6, 7, 8}}
                , obj.shiftGrid(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 1));
//        输入：grid = {{3,8,1,9},{19,7,2,5},{4,6,11,10},{12,0,21,13}}, k = 4
//        输出：{{12,0,21,13},{3,8,1,9},{19,7,2,5},{4,6,11,10}}
        Assert.assertEquals(new int[][]{{12, 0, 21, 13}, {3, 8, 1, 9}, {19, 7, 2, 5}, {4, 6, 11, 10}}
                , obj.shiftGrid(new int[][]{{3, 8, 1, 9}, {19, 7, 2, 5}, {4, 6, 11, 10}, {12, 0, 21, 13}}, 4));
//        输入：grid = {{1,2,3},{4,5,6},{7,8,9}}, k = 9
//        输出：{{1,2,3},{4,5,6},{7,8,9}}
        Assert.assertEquals(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}
                , obj.shiftGrid(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 9));
    }


}
