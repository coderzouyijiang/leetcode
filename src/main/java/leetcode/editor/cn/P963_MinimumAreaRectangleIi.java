//给定在 xy 平面上的一组点，确定由这些点组成的任何矩形的最小面积，其中矩形的边不一定平行于 x 轴和 y 轴。 
//
// 如果没有任何矩形，就返回 0。 
//
// 
//
// 示例 1： 
//
// 
//
// 输入：[[1,2},{2,1},{1,0},{0,1]]
//输出：2.00000
//解释：最小面积的矩形出现在 [1,2},{2,1},{1,0},{0,1] 处，面积为 2。
//
// 示例 2：
//
//
//
// 输入：[[0,1},{2,1},{1,1},{1,0},{2,0]]
//输出：1.00000
//解释：最小面积的矩形出现在 [1,0},{1,1},{2,1},{2,0] 处，面积为 1。
//
//
// 示例 3：
//
//
//
// 输入：[[0,3},{1,2},{3,1},{1,3},{2,1]]
//输出：0
//解释：没法从这些点中组成任何矩形。
//
//
// 示例 4：
//
//
//
// 输入：[[3,1},{1,1},{0,1},{2,1},{3,3},{3,2},{0,2},{2,3]]
//输出：2.00000
//解释：最小面积的矩形出现在 [2,1},{2,3},{3,3},{3,1] 处，面积为 2。
//
//
//
//
// 提示：
//
//
// 1 <= points.length <= 50
// 0 <= points[i][0] <= 40000
// 0 <= points[i][1] <= 40000
// 所有的点都是不同的。
// 与真实值误差不超过 10^-5 的答案将视为正确结果。
//
// Related Topics 几何 数学

package leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//Java：Minimum Area Rectangle II
public class P963_MinimumAreaRectangleIi {
    public static void main(String[] args) {
        Solution solution = new P963_MinimumAreaRectangleIi().new Solution();
        // TO TEST
//        int[][] arr = {{1, 2}, {2, 1}, {1, 0}, {0, 1}};
//        int[][] arr = {{0, 1}, {2, 1}, {1, 1}, {1, 0}, {2, 0}};
//        int[][] arr = {{3, 1}, {1, 1}, {0, 1}, {2, 1}, {3, 3}, {3, 2}, {0, 2}, {2, 3}}; // 2
//        int[][] arr = {{0, 2}, {0, 1}, {3, 3}, {1, 0}, {2, 3}, {1, 2}, {1, 3}}; // 0
        int[][] arr = {{0, 1}, {1, 0}, {3, 2}, {2, 3}, {0, 3}, {1, 1}, {3, 3}, {0, 2}}; // 3
        double result = solution.minAreaFreeRect(arr);
        System.out.println("" + result);
    }


    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        private static final int x = 0;
        private static final int y = 1;
        private static final double maxErr = 0.00001;

        public double minAreaFreeRect(int[][] points) {
            double area = helpMinAreaFreeRect(points, new ArrayList<>(4), 0);
            return area == Double.MAX_VALUE ? 0 : Math.sqrt(area);
        }

        public double helpMinAreaFreeRect(int[][] points, List<int[]> ps, int startIndex) {
            if (ps.size() == 4) {
                double area = computeArea(ps);
                System.out.printf("%s=%s\n", Arrays.deepToString(ps.toArray()), area);
                return area;
            }
            double minArea = Double.MAX_VALUE;
            int endIndex = points.length - (4 - ps.size());
            for (int i = startIndex; i <= endIndex; i++) {
                List<int[]> ps2;
                if (i < endIndex) {
                    ps2 = new ArrayList<>(4);
                    ps2.addAll(ps);
                } else {
                    ps2 = ps;
                }
                ps2.add(points[i]);
                double area = helpMinAreaFreeRect(points, ps2, i + 1);
                minArea = Math.min(area, minArea);
            }
            return minArea;
        }

        public double computeArea(List<int[]> ps) {
            int[] p0 = ps.get(0);
            int[] p1 = ps.get(1);
            int[] p2 = ps.get(2);
            int[] p3 = ps.get(3);
            int dx01 = p1[x] - p0[x];
            int dy01 = p1[y] - p0[y];
            int dx02 = p2[x] - p0[x];
            int dy02 = p2[y] - p0[y];
            if (Math.abs(dx01 * dx02 + dy01 * dy02) < maxErr) {
            } else {
                int dx03 = p3[x] - p0[x];
                int dy03 = p3[y] - p0[y];
                if (Math.abs(dx01 * dx03 + dy01 * dy03) < maxErr) {
                    p2 = ps.get(3);
                    p3 = ps.get(2);
                    dx02 = dx03;
                    dy02 = dy03;
                } else {
                    return Double.MAX_VALUE;
                }
            }
            int dx31 = p1[x] - p3[x];
            int dy31 = p1[y] - p3[y];
            int dx32 = p2[x] - p3[x];
            int dy32 = p2[y] - p3[y];
            if (Math.abs(dx31 * dx32 + dy31 * dy32) < maxErr
                    && Math.abs(dx31 * dx01 + dy31 * dy01) < maxErr) {
//                return Math.sqrt(dx01 * dx01 + dy01 * dy01) * Math.sqrt(dx02 * dx02 + dy02 * dy02);
                return (dx01 * dx01 + dy01 * dy01) * (dx02 * dx02 + dy02 * dy02);
            }
            return Double.MAX_VALUE;
        }

        public double computeArea2(List<int[]> ps) {
            double tx = 0, ty = 0;
            for (int[] p : ps) {
                tx += p[x];
                ty += p[y];
            }
            double cx = tx / 4, cy = ty / 4;
            int[] p0 = ps.get(0);
            double dx0 = p0[x] - cx, dy0 = p0[y] - cy;
            double len0 = dx0 * dx0 + dy0 * dy0;
            int num1 = 0, num2 = 0; // 垂直数，平行数
            for (int i = 1; i < ps.size(); i++) {
                int[] pi = ps.get(i);
                double dxi = pi[x] - cx, dyi = pi[y] - cy;
                double leni = dxi * dxi + dyi * dyi;
                if (Math.abs(len0 - leni) > maxErr) {
                    return Double.MAX_VALUE;
                }
                if (dx0 + dxi < maxErr && dy0 + dyi < maxErr) {
                    num2++; // 平行且相反
                } else if (Math.abs(dx0 * dxi + dy0 * dyi) < maxErr) {
                    num1++; // 垂直
                } else {
                    return Double.MAX_VALUE;
                }
            }
            if (num1 == 2 && num2 == 1) {
                return 2 * len0;
            }
            return Double.MAX_VALUE;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}