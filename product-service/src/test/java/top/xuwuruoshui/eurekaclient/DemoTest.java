package top.xuwuruoshui.eurekaclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoTest {
    public static void main(String[] args) {
        final List list = new ArrayList();
       list.add(1);
       list.add(2);
       list.remove(0);
        System.out.println(Arrays.asList(list));
    }
}
