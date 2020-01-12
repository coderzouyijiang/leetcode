package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate171_3 {

    /*
     用以太网线缆将 n 台计算机连接成一个网络，计算机的编号从 0 到 n-1。线缆用 connections 表示，
     其中 connections[i] = [a, b] 连接了计算机 a 和 b。
    网络中的任何一台计算机都可以通过网络直接或者间接访问同一个网络中其他任意一台计算机。
    给你这个计算机网络的初始布线 connections，你可以拔开任意两台直连计算机之间的线缆，并用它连接一对未直连的计算机。
    请你计算并返回使所有计算机都连通所需的最少操作次数。如果不可能，则返回 -1 。
     */
    public int makeConnected(int n, int[][] connections) {
        // 各个子网中冗余的线 >= 子网络数量-1
        boolean[][] connecteds = new boolean[n][n];
        int lineNum = 0;
        for (int[] conn : connections) {
            connecteds[conn[0]][conn[1]] = true;
            connecteds[conn[1]][conn[0]] = true;
            lineNum++;
        }
        return -1;
    }

    private void dfs(boolean[][] connecteds, boolean[] passed, int p) {
        boolean[] connected = connecteds[p];
        for (int i = 0; i < connected.length; i++) {

        }
    }

    @Test
    public void test() {

    }

}
