package cn.zyj.tunnel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class ShortTest {

    @Test
    public void test() {
        /*
        Short s = 0;
        if (s == null) {
            log.info("true");
        } else {
            log.info("false");
        }
        Assert.assertTrue(s == null);
        Short s2 = 1;
        Assert.assertFalse(s2 == null);
        */
        Short s2 = Short.valueOf("1");
        Short s3 = Short.valueOf("1");
        short s4 = 1;
        log.info("" + (s2 == s4));
        log.info("" + (s2.equals(s4)));
        log.info("" + (s2 == s3));
        log.info("" + (s2.equals(s3)));
    }

}
