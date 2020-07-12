package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class Test23_search {

    /*
    输入: nums = [4,5,6,7,0,1,2], target = 0
    输出: 4
     */
    class Solution {

        public int search(int[] nums, int target) {
            if (nums == null || nums.length == 0) return -1;
            int i = 0, j = nums.length - 1;
            while (i <= j) {
                int mid = i + (j - i) / 2;
                int midVal = nums[mid];
                if (midVal == target) {
                    return mid;
                }
                int left = nums[i];
                int right = nums[j];
                log.info("计算前，target={},i={},j={},mid={},left={},right={},midVal={}", target, i, j, mid, left, right, midVal);
                if (right < left && target >= left && midVal <= right) {
                    j = mid - 1;
                } else if (right < left && target <= right && midVal >= left) {
                    i = mid + 1;
                } else {
                    if (target < midVal) {
                        j = mid - 1;
                    } else {
                        i = mid + 1;
                    }
                }
                log.info("计算后，target={},i={},j={},mid={},left={},right={},midVal={}", target, i, j, mid, left, right, midVal);
            }
            return -1;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(4, solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
        Assert.assertEquals(1, solution.search(new int[]{5, 1, 2, 3, 4}, 1));
        Assert.assertEquals(2, solution.search(new int[]{5, 6, 1, 2, 3, 4}, 1));
        Assert.assertEquals(-1, solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
    }
}
