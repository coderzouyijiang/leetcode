package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RunWith(JUnit4.class)
public class 推箱子 {

    // 坐标
    private static class Pos {
        public final int i;
        public final int j;

        public Pos(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return "(" + i + "," + j + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos pos = (Pos) o;
            return i == pos.i &&
                    j == pos.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }
    }

    // 一次移动
    private static class Move {
        public final Pos from;
        public final Pos to;

        public Move(Pos from, Pos to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Move move = (Move) o;
            return Objects.equals(from, move.from) &&
                    Objects.equals(to, move.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public String toString() {
            return to.toString();
        }
    }

    private static final char BOX = 'B';
    private static final char PLAYER = 'S';
    private static final char TARGET = 'T';
    private static final char BLANK = '.';
    private static final char WALL = '#';

    private static final List<Pos> stepList = Arrays.asList(
            new Pos(1, 0), new Pos(-1, 0), new Pos(0, 1), new Pos(0, -1));

    public Pos searchPos(char[][] grid, char x) {
        for (int i = 0; i < grid.length; i++) {
            final char[] line = grid[i];
            for (int j = 0; j < line.length; j++) {
                if (line[j] == x) {
//                    grid[i][j] = BLANK;
                    return new Pos(i, j);
                }
            }
        }
        return null;
    }

    // 搜索周围空地
    public Pos getValidPos(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) return null;
        final char val = grid[i][j];
        if (val == WALL) return null;
        return new Pos(i, j);
    }

    // 搜索下一步可行的位置
    public List<Pos> searchNextStep(char[][] grid, Pos p) {
        List<Pos> list = new LinkedList<>();
        for (int i = 0; i < stepList.size(); i += 2) {
            final Pos step1 = stepList.get(i);
            final Pos step2 = stepList.get(i + 1);
            final Pos p1 = getValidPos(grid, p.i + step1.i, p.j + step1.j);
            if (p1 != null) {
                final Pos p2 = getValidPos(grid, p.i + step2.i, p.j + step2.j);
                if (p2 != null) {
                    list.add(p1);
                    list.add(p2);
                }
            }
        }
        return list;
    }

    /**
     * 1.求出T-B之间所有可能路径集合PS,PS中每个元素是P,P中包含路径点{p0,p1,..pn}
     * 2.对于一个P，每一步移动 Pn -> Pn+1 ,设与移动方向相反的位置是Sn,必须是空的（留给player)
     * 3.每一次移动,player由Sn-1移动到Pn,Pn和Sn+1必须是连通的(墙和箱子不可穿过)
     */
    public void searchPath(char[][] grid, Move move, LinkedHashSet<Move> path, Pos target, List<List<Pos>> paths) {
        path.add(move);
        final Pos p = move.to;
        if (p.equals(target)) {
            paths.add(path.stream().map(it -> it.to).collect(Collectors.toList()));
            return;
        }
        // player在last
        final Pos player = move.from;
        System.out.println("\n" + path);
        System.out.println(gridToStr(grid, p, player));
        final List<Pos> nextPosList = searchNextStep(grid, p);
        // 按与终点直线距离排序
        Collections.sort(nextPosList, Comparator.comparingInt(it -> computeDistance(it, target)));
        for (Pos nextPos : nextPosList) {
            final Move nextMove = new Move(p, nextPos);
            if (path.contains(nextMove)) {
                continue;
            }
            final Pos nextPlayer = new Pos(p.i - (nextPos.i - p.i), p.j - (nextPos.j - p.j));
            if (!isConnect(grid, player, nextPlayer, p, new HashSet<>())) {
                continue;
            }
            searchPath(grid, nextMove, new LinkedHashSet(path), target, paths);
        }
    }

    private int computeDistance(Pos p1, Pos p2) {
        return (p2.i - p1.i) * (p2.i - p1.i) + (p2.j - p1.j) * (p2.j - p1.j);
    }

    private boolean isConnect(char[][] grid, Pos p, Pos target, Pos box, Set<Pos> path) {
//        log.info("" + path);
        for (Pos step : stepList) {
            final Pos p2 = getValidPos(grid, p.i + step.i, p.j + step.j);
//            log.info("" + path + ":" + p2);
            if (p2 == null || p2.equals(box) || path.contains(p2)) {
                continue;
            }
            if (p2.equals(target)) {
                return true;
            }
            final HashSet<Pos> path2 = new HashSet<>(path);
            path2.add(p2);
            if (isConnect(grid, p2, target, box, path2)) {
                return true;
            }
        }
        return false;
    }

