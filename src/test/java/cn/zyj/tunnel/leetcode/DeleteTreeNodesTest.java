package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RunWith(JUnit4.class)
public class DeleteTreeNodesTest {

    public int deleteTreeNodes(int nodes, int[] parent, int[] value) {
        int count = nodes;
        Set<Integer> ids = new HashSet<>();
        for (int id = nodes - 1; id >= 0; id--) { // id:[0,nodes-1]
            Set<Integer> cids = new HashSet<>();
            for (int cid = id + 1; cid < nodes; cid++) { // 子节点cid:[1,nodes-1]
                if (parent[cid] == id) {
                    value[id] += value[cid];
                    cids.add(cid);
                }
            }
            if (value[id] == 0) {
                count -= cids.size();
                ids.removeAll(cids);
                ids.add(id);
            }
        }
        count -= ids.size();
        return count;
    }

    @Test
    public void test() {
//        输入：nodes = 7, parent = [-1,0,0,1,2,2,2], value = [1,-2,4,0,-2,-1,-1]
//        输出：2
        Assert.assertEquals(2, deleteTreeNodes(7, new int[]{-1, 0, 0, 1, 2, 2, 2}, new int[]{1, -2, 4, 0, -2, -1, -1}));

    }

    @Test
    public void test2() {
        int nodes = 500;
        int[] parent = {-1, 0, 1, 1, 3, 4, 2, 4, 5, 5, 3, 7, 1, 4, 9, 14, 9, 11, 3, 4, 11, 19, 0, 21, 10, 1, 12, 20, 10, 24, 29, 19, 21, 18, 5, 16, 2, 4, 0, 31, 31, 15, 17, 7, 43, 22, 38, 40, 42, 33, 8, 47, 6, 11, 26, 45, 38, 4, 34, 1, 10, 40, 18, 33, 47, 9, 56, 43, 8, 26, 21, 25, 56, 31, 26, 5, 7, 56, 56, 6, 68, 76, 40, 76, 11, 58, 55, 75, 44, 17, 51, 22, 33, 53, 19, 33, 53, 8, 56, 47, 33, 12, 59, 30, 42, 8, 11, 30, 29, 99, 30, 89, 101, 4, 30, 12, 30, 64, 29, 42, 101, 54, 114, 77, 35, 122, 29, 98, 78, 127, 55, 8, 32, 85, 57, 22, 16, 75, 51, 95, 71, 76, 107, 134, 132, 61, 125, 81, 54, 15, 11, 69, 44, 126, 21, 117, 19, 134, 118, 80, 85, 117, 64, 82, 113, 99, 31, 19, 46, 132, 67, 68, 154, 137, 100, 41, 148, 56, 112, 63, 35, 158, 112, 93, 5, 48, 114, 90, 16, 149, 182, 7, 31, 121, 114, 76, 10, 190, 166, 78, 67, 136, 156, 88, 1, 201, 118, 140, 197, 54, 178, 65, 126, 68, 52, 153, 139, 191, 5, 143, 41, 216, 0, 39, 117, 183, 52, 67, 119, 89, 82, 152, 224, 11, 155, 149, 32, 159, 44, 231, 231, 15, 0, 235, 52, 58, 179, 145, 17, 113, 30, 238, 216, 169, 191, 26, 229, 234, 255, 82, 135, 256, 51, 262, 247, 132, 11, 115, 45, 27, 131, 195, 238, 12, 105, 52, 40, 15, 9, 49, 192, 58, 122, 240, 78, 27, 234, 233, 51, 102, 240, 181, 14, 92, 187, 162, 9, 184, 250, 211, 165, 28, 221, 232, 301, 6, 25, 289, 168, 222, 0, 85, 201, 182, 23, 246, 133, 164, 238, 293, 89, 112, 42, 299, 138, 70, 320, 184, 85, 222, 288, 260, 317, 225, 330, 235, 302, 274, 69, 12, 162, 97, 8, 26, 225, 111, 236, 345, 107, 258, 139, 257, 220, 326, 9, 91, 161, 333, 78, 317, 90, 200, 82, 226, 124, 37, 288, 93, 360, 205, 305, 242, 13, 337, 180, 257, 57, 364, 292, 312, 55, 236, 124, 34, 328, 68, 97, 54, 314, 76, 114, 38, 30, 330, 189, 112, 207, 38, 364, 130, 288, 134, 252, 82, 134, 346, 260, 184, 44, 338, 315, 240, 145, 223, 226, 265, 103, 114, 337, 355, 17, 104, 416, 100, 329, 276, 64, 12, 405, 417, 269, 176, 167, 204, 69, 235, 177, 4, 369, 211, 205, 247, 25, 130, 20, 305, 247, 378, 17, 285, 387, 355, 438, 25, 10, 0, 22, 87, 319, 273, 142, 174, 360, 327, 367, 193, 371, 215, 273, 356, 6, 150, 204, 182, 2, 331, 342, 56, 63, 152, 204, 431, 204, 41, 467, 378, 90, 108, 207, 353, 61, 210, 278, 318, 290, 172, 136, 104, 371, 382};
        int[] value = {-630, 641, 886, 656, 4, 6593, -567, -546, -728, -1746, -272, -822, -253, 301, -180, -902, -872, -428, 695, -999, -1114, 880, 682, 637, -86, -598, -1081, 253, 659, -528, -626, -377, -840, -761, -606, -551, 556, 909, -433, -982, -629, 720, 283, -646, 57, 110, -755, 169, 967, -955, -425, 704, -733, 459, -193, 1036, 111, -648, 1341, -405, -942, 414, -273, -641, -486, -431, 0, -478, -112, 145, -988, 572, 100, 0, -494, -1237, -611, 365, 864, -534, -416, 351, 633, 899, -437, -226, -918, -429, 304, 70, -323, 1368, 990, -2599, -883, 649, -571, 57, -719, 146, 852, -624, 828, -387, -249, -44, 456, -873, -100, 321, 0, -625, -898, -526, -32, -214, 0, -2610, -471, 682, -292, 858, -99, -977, -209, -818, -421, -723, 0, 363, -371, -617, 1240, -798, -754, -330, -1161, 35, 726, -421, -359, 0, 661, -482, 746, -894, 0, 45, -360, -22, -384, -958, -319, -95, 220, -460, -163, 978, 447, -864, 0, 301, -427, -770, 927, -446, 322, 217, 294, 827, 0, -454, -916, 729, -461, -146, -1188, 724, 484, -161, -458, 520, -204, -117, -184, -676, -185, -924, 748, -792, -730, 688, -243, 113, 145, 138, -868, 554, 0, 0, -18, 216, 0, 0, -425, 502, 124, 162, 0, -358, -716, 5, 414, -405, -368, 95, 806, 0, -982, 549, 23, -937, -963, -31, -749, 104, -1017, 0, 916, 0, 589, -564, 0, 435, -45, -189, -409, -74, 756, 158, 656, 249, -413, -306, 715, 632, -223, 813, 0, -968, -874, 0, 880, -827, 486, 683, 127, 988, -913, 0, 959, -127, -880, -58, -108, -773, 191, 180, -110, 605, 247, -515, 191, 893, 845, 0, -715, -588, 944, 914, 243, 0, 750, -406, 513, -310, -654, 207, -742, 290, -364, -520, -464, -697, 0, 376, 0, 0, -380, 976, 228, 372, 473, 0, 0, 838, 906, -253, 461, 723, 0, -522, 566, 357, -750, 603, 798, -408, 1000, 696, 30, 725, -609, -976, -965, 770, 548, 404, -685, 576, 387, -574, 398, 370, -276, 495, 451, 769, 982, -193, 446, -560, -767, 501, -409, -61, 64, 686, 940, 0, 505, 0, 935, -967, 129, 108, 529, 981, 0, 10, 944, -800, 0, 272, 86, 974, -50, 734, 0, -1002, 439, -867, -9, -312, 974, 428, -994, 0, 899, -303, 283, 120, -396, 736, -609, -211, 12, 780, 750, -153, 0, 468, 330, 122, 370, 881, -321, 101, -320, -441, 962, -399, 0, 274, -744, 983, 0, 699, -75, -461, -219, 156, -401, 0, 745, -739, 105, -213, 408, -773, 555, -844, 371, 311, -278, 715, -110, -164, 0, 50, 544, 316, -217, 950, 620, 0, 785, -780, 276, -338, -18, 779, 823, 411, 22, 300, -189, 302, -561, 0, 143, -703, 726, -753, -940, 0, 999, 902, 1, 0, 0, -106, -467, 0, 0, -586, 605, 1000, -742, 517, -8, 384, -406, 0, 497, 574, 767, 930, 711, -824, 0, 872, -194, 163, -1000, -951, 770, 763, 159, 419, 734, 232, -267, 968, 364, 696, 198, -470, -521, -110};

        Assert.assertEquals(326, deleteTreeNodes(nodes, parent, value));

    }


}
