package cn.zyj.tunnel.leetcode;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Slf4j
@RunWith(JUnit4.class)
public class MaxSumDivThree {

    /*
    1 <= nums.length <= 4 * 10^4
    1 <= nums[i] <= 10^4
    */
    public int maxSumDivThree0(int[] nums) {
        int sum0 = 0;
        for (int num : nums) sum0 += num;
        if (sum0 % 3 == 0) return sum0;
        // 2^n-1 种组合
        Arrays.sort(nums);
//        List<Integer> sumList = new LinkedList<>();
        int len = 0;
        int[] sumArr = new int[1 << 8];
        sumArr[len++] = sum0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            int curLen = len;
            if (len == sumArr.length) {
                sumArr = Arrays.copyOf(sumArr, len * 2);
            }
            for (int j = 0; j < curLen; j++) {
                int sum = sumArr[j] - num;
                if (sum % 3 == 0) {
                    return sum;
                }
                sumArr[len++] = sum;
            }
        }
        return 0;
    }

    public static int sum(int[] nums) {
        int result = 0;
        for (int num : nums) result += num;
        return result;
    }

    public int maxSumDivThree1(int[] nums) {
        int sum0 = 0;
        for (int num : nums) sum0 += num;
        if (sum0 % 3 == 0) return sum0;
        // 2^n-1 种组合
        Arrays.sort(nums);
        return maxSumDivThree(nums, sum0, 0);
    }

    public int maxSumDivThree(int[] nums, int sum0, int index) {
        int sum = sum0 - nums[index];
        if (sum % 3 == 0) return sum;
        if (++index >= nums.length) return 0;
        int sum2 = maxSumDivThree(nums, sum0, index);
        if (sum2 > 0) return sum2;
        return maxSumDivThree(nums, sum, index);
    }

    public int maxSumDivThree(int[] nums) {
        PriorityQueue<Integer> queue1 = new PriorityQueue<>(Integer::compareTo);
        PriorityQueue<Integer> queue2 = new PriorityQueue<>(Integer::compareTo);
        int sum = 0;
        for (int num : nums) {
            int mod2 = num % 3;
            if (mod2 == 1) queue1.offer(num);
            else if (mod2 == 2) queue2.offer(num);
            sum += num;
        }
        List<Integer> list = new ArrayList<>(2);
        int mod = sum % 3;
        if (mod == 0) return sum;
        if (mod == 1) {
            if (queue1.size() >= 1) {
                list.add(queue1.poll());
            }
            if (queue2.size() >= 2) {
                list.add(queue2.poll() + queue2.poll());
            }
        } else {
            if (queue2.size() >= 1) {
                list.add(queue2.poll());
            }
            if (queue1.size() >= 2) {
                list.add(queue1.poll() + queue1.poll());
            }
        }
        if (list.size() > 0) {
            return sum - Collections.min(list);
        }
        return 0;
    }

    @Test
    public void test() {
        Assert.assertEquals(18, maxSumDivThree(new int[]{3, 6, 5, 1, 8}));
        Assert.assertEquals(0, maxSumDivThree(new int[]{4}));
        Assert.assertEquals(12, maxSumDivThree(new int[]{1, 2, 3, 4, 4}));
        Assert.assertEquals(15, maxSumDivThree(new int[]{2, 6, 2, 2, 7}));
    }

    public int maxSumDivThree3(int[] nums) {
        List<Integer> one = new ArrayList<>(); // 存放 mod3=1 的数
        List<Integer> two = new ArrayList<>(); // 存放 mod3=2 的数

        int sum = 0;
        for (int e : nums) {
            if (e % 3 == 1) one.add(e);
            if (e % 3 == 2) two.add(e);
            sum += e;
        }

        Collections.sort(one);
        Collections.sort(two);

        int res = sum % 3, ans = 0;

        if (res == 0) return sum;

        if (res == 1) {
            //剔除一个满足mod3==1的数， 或两个满足mod3==2的数
            if (two.size() >= 2) ans = Math.max(ans, sum - two.get(0) - two.get(1));
            if (one.size() >= 1) ans = Math.max(ans, sum - one.get(0));
        } else if (res == 2) {
            //剔除一个满足mod3==2的数， 或两个满足mod3==1的数
            if (two.size() > 0) ans = Math.max(ans, sum - two.get(0));
            if (one.size() >= 2) ans = Math.max(ans, sum - one.get(0) - one.get(1));
        }
        return ans;

    }

}
