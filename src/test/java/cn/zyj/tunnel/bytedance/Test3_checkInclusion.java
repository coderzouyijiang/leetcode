package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import scala.Char;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RunWith(JUnit4.class)
public class Test3_checkInclusion {

    class Solution {
        public boolean checkInclusion(String s1, String s2) {
            boolean[] arr = new boolean[26];
            int size = 0;
            for (int i = 0; i < s2.length(); i++) {
                int id = s2.charAt(i) - 'a';
                if (!arr[id]) {
                    arr[id] = true;
                    size++;
                }
            }

            boolean[] arr2 = Arrays.copyOf(arr, arr.length);
            int size2 = size;
            for (int i = 0; i < s1.length(); i++) {
                int id = s1.charAt(i) - 'a';
                if (arr2[id]) {
                    arr2[id] = false;
                    size2--;
                    if (size2 == 0) {
                        return true;
                    }
                } else {

                }
            }
            return false;
        }
    }

    @Test
    public void test() {
        Solution solution = new Solution();
        log.info("");
    }
}
