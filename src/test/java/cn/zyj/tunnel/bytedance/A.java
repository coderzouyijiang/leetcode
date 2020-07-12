package cn.zyj.tunnel.bytedance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class A {

    class Solution {

    }

    @Test
    public void test() {
        Solution solution = new Solution();
        Assert.assertEquals(0, solution);
    }
}
