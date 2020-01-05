package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate170_3 {

    /*
    有 n 个人，每个人都有一个  0 到 n-1 的唯一 id 。
    给你数组 watchedVideos  和 friends ，其中 watchedVideos[i]  和 friends[i] 分别表示 id = i 的人观看过的视频列表和他的好友列表。
    Level 1 的视频包含所有你好友观看过的视频，level 2 的视频包含所有你好友的好友观看过的视频，以此类推。
    一般的，Level 为 k 的视频包含所有从你出发，最短距离为 k 的好友观看过的视频。
    给定你的 id  和一个 level 值，请你找出所有指定 level 的视频，并将它们按观看频率升序返回。
    如果有频率相同的视频，请将它们按名字字典序从小到大排列。
     */
    public List<String> watchedVideosByFriends(List<List<String>> watchedVideos, int[][] friends, int id, int level) {
        Map<String, Integer> map = new HashMap<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{id, 0});
        boolean[] passed = new boolean[friends.length];
        int[] state;
        while ((state = queue.poll()) != null) {
            int curId = state[0];
            if (passed[curId]) continue;
            passed[curId] = true;
            int curLevel = state[1];
            if (curLevel == level) {
                List<String> strs = watchedVideos.get(curId);
                for (String str : strs) {
                    map.put(str, map.getOrDefault(str, 0) + 1);
                }
                continue;
            }
            int[] ids = friends[curId];
            int nextLevel = curLevel + 1;
            for (int nextId : ids) {
                queue.offer(new int[]{nextId, nextLevel});
            }
        }
        List<String> list = map.entrySet().stream()
                .sorted((a, b) -> {
                    int compare = Integer.compare(a.getValue(), b.getValue());
                    if (compare != 0) return compare;
                    return a.getKey().compareTo(b.getKey());
                })
                .map(Map.Entry::getKey).collect(Collectors.toList());
        return list;
    }

    @Test
    public void test() {
//        输入：s = "10#11#12"
//        输出：""
//        解释："j" -> "10#" , "k" -> "11#" , "a" -> "1" , "b" -> "2".

//        Assert.assertEquals("jkab", freqAlphabets("10#11#12"));
    }

}
