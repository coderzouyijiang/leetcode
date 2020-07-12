package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RunWith(JUnit4.class)
public class Test1_lengthOfLongestSubstring {

    class Solution {
        public int lengthOfLongestSubstring(String s) {
            Map<Character, Integer> ch2Index = new LinkedHashMap<>();
            int start = 0; // 避免map的remove操作
            int maxLength = 0;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                Integer preIndex = ch2Index.get(ch);
//                log.info("i={},ch={},start={},maxLength={},preIndex={},ch2Index={}", i, ch, start, maxLength, preIndex, ch2Index);
                ch2Index.put(ch, i);
                if (preIndex == null || preIndex < start) {
                    maxLength = Math.max(i - start + 1, maxLength);
                } else {
                    start = preIndex + 1;
                }
            }
            return maxLength;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(3, solution.lengthOfLongestSubstring("abcabcbb"));
        Assert.assertEquals(1, solution.lengthOfLongestSubstring("bbbbb"));
        Assert.assertEquals(3, solution.lengthOfLongestSubstring("pwwkew"));
    }
}