    public int minPushBox(char[][] grid) {
        final Pos box = searchPos(grid, BOX);
        final Pos target = searchPos(grid, TARGET);
        final Pos player = searchPos(grid, PLAYER);

        List<List<Pos>> paths = new LinkedList<>();
        final Move firstMove = new Move(player, box);
        LinkedHashSet<Move> firstPath = new LinkedHashSet<>();
        searchPath(grid, firstMove, firstPath, target, paths);
        final List<Pos> minPath = paths.stream().min(Comparator.comparingInt(List::size)).orElse(null);
        return minPath != null ? minPath.size() - 1 : -1;
    }

    // 打印地图
    private String gridToStr(char[][] grid, Pos box, Pos player) {
        String str = "";
        for (int i = 0; i < grid.length; i++) {
            if (i > 0) {
                str += "\n";
            }
            final char[] line = grid[i];
            for (int j = 0; j < line.length; j++) {
                if (j > 0) {
                    str += " ";
                }
                final char val = line[j];
                if (player != null && i == player.i && j == player.j) {
                    str += PLAYER;
                } else if (box != null && i == box.i && j == box.j) {
                    str += BOX;
                } else if (val == BOX || val == PLAYER) {
                    str += BLANK;
                } else {
                    str += val;
                }
            }
        }
        return str;
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
            {'#', '#', '#', '#', '#', '#'},
            {'#', 'T', '.', '.', '.', '#'},
            {'#', '.', '#', 'B', '.', '#'},
            {'#', '.', '.', '.', '.', '#'},
            {'#', '.', '.', '.', 'S', '#'},
            {'#', '#', '#', '#', '#', '#'}};

    char[][] grid5 = {
            {'.', '.', '#', '.', '.', '.', '.', '#'}
            , {'.', 'B', '.', '.', '.', '.', '.', '#'}
            , {'.', '.', 'S', '.', '.', '.', '.', '.'}
            , {'.', '#', '.', '.', '.', '.', '.', '.'}
            , {'.', '.', '.', '.', '.', '.', '.', '.'}
            , {'.', '.', '.', 'T', '.', '.', '.', '.'}
            , {'.', '.', '.', '.', '.', '.', '.', '#'}
            , {'.', '#', '.', '.', '.', '.', '.', '.'}};

    @Test
    public void test() {
        Assert.assertEquals(3, minPushBox(grid1));
        Assert.assertEquals(5, minPushBox(grid2));
        Assert.assertEquals(-1, minPushBox(grid3));
    }

    @Test
    public void test_searchPath() {
//        final List<char[][]> grids = Arrays.asList(grid1, grid2, grid3, grid4, grid5);
//        final List<char[][]> grids = Arrays.asList(grid1, grid2, grid3, grid4);
        final List<char[][]> grids = new LinkedList<>();
        grids.add(grid5);
        for (char[][] grid : grids) {
            System.out.println("------------------------------------------\n");
            final Pos box = searchPos(grid, BOX);
            final Pos target = searchPos(grid, TARGET);
            final Pos player = searchPos(grid, PLAYER);

            System.out.println(gridToStr(grid, box, player));
            List<List<Pos>> paths = new LinkedList<>();
            final Move firstMove = new Move(player, box);
            LinkedHashSet<Move> firstPath = new LinkedHashSet<>();
            searchPath(grid, firstMove, firstPath, target, paths);
            System.out.println("\nresult:");
            for (List<Pos> path : paths) {
                System.out.println("" + path + "," + (path.size() - 1));
            }
        }
    }

    @Test
    public void test_isConnect() {
        final Pos box = searchPos(grid1, BOX);
        final Pos target = searchPos(grid1, TARGET);
        final Pos player = searchPos(grid1, PLAYER);
        System.out.println(gridToStr(grid1, box, player));
        final boolean isConnect = isConnect(grid1, player, new Pos(2, 4), box, new LinkedHashSet<>());
        log.info("");
    }

}
