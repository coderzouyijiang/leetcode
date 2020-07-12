package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class Test5_reverseWords {

    class Solution1 {
        public String reverseWords(String s) {
            List<String> list = new ArrayList<>();
            String str = "";
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (ch == ' ') {
                    if (!str.isEmpty()) {
                        list.add(str);
                        str = "";
                    }
                } else {
                    str += ch;
                }
            }
            if (!str.isEmpty()) {
                list.add(str);
            }
            String result = "";
            for (int i = list.size() - 1; i >= 0; i--) {
                result += list.get(i);
                if (i > 0) {
                    result += " ";
                }
            }
            return result;
        }

    }

    class Solution {

        public String reverseWords(String s) {
            List<String> list = Arrays.asList(s.trim().split(" +"));
            Collections.reverse(list);
            return String.join(" ",list);
        }

    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals("blue is sky the", solution.reverseWords("the sky is blue"));
    }
}
