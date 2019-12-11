package cn.zyj.tunnel.leetcode;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
@RunWith(JUnit4.class)
public class CountShipsTest {

    public static class Sea {
        private int num = 0;
        //        private boolean[][] grid = new boolean[1000][1000];
        private List<int[]> posList;

        List<String> finedList = new LinkedList<>();

        public boolean hasShips(int[] topRight, int[] bottomLeft) {
            boolean flag = false;
            for (int[] pos : posList) {
                int x = pos[0];
                int y = pos[1];
                if (x >= bottomLeft[0] && x <= topRight[0] && y >= bottomLeft[1] && y <= topRight[1]) {
                    flag = true;
                    break;
                }
            }
            int w = topRight[0] - bottomLeft[0] + 1;
            int h = topRight[1] - bottomLeft[1] + 1;
            System.out.printf("%03d:(%s,%s)-(%s,%s),w=%s,h=%s,flag=%s\n", ++num, bottomLeft[0], bottomLeft[1], topRight[0], topRight[1], w, h, flag);
            if (flag && w == 1 && h == 1) {
                finedList.add("(" + bottomLeft[0] + "," + bottomLeft[1] + ")");
            }
            return flag;
        }

        public Sea(String json) {
            // [[1,1],[2,2],[3,3],[5,5]]
            this.posList = JSON.parseArray(json, int[].class);
        }
    }

    final int gridNum = 2;

    public int countShips0(Sea sea, int[] topRight, int[] bottomLeft) {
        int l0 = bottomLeft[0];
        int b0 = bottomLeft[1];
        int r0 = topRight[0];
        int t0 = topRight[1];
        int h0 = t0 - b0 + 1; // 高
        int w0 = r0 - l0 + 1; // 宽
        /*
        int maxWidth = Math.max(w0, h0);
        int h1 = maxWidth;
        int w1 = maxWidth;
        int hNum = Math.min((int) Math.ceil((double) h0 / h1), gridNum);
        int wNum = Math.min((int) Math.ceil((double) w0 / w1), gridNum);
        */
        // 格子数 hNum=min(h0,100)
        int hNum = Math.min(h0, gridNum);
        int wNum = Math.min(w0, gridNum);
        // 小格宽度 h1=h0/hNum,向上取整
        int h1 = (int) Math.ceil((double) h0 / (double) hNum);
        int w1 = (int) Math.ceil((double) w0 / (double) wNum);
        int iMax = wNum - 1;
        int jMax = hNum - 1;
        System.out.printf("countShips:(%s,%s)-(%s,%s),w0=%s,h0=%s->wNum=%s,hNum=%s,w1=%s,h1=%s\n", l0, b0, r0, t0, w0, h0, wNum, hNum, w1, h1);
        List<int[]> wRanges = new LinkedList<>();
        for (int l = l0, i = 0; i < wNum; l += w1, i++) {
            int[] lb = {l, b0};
            int[] rt = {i < iMax ? (l + w1 - 1) : r0, t0};
            if (sea.hasShips(rt, lb)) {
                wRanges.add(new int[]{lb[0], rt[0]});
            }
        }
        List<int[]> hRanges = new LinkedList<>();
        for (int b = b0, j = 0; j < hNum; b += h1, j++) {
            int[] lb = {l0, b};
            int[] rt = {r0, j < jMax ? (b + h1 - 1) : t0};
            if (sea.hasShips(rt, lb)) {
                hRanges.add(new int[]{lb[1], rt[1]});
            }
        }
        System.out.println();
        int count = 0;
        for (int[] wRange : wRanges) {
            for (int[] hRange : hRanges) {
                int w2 = wRange[1] - wRange[0] + 1;
                int h2 = hRange[1] - hRange[0] + 1;
                if (w2 == 1) {
                    for (int y = hRange[0]; y <= hRange[1]; y++) {
                        int[] pos = {wRange[0], y};
                        count += sea.hasShips(pos, pos) ? 1 : 0;
                    }
                } else if (h2 == 1) {
                    for (int x = wRange[0]; x <= wRange[1]; x++) {
                        int[] pos = {x, hRange[0]};
                        count += sea.hasShips(pos, pos) ? 1 : 0;
                    }
                } else {
                    int[] lb = {wRange[0], hRange[0]};
                    int[] rt = {wRange[1], hRange[1]};
                    count += countShips0(sea, rt, lb);
                }
                if (count >= 10) { // 至多只有 10 艘船。
                    return count;
                }
            }
        }
        return count;
    }

    private static final int left = 0;
    private static final int bottom = 1;
    private static final int right = 2;
    private static final int top = 3;
    private static final int area = 4; // 面积

