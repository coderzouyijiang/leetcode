//给定一组非负整数，重新排列它们的顺序使之组成一个最大的整数。 
//
// 示例 1: 
//
// 输入: [10,2]
//输出: 210 
//
// 示例 2: 
//
// 输入: [3,30,34,5,9]
//输出: 9534330 
//
// 说明: 输出结果可能非常大，所以你需要返回一个字符串而不是整数。 
// Related Topics 排序

package leetcode.editor.cn;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

//Java：Largest Number
@Slf4j
public class P179_LargestNumber {
    public static void main(String[] args) {
        Solution solution = new P179_LargestNumber().new Solution();
        // TO TEST
//        String result = solution.largestNumber(new int[]{3, 30, 34, 5, 9});
//        String result = solution.largestNumber(new int[]{121, 12});
        String result = solution.largestNumber(new int[]{0, 0});
        log.info(result);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public String largestNumber(int[] nums) {
            List<String> list = new ArrayList<>(nums.length);
            for (int num : nums) {
                list.add(Integer.toString(num, 10));
            }
            list.sort((str1, str2) -> {
                int len = str1.length() + str2.length();
                for (int i = 0; i < len; i++) {
                    char ch1 = i < str1.length() ? str1.charAt(i) : str2.charAt(i - str1.length());
                    char ch2 = i < str2.length() ? str2.charAt(i) : str1.charAt(i - str2.length());
                    if (ch2 > ch1) {
                        return 1;
                    } else if (ch1 > ch2) {
                        return -1;
                    }
                }
                return 0;
            });
            String result = "";
            for (String str : list) {
                result += str;
            }
            int i = 0;
            while (i < result.length() && result.charAt(i) == '0') {
                i++;
            }
            return i < result.length() ? result.substring(i) : "0";
        }
    }

//    private int compare(int a,int b){
//        int wa = Integer.highestOneBit(a);
//        int wb = Integer.highestOneBit(b);
//
//    }

//leetcode submit region end(Prohibit modification and deletion)

}