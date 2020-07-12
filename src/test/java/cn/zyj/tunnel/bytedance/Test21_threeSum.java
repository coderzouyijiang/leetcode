package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class Test21_threeSum {

    class Solution {

        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);
            List<List<Integer>> result = new LinkedList<>();
            for (int i = 0; i < nums.length - 2; i++) {
                int a = nums[i];
                if (i > 0 && a == nums[i - 1]) continue;
                if (a > 0) break;
                for (int j = i + 1; j < nums.length - 1; j++) {
                    int b = nums[j];
                    if (j > i + 1 && b == nums[j - 1]) continue;
                    int c = -(a + b);
                    if (c < nums[j + 1]) continue;
                    if (Arrays.binarySearch(nums, j + 1, nums.length, c) > -1) {
                        result.add(Arrays.asList(a, b, c));
                    }
                }
            }
            return result;
        }

    }

    @Test
    public void test() {
        Solution solution = new Solution();
        log.info("");
    }
}
