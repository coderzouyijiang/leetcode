package cn.zyj.tunnel.javafx;

import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JfxTest {

    @Test
    public void test() {
//        App1.launch("a=1", "b", "c=2,3");
        Application.launch(App1.class, "a=1", "b", "c=2,3", "-d", "123", "--e", "456");
    }

}
