package cn.zyj.tunnel.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class NumOfBergsTest {

    public List<Integer> numOfBurgers(int tomatoSlices, int cheeseSlices) {
        // input:t,c
        // output:x,y
        // t=4*x+2*y => t=4*x+2(c-x) => t=2*x+2*c => x=(t-2*c)/2 => x=(t/2)-c
        // c=x+y => y=c-x
        int x, y;
        if (tomatoSlices % 2 != 0
                || (x = (tomatoSlices / 2) - cheeseSlices) < 0
                || (y = cheeseSlices - x) < 0) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(x, y);
    }

    @Test
    public void test() {
        Assert.assertEquals(Arrays.asList(1, 6), numOfBurgers(16, 7));
        Assert.assertEquals(Arrays.asList(), numOfBurgers(4, 7));
    }
}
