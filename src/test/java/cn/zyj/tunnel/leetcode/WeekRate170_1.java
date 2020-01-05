package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import scala.Int;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@RunWith(JUnit4.class)
public class WeekRate170_1 {

    public String freqAlphabets(String s) {
        String result = "";
        LinkedList<Integer> queue = new LinkedList<>();
        for (int i = 0; i <= s.length(); i++) {
            char ch;
            if (i < s.length() && (ch = s.charAt(i)) != '#') {
                queue.push(ch - '0');
                continue;
            }
            String str = "";
            if (i < s.length()) {
                int val = queue.pop() + queue.pop() * 10;
                str = (char) (val + 'a' - 1) + str;
            }
            Integer val;
            while ((val = queue.poll()) != null) {
                str = (char) (val + 'a' - 1) + str;
            }
            result += str;
        }
        return result;
    }

    @Test
    public void test() {
//        输入：s = "10#11#12"
//        输出："jkab"
//        解释："j" -> "10#" , "k" -> "11#" , "a" -> "1" , "b" -> "2".

//        Assert.assertEquals("jkab", freqAlphabets("10#11#12"));
        Assert.assertEquals("acz", freqAlphabets("1326#"));
    }

}
