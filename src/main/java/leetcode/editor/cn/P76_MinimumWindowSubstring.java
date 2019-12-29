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

//Java：Minimum Window Substring
public class P76_MinimumWindowSubstring {
    public static void main(String[] args) {
        Solution solution = new P76_MinimumWindowSubstring().new Solution();
        // TO TEST
//        输入: S = "ADOBECODEBANC", T = "ABC"
//        输出: "BANC"
        System.out.println("minWindow:" + solution.minWindow("ADOBECODEBANC", "ABC"));
    }


    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public String minWindow(String s, String t) {
            int[] chMasks = new int['Z' + 1]; // 字符->字符id
            int chNum = 0;
            int target = 0;
            for (int i = 0; i < t.length(); i++) {
                char ch = t.charAt(i);
                if (chMasks[ch] == 0) {
                    target |= (chMasks[ch] = 1 << chNum++);
                }
            }
            if (s.length() < chNum) return "";
            String result = null;
            int m = s.length() - chNum;
            for (int i = 0; i < m; i++) {
                int state = 0;
                for (int j = i; j < s.length(); j++) {
                    state |= chMasks[s.charAt(j)];
                    System.out.printf("[%s,%s]=%s,state=%s,len=%s\n", i, j, s.substring(i, j + 1), state, j + 1 - i);
                    if (state == target) {
                        if (result == null || j + 1 - i < result.length()) {
                            result = s.substring(i, j + 1);
                        } else {
                            break;
                        }
                    }
                }
            }
            return result == null ? "" : result;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}