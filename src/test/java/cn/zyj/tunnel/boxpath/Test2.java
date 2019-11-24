package cn.zyj.tunnel.boxpath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.PriorityQueue;

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
        System.out.println(finder.minPushBox(grid4));
    }

}
