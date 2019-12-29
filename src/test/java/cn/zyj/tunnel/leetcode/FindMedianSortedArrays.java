package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
@Slf4j
public class FindMedianSortedArrays {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        if (n == 0 || nums1[m - 1] < nums2[0]) {
            return getMidVal(nums1, nums2);
        }
        if (m == 0 || nums2[n - 1] < nums1[0]) {
            return getMidVal(nums2, nums1);
        }
        int i1 = getIndex(nums1, 0, m, nums2[0]);
        if (i1 >= (m - i1) + n) {
            return getMidVal(nums1, nums2);
        }
        int j1 = getIndex(nums1, 0, m, nums2[n - 1]);
        if ((m - j1 - 1) >= j1 + n) {
            return getMidVal(nums2, nums1);
        }
        int i2 = getIndex(nums2, 0, n, nums1[0]);
        if (i2 >= (n - i2) + m) {
            return getMidVal(nums2, nums1);
        }
        int j2 = getIndex(nums2, 0, n, nums1[m - 1]);
        if ((n - j2 - 1) >= j2 + m) {
            return getMidVal(nums1, nums2);
        }
//        int left = i1 + i2;
//        int right = (m - j1 - 1) + (n - j2 - 1);
//        int mid = m + n - left - right;
        return -1;
    }

    public int getIndex(int[] arr, int fromIndex, int toIndex, int key) {
        int index = Arrays.binarySearch(arr, fromIndex, toIndex, key);
        return index >= 0 ? index : (-index - 1);
    }

    public int getMidVal(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int len = m + n;
        int index1 = len / 2 - 1;
        int index2 = index1 + len % 2;
        int v1 = index1 < m ? nums1[index1] : nums2[index1 - m];
        int v2 = index2 < m ? nums1[index2] : nums2[index2 - m];
        return (v1 + v2) / 2;
    }

}
