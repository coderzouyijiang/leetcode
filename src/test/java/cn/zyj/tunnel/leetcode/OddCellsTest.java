package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RunWith(JUnit4.class)
public class OddCellsTest {

    public int oddCells(int n, int m, int[][] indices) {
        int[][] count = new int[n][m];
        for (int[] pos : indices) {
            int r = pos[0];
            int c = pos[1];
            for (int i = 0; i < n; i++) {
                count[i][c]++;
            }
            for (int i = 0; i < m; i++) {
                count[r][i]++;
            }
//            count[r][c]--;
        }
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sum += count[i][j] % 2;
            }
        }
        return sum;
    }

    public int oddCells2(int n, int m, int[][] indices) {
        int[] rs = new int[n];
        int[] cs = new int[m];
        for (int[] pos : indices) {
            rs[pos[0]]++;
            cs[pos[1]]++;
        }
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sum += (rs[i] + cs[j]) % 2;
            }
        }
        return sum;
    }

    public int oddCells4(int n, int m, int[][] indices) {
        BitSet rs = new BitSet(n);
        BitSet cs = new BitSet(m);
        for (int[] pos : indices) {
            rs.flip(pos[0]);
            cs.flip(pos[1]);
        }
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sum += (rs.get(i) ^ cs.get(j)) ? 1 : 0;
            }
        }
        return sum;
    }

    public int oddCells3(int n, int m, int[][] indices) {
        BitSet rs = new BitSet(n);
        BitSet cs = new BitSet(m);
        int sum = 0;
        for (int[] pos : indices) {
            int r = pos[0];
            int c = pos[1];
            boolean r2 = !rs.get(r);
            boolean c2 = !cs.get(c);
            rs.set(r, r2);
            cs.set(c, c2);
            if (r2 && c2) {
                sum += (m + n - 1);
            } else if (r2 && !r2) {
                sum += (-m + n);
            } else if (!r2 && c2) {
                sum += (m - n);
            } else {
                sum -= (m + n - 1);
            }
        }
        return sum;
    }

    @Test
    public void test() {
        Assert.assertEquals(6, oddCells3(2, 3, new int[][]{{0, 1}, {1, 1},}));
    }

    public List<List<Integer>> reconstructMatrix2(int upper, int lower, int[] colsum) {
        int[] rowsum = {upper, lower};
        List<List<Integer>> result = new ArrayList<>(2);
        List<Integer> list1 = new ArrayList<>(colsum.length);
        for (int i = 0; i < colsum.length; i++) {
            if (colsum[i] == 2) {
                if (upper <= 0 || lower <= 0) {
                    return Collections.EMPTY_LIST;
                }
                list1.add(1);
                colsum[i] -= 2;
                rowsum[0]--;
                rowsum[1]--;
            } else {
                list1.add(0);
            }
        }
        result.add(list1);
        result.add(new ArrayList<>(list1));

        for (int i = 0; i < colsum.length; i++) {
            loop:
            for (int j = 0; j < result.size(); j++) {
                List<Integer> list = result.get(j);
                if (list.get(i) == 0 && colsum[i] == 1 && rowsum[j] > 0) {
                    colsum[i]--;
                    list.set(i, 1);
                    rowsum[j]--;
                    break loop;
                }
            }
            if (colsum[i] > 0) {
                return Collections.EMPTY_LIST;
            }
        }
        if (rowsum[0] != 0 || rowsum[1] != 0) {
            return Collections.EMPTY_LIST;
        }
        return result;
    }

    public List<List<Integer>> reconstructMatrix(int upper, int lower, int[] rowSum, int[] colsum) {
        List<List<Integer>> result = new ArrayList<>(rowSum.length);
        for (int i = 0; i < rowSum.length; i++) {
            result.add(new ArrayList(colsum.length));
        }
        for (int v = upper; v >= lower; v--) {

        }
        for (int i = 0; i < colsum.length; i++) {
            for (int j = 0; j < rowSum.length; j++) {
                if (colsum[i] > 0 && rowSum[j] > 0) {
                    colsum[i]--;
                    rowSum[j]--;
                    result.get(j).add(1);
                } else {
                    result.get(j).add(0);
                }
            }
            if (colsum[i] > 0) {
                return Collections.EMPTY_LIST;
            }
        }
        for (int i = 0; i < rowSum.length; i++) {
            if (rowSum[i] > 0) {
                return Collections.EMPTY_LIST;
            }
        }
        return result;
    }

    public List<List<Integer>> reconstructMatrix(int upper, int lower, int[] colsum) {
        int[] rowsum = {upper, lower};
        return reconstructMatrix(rowsum, colsum);
    }

    public List<List<Integer>> reconstructMatrix(int[] rowsum, int[] colsum) {
        PriorityQueue<int[]> colsumQueue = new PriorityQueue<>((arr1, arr2) -> Integer.compare(arr2[1], arr1[1]));
        BitSet[] bitSets = new BitSet[rowsum.length];
        for (int i = 0; i < rowsum.length; i++) {
            bitSets[i] = new BitSet(colsum.length);
        }
        for (int i = 0; i < colsum.length; i++) {
            colsumQueue.offer(new int[]{i, colsum[i]});
        }
        for (int[] pair; (pair = colsumQueue.poll()) != null; ) {
            int i = pair[0];
            int sum = pair[1];
            for (int j = 0; j < rowsum.length && sum > 0; j++) {
                if (rowsum[j] <= 0) continue;
                bitSets[j].set(i, true);
                rowsum[j]--;
                sum--;
            }
            if (sum > 0) {
                return Collections.EMPTY_LIST;
            }
        }
        for (int sum : rowsum) {
            if (sum != 0) {
                return Collections.EMPTY_LIST;
            }
        }
        return Stream.of(bitSets)
                .map(it -> it.stream().mapToObj(Integer::valueOf).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Test
    public void test2() {
        //   1 1 1
        // 2 1 1
        // 1     1
        log.info("{}", reconstructMatrix(2, 1, new int[]{1, 1, 1}));
//        5
//        5
//                [2,1,2,0,1,0,1,2,0,1]
//        log.info("{}", reconstructMatrix(5, 5, new int[]{2, 1, 2, 0, 1, 0, 1, 2, 0, 1}));
//        1
//        4
//                [2,1,2,0,0,2]
//        log.info("{}", reconstructMatrix(1, 4, new int[]{2, 1, 2, 0, 0, 2}));
    }

}

