package cn.zyj.tunnel.boxpath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

@RunWith(JUnit4.class)
public class Test2 {

    @Test
    public void test_priorityQueue() {

        PriorityQueue<int[]> stateQueue = new PriorityQueue(BoxPathFinder2.stateComparator);
        stateQueue.add(new int[]{11, 1, 2, 4, 5});
        stateQueue.add(new int[]{56, 1, 2, 4, 5});
        stateQueue.add(new int[]{2, 1, 2, 4, 5});
        stateQueue.add(new int[]{22, 1, 2, 4, 5});
        stateQueue.add(new int[]{43, 1, 2, 4, 5});

        int[] state;
        while ((state = stateQueue.poll()) != null) {
            System.out.println(Arrays.toString(state));
        }
    }

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
        BoxPathFinder2 finder = new BoxPathFinder2();
        List<int[]> path = finder.minPushBoxPath(grid4);
        BoxPathFinder2.printPath(grid4, path);
    }

    @Test
    public void test_isConnect() {
//        boolean connect = BoxPathFinder2.isConnect(grid4, 4, 7, 1, 6);
//        System.out.println(connect);

        boolean connect2 = BoxPathFinder2.isConnect(grid4, 3, 4, 2, 4, 5, 3);
        System.out.println(connect2);

        System.out.println(BoxPathFinder2.computeStateHash(new int[]{1, 2, 3, 4}));
        System.out.println(BoxPathFinder2.computeStateHash(new int[]{1, 2, 3, 5}));
    }

    @Test
    public void test_set() {
        Set<int[]> set = new HashSet<>();
        System.out.println(set.add(new int[]{1, 2}));
        System.out.println(set.add(new int[]{3, 4}));
        System.out.println(set.add(new int[]{3, 4}));
        System.out.println(set.add(new int[]{1, 2}));
        System.out.println(Integer.toString(100, 2));
        System.out.println(Integer.toBinaryString(100));
    }

}
