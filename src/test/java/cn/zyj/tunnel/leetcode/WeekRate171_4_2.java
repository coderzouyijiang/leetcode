package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Comparator;
import java.util.PriorityQueue;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate171_4_2 {

    private static final int[][] distArr = new int[26][26];

    static {
        for (int i = 0; i < 26; i++) {
            int x1 = i % 6;
            int y1 = i / 6;
            for (int j = i + 1; j < 26; j++) {
                int x2 = j % 6;
                int y2 = j / 6;
                int dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);
                distArr[i][j] = dist;
                distArr[j][i] = dist;
            }
//            System.out.println(Arrays.toString(distArr[i]));
        }
    }

    public int minimumDistance(String word) {
        if (word.length() <= 2) return 0;
        // 把word分成2个集合l和r
        // 使 sum_i(distArr[l[i]][l[i+1]])+sum_j(distArr[r[j]][r[j+1]]) 最小
        // 同一字母分配给同一集合 !!!!!!!!!!!! 复杂度2^26


        return -1;
    }

    /**
     * @param word
     * @param chGroup 字母所在分组索引
     * @return
     */
    private int computeDist(String word, int[] chGroup) {
        int count = 0;
        int[] chArr = {-1, -1};
        for (int i = 0; i < word.length(); i++) {
            int ch = word.charAt(i) - 'A';
            int group = chGroup[ch];
            int curCh = chArr[group];
            if (curCh >= 0) {
                count += distArr[curCh][ch];
            }
            chArr[group] = ch;
        }
        return count;
    }

    @Test
    public void test() {
        System.out.println("" + minimumDistance("HAPPY")); // 6
    }

    @Test
    public void test2() {
        int[] chGroup = new int[26];
        chGroup['H' - 'A'] = 0;
        chGroup['A' - 'A'] = 0;
        chGroup['P' - 'A'] = 1;
        chGroup['P' - 'A'] = 1;
        chGroup['Y' - 'A'] = 0;
        System.out.println("" + computeDist("HAPPY", chGroup)); // 6
    }
}
