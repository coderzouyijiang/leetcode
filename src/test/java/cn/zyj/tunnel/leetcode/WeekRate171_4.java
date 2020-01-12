package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate171_4 {

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
        // 分成2个集合按顺序运动
        // HAPPY
        final int left = 0, right = 1, dist = 2;
        int[] start = {-1, -1, 0};
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(it -> it[dist]));
        queue.offer(start);
        int[] state;
        while ((state = queue.poll()) != null) {
            int l = state[left];
            int r = state[right];
            int d = state[dist];
//            System.out.printf("%s,%s,%s\n", Arrays.toString(state), (char) (l + 'A'), (char) (r + 'A'));
            int next = Math.max(l, r) + 1;
            if (next == word.length()) {
                return d;
            }
            int nextCh = word.charAt(next) - 'A';
            int dl = l < 0 ? 0 : distArr[word.charAt(l) - 'A'][nextCh];
            queue.offer(new int[]{next, r, d + dl});
            int dr = r < 0 ? 0 : distArr[word.charAt(r) - 'A'][nextCh];
            queue.offer(new int[]{l, next, d + dr});
        }
        return -1;
    }

    @Test
    public void test() {
        System.out.println("" + minimumDistance("HAPPY")); // 6
    }

}
