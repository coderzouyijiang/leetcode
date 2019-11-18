package cn.zyj.tunnel.leetcode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class 有效的括号 {

    public boolean isValid(String s) {
        return ValidCharPair.firstUnvalidIndex(s) == -1;
    }

    @Test
    public void quest_有效的括号() {
        Assert.assertEquals(true, isValid("()"));
        Assert.assertEquals(true, isValid("()[]"));
        Assert.assertEquals(true, isValid("()[]{}"));
        Assert.assertEquals(true, isValid("([]){}"));
        Assert.assertEquals(true, isValid("({[]})"));
        Assert.assertEquals(false, isValid("({[}])"));
    }

}
