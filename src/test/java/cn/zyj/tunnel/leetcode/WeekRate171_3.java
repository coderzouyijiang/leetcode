package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public int makeConnected0(int n, int[][] connections) {
        Set<Set<Integer>> childNets = new HashSet<>();
        for (int[] conn : connections) {
            int p1 = conn[0];
            int p2 = conn[1];
            Set<Integer> net1 = null;
            Set<Integer> net2 = null;
            for (Set<Integer> childNet : childNets) {
                if (childNet.contains(p1)) {
                    net1 = childNet;
                } else if (childNet.contains(p2)) {
                    net2 = childNet;
                } else if (net1 != null && net2 != null) {
                    break;
                }
            }
            if (net1 != null && net2 != null) {
                if (net1 != net2) {
                    net1.addAll(net2);
                }
                childNets.remove(net2);
            } else if (net1 != null) {
                net1.add(p2);
            } else if (net2 != null) {
                net2.add(p1);
            } else {
                Set<Integer> net3 = new HashSet<>();
                net3.add(p1);
                net3.add(p2);
                childNets.add(net3);
            }
        }
        if (childNets.size() == 1) {
            return 0;
        }

        return -1;
    }

    public int makeConnected(int n, int[][] connections) {
        boolean[][] connected = new boolean[n][n];
        for (int[] conn : connections) {
            connected[conn[0]][conn[1]] = true;
            connected[conn[1]][conn[0]] = true;
        }
        boolean[] passed = new boolean[n];
        passed[0] = true;
        for (int i = 0; i < n; i++) {
            passed[i] = connected[0][i];
        }
        for (int i = 0; i < n; i++) {
//            connected[i];

        }
        return 0;
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
