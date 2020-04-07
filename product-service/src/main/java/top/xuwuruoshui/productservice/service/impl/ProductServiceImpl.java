package top.xuwuruoshui.productservice.service.impl;

import org.springframework.stereotype.Service;
import top.xuwuruoshui.productservice.domain.Product;
import top.xuwuruoshui.productservice.service.ProductService;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    public static final Map<Integer, Product> daoMap = new HashMap<>();

    static {
        Product p1 = new Product(1,"冰箱",700,100);
        Product p2 = new Product(2,"洗衣机",130,108);
        Product p3 = new Product(3,"电话",2300,102);
        Product p4 = new Product(4,"杯子",20,210);
        Product p5 = new Product(5,"笔记本",5000,90);
        Product p6 = new Product(6,"键盘",1000,100);
        Product p7= new Product(7,"鼠标",1230,910);

        daoMap.put(p1.getId(),p1);
        daoMap.put(p2.getId(),p2);
        daoMap.put(p3.getId(),p3);
        daoMap.put(p4.getId(),p4);
        daoMap.put(p5.getId(),p5);
        daoMap.put(p6.getId(),p6);
        daoMap.put(p7.getId(),p7);
    }


    @Override
    public List<Product> listProduct() {
        Collection<Product> products = daoMap.values();
        List<Product> list = new ArrayList<>(products);
        return list;
    }

    @Override
    public Product findById(int id) {
        return daoMap.get(id);
    }
}
