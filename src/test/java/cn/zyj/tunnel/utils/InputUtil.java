package cn.zyj.tunnel.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class InputUtil {

    public static int[][] parseIntGrid(String input) {
        JSONArray arr = JSONObject.parseArray(input);
        int[][] grid = new int[arr.size()][];
        for (int i = 0; i < arr.size(); i++) {
            JSONArray arr2 = arr.getJSONArray(i);
            int[] line = new int[arr2.size()];
            for (int j = 0; j < arr2.size(); j++) {
                line[j] = arr2.getIntValue(j);
            }
            grid[i] = line;
        }
        return grid;
    }

    public static int[] parseIntArray(String str) {
        List<Integer> list = JSON.parseArray(str, Integer.class);
        int[] arr = new int[list.size()];
        int i = 0;
        for (Integer x : list) arr[i++] = x;
        return arr;
    }

}
