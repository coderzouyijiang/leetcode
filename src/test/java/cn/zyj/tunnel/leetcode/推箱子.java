package cn.zyj.tunnel.leetcode;

import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
            return Objects.hashCode(i, j);
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
            return Objects.equal(from, move.from) &&
                    Objects.equal(to, move.to);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(from, to);
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
    public List<Pos> searchNextPos(char[][] grid, Pos p, Pos from) {
        List<Pos> list = new ArrayList<>(4);
        final Pos p1 = getValidPos(grid, p.i, p.j + 1);
        if (p1 != null) {
            final Pos p2 = getValidPos(grid, p.i, p.j - 1);
            if (p2 != null) {
                findNextPos(list, from, p1, p2);
            }
        }
        final Pos p3 = getValidPos(grid, p.i + 1, p.j);
        if (p3 != null) {
            final Pos p4 = getValidPos(grid, p.i - 1, p.j);
            if (p4 != null) {
                findNextPos(list, from, p3, p4);
            }
        }
        return list;
    }

    private void findNextPos(List<Pos> nextPosList, Pos last, Pos p1, Pos p2) {
        if (p1.equals(last)) {
            nextPosList.add(p2);
        } else if (p2.equals(last)) {
            nextPosList.add(p1);
        } else {
            nextPosList.add(p1);
            nextPosList.add(p2);
        }
    }

    /*
    // 搜索能推箱子的位置
    public List<Pos> searchPushPos(char[][] grid, Pos box, Pos player) {
        final List<Pos> posList = searchGroundPos(grid, box);
        for (int i = 0; i < posList.size(); i += 2) {
            final Pos p1 = posList.get(i);
            final Pos p2 = posList.get(i + 1);
        }
    }
    */

    public int minPushBox(char[][] grid) {
        final Pos player = searchPos(grid, PLAYER);
        grid[player.i][player.j] = BLANK;

        final Pos box = searchPos(grid, BOX);
        final Pos target = searchPos(grid, TARGET);


        return -1;
    }

    public void searchPath(char[][] grid, List<Move> path, Pos target, List<List<Pos>> paths) {
        final Move move = path.get(path.size() - 1);
        final Pos p = move.to;
        if (p.equals(target)) {
            paths.add(path.stream().map(it -> it.to).collect(Collectors.toList()));
            return;
        }
        final Pos last = move.from;
//        System.out.println("\n" + path);
//        System.out.println(gridToStr(grid, p, target));
        final List<Pos> nextPosList = searchNextPos(grid, p, last);
        for (Pos nextPos : nextPosList) {
            final Move nextMove = new Move(p, nextPos);
            if (path.contains(nextMove)) {
                continue;
            }
            final List<Move> nextPath = new ArrayList<>(path);
            nextPath.add(nextMove);
            searchPath(grid, nextPath, target, paths);
        }
    }

    private String gridToStr(char[][] grid, Pos box, Pos target) {
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
                if (target != null && i == target.i && j == target.j) {
                    str += TARGET;
                } else if (box != null && i == box.i && j == box.j) {
                    str += BOX;
                } else if (val == BOX) {
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

    /**
     * 1.求出T-B之间所有可能路径集合PS,PS中每个元素是P,P中包含路径点{p0,p1,..pn}
     * 2.对于一个P，每一步移动 Pn -> Pn+1 ,设与移动方向相反的位置是Sn,必须是空的（留给player)
     * 3.每一次移动,player由Sn-1移动到Pn,Pn和Sn+1必须是连通的(墙和箱子不可穿过)
     */
    @Test
    public void test() {
        Assert.assertEquals(-1, minPushBox(grid1));
        Assert.assertEquals(5, minPushBox(grid2));
        Assert.assertEquals(-1, minPushBox(grid3));
    }

    @Test
    public void test_searchPath() {
        final List<char[][]> grids = Arrays.asList(grid1, grid2, grid3, grid4);
        for (char[][] grid : grids) {
            System.out.println("------------------------------------------\n");
            final Pos box = searchPos(grid, BOX);
            final Pos target = searchPos(grid, TARGET);
            final Pos player = searchPos(grid, PLAYER);

            System.out.println(gridToStr(grid, box, target));
            List<List<Pos>> paths = new LinkedList<>();
            searchPath(grid, Arrays.asList(new Move(null, box)), target, paths);
            System.out.println("\nresult:");
            for (List<Pos> path : paths) {
                System.out.println("" + path);
            }
        }
    }
}
