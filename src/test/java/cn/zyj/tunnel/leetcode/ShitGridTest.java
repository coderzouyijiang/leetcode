package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Stream;

@RunWith(JUnit4.class)
public class ShitGridTest {

    public int minTimeToVisitAllPoints(int[][] points) {
        if (points.length <= 1) return 0;
        int sumDist = 0;
//        int maxDist = 0;
        for (int i = 1; i < points.length; i++) {
            int[] p0 = points[i - 1];
            int[] p1 = points[i];
            int dist = Math.max(Math.abs(p1[0] - p0[0]), Math.abs(p1[1] - p0[1]));
            sumDist += dist;
//            maxDist = Math.max(maxDist, dist);
//            p0 = p1;
        }
        return sumDist;
    }

    @Test
    public void test() {
        int[][] points = new int[][]{{1, 1}, {3, 4}, {-1, 0}};
        Assert.assertEquals(7, minTimeToVisitAllPoints(points));
    }

    /**
     * 1 <= products.length <= 1000
     * 1 <= Σ products[i].length <= 2 * 10^4
     * products[i] 中所有的字符都是小写英文字母。
     * 1 <= searchWord.length <= 1000
     * searchWord 中所有字符都是小写英文字母。// 0-26
     *
     * @param products
     * @param searchWord
     * @return
     */
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        Arrays.sort(products);
        List<String> productList = new ArrayList<>(Arrays.asList(products));
        List<List<String>> result = new LinkedList<>();
        for (int i = 1; i <= searchWord.length(); i++) {
            String prefix = searchWord.substring(0, i);
            List<String> list = new LinkedList<>();
            Iterator<String> it = productList.iterator();
            innerLoop:
            while (it.hasNext()) {
                String product = it.next();
                if (product.startsWith(prefix)) {
                    list.add(product);
                    if (list.size() >= 3) break innerLoop;
                } else {
                    it.remove();
                }
            }
            result.add(list);
        }
        return result;
    }

    public List<List<String>> suggestedProducts2(String[] products, String searchWord) {
        Arrays.sort(products);
        Map<String, PriorityQueue<String>> map = new HashMap<>();
        int len = Stream.of(products).map(String::length).max(Integer::compare).get();
        for (int i = 1; i <= len; i++) { // 0,i
            for (String product : products) {
                if (i > product.length()) continue;
                map.computeIfAbsent(product.substring(0, i), k -> new PriorityQueue<>()).offer(product);
            }
        }
        List<List<String>> results = new LinkedList<>();
        for (int i = 1; i < searchWord.length(); i++) {
            List<String> result = new LinkedList<>();
            Queue<String> queue = map.get(searchWord.substring(0, i));
            if (queue != null) {
                if (!queue.isEmpty()) result.add(queue.poll());
                if (!queue.isEmpty()) result.add(queue.poll());
                if (!queue.isEmpty()) result.add(queue.poll());
            }
            results.add(result);
        }
        return results;
    }

}
