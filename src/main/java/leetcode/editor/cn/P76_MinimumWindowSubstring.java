//给你一个字符串 S、一个字符串 T，请在字符串 S 里面找出：包含 T 所有字母的最小子串。 
//
// 示例： 
//
// 输入: S = "ADOBECODEBANC", T = "ABC"
//输出: "BANC" 
//
// 说明： 
//
// 
// 如果 S 中不存这样的子串，则返回空字符串 ""。 
// 如果 S 中存在这样的子串，我们保证它是唯一的答案。 
// 
// Related Topics 哈希表 双指针 字符串 Sliding Window

package leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Java：Minimum Window Substring
public class P76_MinimumWindowSubstring {
    public static void main(String[] args) {
        Solution solution = new P76_MinimumWindowSubstring().new Solution();
        // TO TEST
//        输入: S = "ADOBECODEBANC", T = "ABC"
//        输出: "BANC"
//        System.out.println("minWindow:" + solution.minWindow("ADOBECODEBANC", "ABC"));
//        System.out.println("minWindow:" + solution.minWindow("A", "A"));
//        System.out.println("minWindow:" + solution.minWindow("a", "a"));
//        System.out.println("minWindow:" + solution.minWindow("a", "aa"));
        System.out.println("minWindow:" + solution.minWindow("aaaaaaaaaaaabbbbbcdd", "abcdd"));
    }


    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public String minWindow0(String s, String t) {
            int[] chIds = new int[128]; // 字符->字符id
            Arrays.fill(chIds, -1);
            int chNum = 0;
            BitSet target = new BitSet();
            for (int i = 0; i < t.length(); i++) {
                char ch = t.charAt(i);
                if (chIds[ch] == -1) {
//                    target |= (chMasks[ch] = 1 << chNum++);
                    chIds[ch] = chNum;
                    target.set(chNum);
                    chNum++;
                }
            }
            if (s.length() < chNum) return "";
            String result = null;
            int m = s.length() - chNum;
            for (int i = 0; i <= m; i++) {
//                int state = 0;
                BitSet state = new BitSet(chNum);
                for (int j = i; j < s.length(); j++) {
//                    state |= chMasks[s.charAt(j)];
                    int chId = chIds[s.charAt(j)];
                    if (chId == -1) continue;
                    state.set(chId);
                    System.out.printf("[%s,%s]=%s,state=%s,len=%s\n", i, j, s.substring(i, j + 1), state, j + 1 - i);
                    if (!state.equals(target)) continue;
                    if (result == null || j + 1 - i < result.length()) {
                        result = s.substring(i, j + 1);
                    } else {
                        break;
                    }
                }
            }
            return result == null ? "" : result;
        }

        public String minWindow(String s, String t) {
            int[] chIds = new int[128]; // 字符->字符id
            Arrays.fill(chIds, -1);
            int chNum = 0;
            int[] target = new int[t.length()];
            for (int i = 0; i < t.length(); i++) {
                char ch = t.charAt(i);
                int chId = chIds[ch];
                if (chId == -1) {
                    chId = chNum++;
                    chIds[ch] = chId;
                }
                target[chId]++;
            }
            target = Arrays.copyOf(target, chNum);

            if (s.length() < chNum) return "";
            String result = null;
            int formed = 0;
            int[] state = new int[chNum];
            int m = s.length() - chNum;
            for (int i = 0; i <= m; i++) {

                for (int j = i; j < s.length(); j++) {
                    int chId = chIds[s.charAt(j)];
                    if (chId == -1) continue;
                    state[chId]++;
                    if (state[chId] >= target[chId]) {
                        formed++;
                    }
                    System.out.printf("[%s,%s]=%s,state=%s,len=%s\n"
                            , i, j, s.substring(i, j + 1), Arrays.toString(state), j + 1 - i);
                    if (formed < chNum) continue;
//                    if (!arrGtEq(state, target)) continue;
                    if (result == null || j + 1 - i < result.length()) {
                        result = s.substring(i, j + 1);
                    } else {
                        break;
                    }
                }

            }
            return result == null ? "" : result;
        }
    }

    private boolean arrGtEq(int[] state, int[] target) {
        for (int i = 0; i < state.length; i++) {
            if (state[i] < target[i]) return false;
        }
        return true;
    }
//leetcode submit region end(Prohibit modification and deletion)

}