    public int countShips2(Sea sea, int[] rt, int[] lb) {
        PriorityQueue<int[]> rectQueue = new PriorityQueue<>(Comparator.comparingInt(r -> r[area]));
        rectQueue.offer(createRect(rt, lb));
        int count = 0;
        int[] rect;
        while ((rect = rectQueue.poll()) != null) {
            int w = rect[right] - rect[left];
            int h = rect[top] - rect[bottom];
            if (w == 0 && h == 0) {
                if (++count > 10) {
                    return count;
                }
            }
            List<int[][]> paramsList;
            if (w >= h) {
                paramsList = Arrays.asList(
                        new int[][]{{rect[right], rect[top]}, {rect[left] + w / 2 + 1, rect[bottom]}},
                        new int[][]{{rect[left] + w / 2, rect[top]}, {rect[left], rect[bottom]}}
                );
            } else {
                paramsList = Arrays.asList(
                        new int[][]{{rect[right], rect[top]}, {rect[left], rect[bottom] + h / 2 + 1}},
                        new int[][]{{rect[right], rect[bottom] + h / 2}, {rect[left], rect[bottom]}}
                );
            }
            /*
            int wMid = rect[left] + w / 2;
            int hMid = rect[bottom] + h / 2;
            List<int[][]> paramsList = Arrays.asList(
                    new int[][]{{rect[right], rect[top]}, {wMid + 1, hMid + 1}},
                    new int[][]{{wMid, hMid}, {rect[left], rect[bottom]}},
                    new int[][]{{wMid, rect[top]}, {rect[left], hMid + 1}}, // 中上-左中
                    new int[][]{{rect[right], hMid}, {wMid + 1, rect[bottom]}} // 右中-中下
            );
            */
            for (int[][] params : paramsList) {
                if (sea.hasShips(params[0], params[1])) {
                    rectQueue.offer(createRect(params[0], params[1]));
                }
            }
        }
        return count;
    }

    private int[] createRect(int[] rt, int[] lb) {
        int[] rect = {lb[0], lb[1], rt[0], rt[1], (rt[0] - lb[0]) * (rt[1] - lb[1])};
        System.out.printf("createRect:%s\n", Arrays.toString(rect));
        return rect;
    }

    public int countShips(Sea sea, int[] rt, int[] lb) {
        return helpCountShips(sea, lb[0], lb[1], rt[0], rt[1]);
    }

    public int helpCountShips(Sea sea, int l, int b, int r, int t) {
        if (!sea.hasShips(new int[]{r, t}, new int[]{l, b})) {
            return 0;
        }
        if (l == r && b == t) {
            return 1;
        }
        if (r - l >= t - b) { // 宽度>高度
            int xMid = (r + l) / 2;
            return helpCountShips(sea, l, b, xMid, t)
                    + helpCountShips(sea, xMid + 1, b, r, t);
        } else {
            int yMid = (t + b) / 2;
            return helpCountShips(sea, l, b, r, yMid)
                    + helpCountShips(sea, l, yMid + 1, r, t);
        }
    }

    @Test
    public void test() {
        // ships = [[1,1],[2,2],[3,3],[5,5]], topRight = [4,4], bottomLeft = [0,0]
        // 输出：3
        Sea sea = new Sea("[[1,1],[2,2],[3,3],[5,5]]");
        int count = countShips(sea, new int[]{4, 4}, new int[]{0, 0});
        log.info("" + count);
        Assert.assertEquals(3, count);
    }

    @Test
    public void test2() {
//        [[1,1],[2,2],[3,3]]
//        [1000,1000]
//        [0,0]
        Sea sea = new Sea("[[1,1],[2,2],[3,3]]");
        int count = countShips(sea, new int[]{1000, 1000}, new int[]{0, 0});
        log.info("" + count);
        Assert.assertEquals(3, count);
    }

    @Test
    public void test3() {
//    [[1,1],[2,2],[3,3],[5,5]]
//      [4,4]
//      [0,0]
        Sea sea = new Sea("[[1,1],[2,2],[3,3],[5,5]]");
        int count = countShips(sea, new int[]{4, 4}, new int[]{0, 0});
        log.info("" + count);
        Assert.assertEquals(3, count);
    }

    @Test
    public void test4() {
//        [[6,6],[100,50],[999,81],[50,50],[700,600]]
//        [1000,1000]
//        [0,0]
        Sea sea = new Sea("[[6,6],[100,50],[999,81],[50,50],[700,600]]");
        int count = countShips(sea, new int[]{1000, 1000}, new int[]{0, 0});
        log.info("" + count + "," + sea.finedList);
        Assert.assertEquals(5, count);
    }

    @Test
    public void test5() {
        //[[162,339],[613,308],[648,683]]
        //[1000,1000]
        //[0,0]
        Sea sea = new Sea("[[162,339],[613,308],[648,683]]");
        int count = countShips(sea, new int[]{1000, 1000}, new int[]{0, 0});
        log.info("" + count + "," + sea.finedList);
        Assert.assertEquals(3, count);
    }

    @Test
    public void test51() {
        //[[162,339],[613,308],[648,683]]
        //[1000,1000]
        //[0,0]
        Sea sea = new Sea("[[162,339],[613,308],[648,683]]");
        int count = countShips(sea, new int[]{200, 400}, new int[]{100, 300});
        log.info("" + count + "," + sea.finedList);
        Assert.assertEquals(1, count);
    }

    @Test
    public void test6() {
//    [[6,6],[100,50],[999,81],[50,50],[700,600]]
//    [1000,1000]
//    [0,0]
        Sea sea = new Sea("[[6,6],[100,50],[999,81],[50,50],[700,600]]");
        int count = countShips(sea, new int[]{1000, 1000}, new int[]{0, 0});
        log.info("" + count + "," + sea.finedList);
        Assert.assertEquals(5, count);
    }

    @Test
    public void test7() {
//        [[609,686],[83,651],[929,626],[584,143],[197,434]]
//        [1000,1000]
//        [0,0]
        Sea sea = new Sea("[[609,686],[83,651],[929,626],[584,143],[197,434]]");
        int count = countShips(sea, new int[]{1000, 1000}, new int[]{0, 0});
        log.info("" + count + "," + sea.finedList);
        Assert.assertEquals(5, count);
    }

}
